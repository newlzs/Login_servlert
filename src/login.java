import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class login extends HttpServlet {
    public login() throws SQLException{
        super();
    }

    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        Connection con = null;
        try {
            con = MysqlConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        String account = new String(request.getParameter("account").getBytes("ISO-8859-1"), "utf-8");
        String password = new String(request.getParameter("password"));
        System.out.println(account + "密码: " + password);

        // 验证密码
        Statement sql_statement = null;
        try {
            sql_statement = con.createStatement();
            String query = "select  * from user WHERE account='" + account + "'";
            ResultSet result = sql_statement.executeQuery(query);

            if(result.isBeforeFirst()){
                System.out.println("asdfasdfasdfasdfasdfsadfasf");
                result.next();
                String true_password = result.getString("password");

                // 清理资源
                result.close();
                sql_statement.close();

                if(true_password.equals(password)) out.println("登陆成功");
                else out.println("登陆失败");
            }else{
                out.println("用户不存在");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
