package member;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/checkDuplicate")
public class CheckDuplicateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService;

    public CheckDuplicateController() {
        memberService = new MemberServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");

        String type = request.getParameter("type");
        String value = request.getParameter("value");

        boolean exists = false;

        /* userId OR email 중복체크 */
        if ("userId".equals(type)) {
            exists = memberService.isUserIdExists(value);
        } else if ("userEmail".equals(type)) {
            exists = memberService.isEmailExists(value);
        }

        response.getWriter().write(exists ? "duplicate" : "available");
    }
}