<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="member.dto.MemberDTO" %>
<%
    MemberDTO member = (MemberDTO) request.getAttribute("member");

    if (member == null) {
        response.sendRedirect(request.getContextPath() + "/main.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 수정</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
</head>
<body>

<div class="user-container">
    <h2>회원정보 수정</h2>

    <form action="<%=request.getContextPath()%>/editMember" method="post">
        <table>
            <tr>
                <td>아이디</td>
                <td>
                    <input type="text" name="userId" value="<%=member.getUserId()%>" readonly>
                </td>
            </tr>
            <tr>
                <td>비밀번호</td>
                <td>
                    <input type="password" name="userPw" value="<%=member.getUserPw()%>" required>
                </td>
            </tr>
            <tr>
                <td>이름</td>
                <td>
                    <input type="text" name="userName" value="<%=member.getUserName()%>" required>
                </td>
            </tr>
            <tr>
                <td>생년월일</td>
                <td>
                    <input type="date" name="birthDate" value="<%=member.getBirthDate()%>" required>
                </td>
            </tr>
            <tr>
                <td>이메일</td>
                <td>
                    <input type="email" name="userEmail" value="<%=member.getUserEmail()%>" required>
                </td>
            </tr>
        </table>

        <div class="btn-area">
            <input type="submit" value="수정하기">
            <input type="button" value="취소" onclick="location.href='main.jsp'">
        </div>
    </form>
</div>

</body>
</html>