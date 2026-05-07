<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/join.css">
<script defer src="${pageContext.request.contextPath}/js/member/join.js"></script>
</head>
<body data-context-path="${pageContext.request.contextPath}">

<div class="user-container">
    <h2>회원가입</h2>

    <form id="joinForm" action="${pageContext.request.contextPath}/join" method="post">
        <table>
            <tr>
                <td>이름</td>
                <td>
                    <input type="text" name="userName" maxlength="30" required>
                </td>
            </tr>

            <tr>
                <td>아이디</td>
                <td>
                    <div class="input-check-row">
                        <input type="text" id="userId" name="userId" maxlength="20" required>
                        <button type="button" id="checkIdBtn">중복확인</button>
                    </div>
                    <div id="userIdMsg" class="field-message"></div>
                </td>
            </tr>

            <tr>
                <td>비밀번호</td>
                <td>
                    <input type="password" name="userPw" maxlength="30" required>
                </td>
            </tr>

            <tr>
                <td>생년월일</td>
                <td>
                    <input type="date" name="birthDate" required>
                </td>
            </tr>

            <tr>
                <td>이메일</td>
                <td>
                    <div class="input-check-row">
                        <input type="email" id="userEmail" name="userEmail" maxlength="100" required>
                        <button type="button" id="checkEmailBtn">중복확인</button>
                    </div>
                    <div id="userEmailMsg" class="field-message"></div>
                </td>
            </tr>
        </table>

        <div id="joinFormMsg" class="form-message"></div>

        <div class="btn-area">
            <input type="submit" value="회원가입">
            <input type="button" value="로그인" onclick="location.href='${pageContext.request.contextPath}/member/login.jsp'">
        </div>
    </form>
</div>

</body>
</html>