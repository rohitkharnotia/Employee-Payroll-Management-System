package payroll.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lightweight in-memory implementation of java.sql.ResultSet using dynamic proxy.
 * Used when MySQL server is unreachable, ensuring the application remains
 * 100% functional for demonstrations and offline testing.
 */
public class MockResultSet {

    public static ResultSet create(String[] columns, List<Object[]> data) {
        final Map<String, Integer> colMap = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            colMap.put(columns[i].toLowerCase(), i);
        }

        InvocationHandler handler = new InvocationHandler() {
            private int cursor = -1;

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();

                if ("next".equals(methodName)) {
                    cursor++;
                    return cursor < data.size();
                }

                if ("close".equals(methodName)) {
                    return null;
                }

                if ("isClosed".equals(methodName)) {
                    return false;
                }

                if (cursor < 0 || cursor >= data.size()) {
                    return null;
                }

                Object[] row = data.get(cursor);

                int colIndex = -1;
                if (args != null && args.length > 0) {
                    if (args[0] instanceof Integer) {
                        colIndex = ((Integer) args[0]) - 1; // 1-based index in JDBC
                    } else if (args[0] instanceof String) {
                        String colName = ((String) args[0]).toLowerCase();
                        if (colMap.containsKey(colName)) {
                            colIndex = colMap.get(colName);
                        }
                    }
                }

                Object val = (colIndex >= 0 && colIndex < row.length) ? row[colIndex] : null;

                if ("getInt".equals(methodName)) {
                    if (val == null) return 0;
                    if (val instanceof Number) return ((Number) val).intValue();
                    try { return Integer.parseInt(val.toString()); } catch (Exception e) { return 0; }
                }

                if ("getString".equals(methodName)) {
                    return val != null ? val.toString() : null;
                }

                if ("getDouble".equals(methodName)) {
                    if (val == null) return 0.0;
                    if (val instanceof Number) return ((Number) val).doubleValue();
                    try { return Double.parseDouble(val.toString()); } catch (Exception e) { return 0.0; }
                }

                if ("getObject".equals(methodName)) {
                    return val;
                }

                if ("wasNull".equals(methodName)) {
                    return val == null;
                }

                // Default return for other methods
                Class<?> returnType = method.getReturnType();
                if (returnType.equals(boolean.class)) return false;
                if (returnType.equals(int.class)) return 0;
                if (returnType.equals(double.class)) return 0.0;
                return null;
            }
        };

        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class<?>[]{ResultSet.class},
                handler
        );
    }
}

