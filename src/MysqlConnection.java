import java.sql.*;

public class MysqlConnection {
    // 驱动路径
    private static String driver="com.mysql.cj.jdbc.Driver";
    public static String url = "jdbc:mysql://localhost:3306/demo?serverTimezone=UTC";
    public static String user = "mydemo";
    public static String password = "mydemo";
    // 驱动程序放在静态代码中，只在类加载的时候加载一次，且在构造方法前加载
    static{
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Connection con = null;

    // 获取数据库连接的方法
    public static Connection getConnection() throws SQLException {
        con = DriverManager.getConnection(url, user, password);
        return con;
    }
    // 关闭资源的方法
    public static void closeResource(ResultSet rs, Statement st, Connection con){
        closeResultSet(rs);
        closeStatement(st);
        closeConnection(con);
    }
    // 释放连接
    public static void closeConnection(Connection con) {
        if(con != null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 等待垃圾回收
        con = null;
    }
    // 释放语句执行者
    public static void closeStatement(Statement st){
        if(st != null){
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        st = null;
    }
    // 释放结果集
    public static void closeResultSet(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        rs = null;
    }
}
