<%@ page import="java.util.List" %>
<%@ page import="member.dto.MemberDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    @SuppressWarnings("unchecked")
    List<MemberDTO> memberList = (List<MemberDTO>) request.getAttribute("memberList");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사용자 관리</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css">
</head>

<body>

<div class="board-container">
    <h2>사용자 관리</h2>

    <table>
        <tr>
            <th>아이디</th>
            <th>이름</th>
            <th>이메일</th>
            <th>권한</th>
            <th>가입일</th>
            <th>관리</th>
        </tr>

<%
    if (memberList == null || memberList.isEmpty()) {
%>
        <tr>
            <td colspan="6">등록된 사용자가 없습니다.</td>
        </tr>
<%
    } else {
        for (MemberDTO member : memberList) {
%>
        <tr>
            <td><%= member.getUserId() %></td>
            <td><%= member.getUserName() %></td>
            <td><%= member.getUserEmail() %></td>
            <td>
                <form action="<%=request.getContextPath()%>/admin/member/roleUpdate" method="post">
                    <input type="hidden" name="userId" value="<%= member.getUserId() %>">

                    <select name="role">
                        <option value="USER" <%= "USER".equals(member.getUserRole()) ? "selected" : "" %>>USER</option>
                        <option value="ADMIN" <%= "ADMIN".equals(member.getUserRole()) ? "selected" : "" %>>ADMIN</option>
                    </select>

                    <input type="submit" value="변경">
                </form>
            </td>
            <td><%= member.getRegDate() %></td>
            <td>
                <form action="<%=request.getContextPath()%>/admin/member/delete" method="post" style="display:inline;">
                    <input type="hidden" name="userId" value="<%= member.getUserId() %>">
                    <input type="submit" value="계정삭제" onclick="return confirm('정말 이 사용자를 삭제하겠습니까?');">
                </form>
            </td>
        </tr>
<%
        }
    }
%>
    </table>

    <div class="btn-area">
        <input type="button" value="관리자 메인" onclick="location.href='<%=request.getContextPath()%>/admin/adminMain.jsp'">
    </div>
</div>

<%
    String msg = request.getParameter("msg");

    if ("hasBoard".equals(msg)) {
%>
    <script>alert("게시글이 있는 회원은 삭제할 수 없습니다.");</script>
<%
    } else if ("self".equals(msg)) {
%>
    <script>alert("본인 계정은 삭제할 수 없습니다.");</script>
<%
    } else if ("success".equals(msg)) {
%>
    <script>alert("회원이 삭제되었습니다.");</script>
<%
    } else if ("error".equals(msg)) {
%>
    <script>alert("삭제 중 오류가 발생했습니다.");</script>
<%
    }
%>

</body>
</html>