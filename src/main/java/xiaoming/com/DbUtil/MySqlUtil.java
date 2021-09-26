package xiaoming.com.DbUtil;

import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlUtil {



    /**
     * 创建连接数据库
     *
     * @return conn 连接
     */
    public static Connection connectDB() {
        String UserName = "root";
        String Password = "root";
        String url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&useSSL=false";


        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("加载驱动成功!!!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(url, UserName, Password);
            System.out.println("连接数据库成功!!!");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /***
     * 释放数据库连接
     * @param rs
     * @param st
     * @param conn
     */
    public static void free(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close(); // 关闭结果集
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close(); // 关闭Statement
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close(); // 关闭连接
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    /**
     * select 查询
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> MySqlSelect(String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = MySqlUtil.connectDB();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = ps.getMetaData();

            // 取得结果集列数
            int columnCount = rsmd.getColumnCount();

            // 构造泛型结果集
            List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
            Map<String, Object> data = null;

            // 循环结果集
            //System.out.println(rs.next());
            while (rs.next()) {
                data = new HashMap<String, Object>();
                // 每循环一条将列名和列值存入Map
                for (int i = 1; i < columnCount; i++) {
                    try {
                        data.put(rsmd.getColumnLabel(i), rs.getObject(rsmd
                                .getColumnLabel(i)));
                    } catch (Exception e) {
                        //	System.out.println("数据存储："+rsmd.getColumnLabel(i));
                        e.printStackTrace();
                    }

                }
                // 将整条数据的Map存入到List中
                datas.add(data);
            }
            //  System.out.println("准备释放");
            return datas;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            // System.out.println("执行finally");
            MySqlUtil.free(rs, ps, conn);
            //  System.out.println("释放成功");
        }
    }



    @Test
    public void dbTest() {

        String sql = "SELECT name ,age \n" +
                "FROM  user\n" +
                "WHERE  sex ='F';\n";
        MySqlUtil ms = new MySqlUtil();
        List<Map<String, Object>> ListResults = ms.MySqlSelect(sql);
        for (int i = 0; i < ListResults.size(); i++) {
            Map<String, Object> map = ListResults.get(i);
            for (String key : map.keySet()) {
                System.out.print(key + "=" + map.get(key)+"   ");
            }
            System.out.println();
        }
    }
}
