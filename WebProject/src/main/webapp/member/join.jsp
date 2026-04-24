<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
</head>
<body>

<div class="container">
    <h2>회원가입</h2>
    <form action="<%=request.getContextPath()%>/join" method="post">
        <table>
            <tr>
                <td>이름</td>
                <td><input type="text" name="userName" required></td>
            </tr>
            <tr>
                <td>아이디</td>
                <td><input type="text" name="userId" required></td>
            </tr>
            <tr>
                <td>비밀번호</td>
                <td><input type="password" name="userPw" required></td>
            </tr>
            <tr>
                <td>생년월일</td>
                <td><input type="date" name="birthDate" required></td>
            </tr>
            <tr>
                <td>이메일</td>
                <td><input type="email" name="userEmail" required></td>
            </tr>
        </table>

        <div class="btn-area">
            <input type="submit" value="회원가입">
            <input type="button" value="로그인" onclick="location.href='login.jsp'"> 
        </div>
    </form>
</div>

<script>
const msg = "<%= request.getParameter("msg") == null ? "" : request.getParameter("msg") %>";
    
if (msg === "duplicate") {
    alert("이미 사용 중인 아이디입니다.");
} else if (msg === "duplicateEmail") {
    alert("이미 사용 중인 이메일입니다.");
} else if (msg === "fail") {
    alert("회원가입 실패");
}
</script>

</body>
</html>