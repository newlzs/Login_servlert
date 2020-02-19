import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.xdevapi.JsonArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class get_message extends HttpServlet {
    public get_message(){
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        HttpSession session = req.getSession(false);
        PrintWriter out = resp.getWriter();
        Map<String,Object> result = new HashMap<String,Object>();

        if(session != null){
            String account = (String) session.getAttribute("account");

            result.put("code", 200);
            result.put("message", "用户信息");
            result.put("data", GetMessage_json(account));
            System.out.println("success");
        }else{
            result.put("code", 101);
            result.put("message", "未登录, 无法获取");
            result.put("data", null);
            System.out.println("fail");
        }
        JSONObject json = new JSONObject(result);
        out.println(json.toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    // 从数据库获取用户信息, 并转化为json
    protected JSONObject GetMessage_json(String account) {
        Connection con = null;
        JSONObject data = new JSONObject();
        try {
            con = MysqlConnection.getConnection();
            Statement sql_statement = con.createStatement();
            String query = "select  * from user WHERE account='" + account + "'";
            ResultSet sql_result = sql_statement.executeQuery(query);
            sql_result.next();

            // 获取列数
            ResultSetMetaData metaData = (ResultSetMetaData) sql_result.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 遍历每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName =metaData.getColumnLabel(i);
                String value = sql_result.getString(columnName);
                data.put(columnName, value);
            }
            sql_statement.close();
            sql_result.close();
        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}

