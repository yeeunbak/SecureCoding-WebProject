<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
</head>
<body>

<div class="user-container">
    <h2>로그인</h2>
    <form action="<%=request.getContextPath()%>/login" method="post">
        <table>
            <tr>
                <td>아이디</td>
                <td><input type="text" name="userId" required></td>
            </tr>
            <tr>
                <td>비밀번호</td>
                <td><input type="password" name="userPw" required></td>
            </tr>
        </table>

        <div class="btn-area">
            <input type="submit" value="로그인">
            <input type="button" value="회원가입" onclick="location.href='join.jsp'">
        </div>
    </form>
</div>

<script>
    const msg = "<%= request.getParameter("msg") == null ? "" : request.getParameter("msg") %>";

    if (msg == "fail") {
        alert("아이디 또는 비밀번호가 올바르지 않습니다.");
    } else if (msg == "logout") {
        alert("로그아웃되었습니다.");
    }
</script>
</body>
</html>