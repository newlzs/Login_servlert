import com.mysql.cj.xdevapi.Session;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class logout extends HttpServlet {
    public logout(){
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        session.invalidate();
        System.out.println("解除成功");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        doPost(request, response);
    }
}
