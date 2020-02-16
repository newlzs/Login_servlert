import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class register extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public register() throws SQLException {
        super();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        Connection con = null;
        try {
            con = MysqlConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        String title = "注册反馈页面";
        String account = new String(request.getParameter("account").getBytes("ISO-8859-1"), "utf-8");
        String password = new String(request.getParameter("password"));
        System.out.println(account + "密码" + password);
        // 添加用户信息
        Statement sql_statement = null;
        try {
            sql_statement = con.createStatement();
            String query = "select  * from user WHERE account='" + account + "'";
            ResultSet result = sql_statement.executeQuery(query);
            // 用户名已存在
            if(result.next()){
                out.println("<meta http-equiv=\"refresh\" content=\"2;url=register.html\"> ");
                out.println("用户名已经存在, 正在跳转到注册页面重新注册。。。");
            }else{
                // 否则添加用户
                String sql = "insert into user(account,password) values (?,?)";
                PreparedStatement pst = con.prepareStatement(sql); // 创建查询对象
                pst.setString(1, account);
                pst.setString(2, password);
                pst.executeUpdate();
                pst.close();
                out.println("用户注册成功");
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
