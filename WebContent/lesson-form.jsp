<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Lesson Management Application</title>
<link rel="stylesheet"
	href="https://drive.google.com/uc?export=view&id=13NlAxVuxNzrD__kd5xZzQa9DOSH729Om">
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
					<label>Start Date</label>
					<input type="datetime-local"
					value="<c:out value='${lesson.start_date}' />" class="form-control"
					name="start_date">
				</fieldset>

				<fieldset class="form-group">
					<label>End Date</label>
					<input type="datetime-local"
					value="<c:out value='${lesson.end_date}' />" class="form-control"
					name="end_date">
				</fieldset>
				
				<fieldset class="form-group">
				<label>Add Students</label>
				<br>
				<c:forEach var="user" items="${listUser}">
					<td>${user.name}</td>
					<td><input type="checkbox" name="selectedUsers" value="${user.id}"></td>
					<br>
				</c:forEach>
				</fieldset>

				<fieldset class="form-group">
					<label>Number of weekly repetitions to add</label>
					<input type="text"
					value="<c:out value='${lesson.weeks}' />" class="form-control"
					name="weeks">
				</fieldset>

				<button type="submit" class="btn btn-success">Save</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
