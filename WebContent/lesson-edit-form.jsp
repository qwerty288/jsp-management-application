<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Lesson Management Application</title>
<link rel="stylesheet"
	href="https://drive.google.com/uc?export=view&id=13NlAxVuxNzrD__kd5xZzQa9DOSH729Om"
>
</head>
<body>

	<header>
		<nav class="navbar navbar-expand-md navbar-dark"
			style="background-color: grey">
			<div>
				<a class="navbar-brand"> Lesson Management App </a>
			</div>

		</nav>
	</header>
	<br>
	<div class="container col-md-5">
		<div class="card">
			<div class="card-body">
				<c:if test="${lesson != null}">
					<form action="updateLesson" method="post">
				</c:if>
				<c:if test="${lesson == null}">
					<form action="insertLesson" method="post">
				</c:if>

				<c:if test="${lesson != null}">
					<input type="hidden" name="id" value="<c:out value='${lesson.id}' />" />
				</c:if>

				<fieldset class="form-group">
					<label>Lesson Name</label> <input type="text"
						value="<c:out value='${lesson.name}' />" class="form-control"
						name="name" required="required">
				</fieldset>
				
				<fieldset class="form-group">
				<label>Students in lesson</label>
				<br>
				<c:forEach var="student" items="${listStudent}">
					<c:if test="${student.checked != null}">
					<td>${student.name}</td>
					<td><input type="checkbox" checked name="selectedUsers" value="${student.id}"></td>
					</c:if>
					<c:if test="${student.checked == null}">
					<td>${student.name}</td>
					<td><input type="checkbox" name="selectedUsers" value="${student.id}"></td>
					</c:if>
					<br>
				</c:forEach>
				</fieldset>

				<button type="submit" class="btn btn-success">Save</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
