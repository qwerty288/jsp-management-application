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
				<a class="navbar-brand"> Lesson
					Management App </a>
			</div>

		</nav>
	</header>
	<br>

	<div class="row">
		<!-- <div class="alert alert-success" *ngIf='message'>{{message}}</div> -->

		<div class="container">
			<h3 class="text-center">List of Students</h3>
			<hr>
			<div class="container text-left">

				<a href="<%=request.getContextPath()%>/new" class="btn btn-success">Add
					New Student</a>
			</div>
			<br>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>ID</th>
						<th>Name</th>
						<th>Email</th>
						<th>Number</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<!--   for (Todo todo: todos) {  -->
					<c:forEach var="user" items="${listStudent}">

						<tr>
							<td><c:out value="${user.id}" /></td>
							<td><c:out value="${user.name}" /></td>
							<td><c:out value="${user.email}" /></td>
							<td><c:out value="${user.number}" /></td>
							<td><a href="edit?id=<c:out value='${user.id}' />">Edit</a>
								&nbsp;&nbsp;&nbsp;&nbsp; <a
								href="delete?id=<c:out value='${user.id}' />">Delete</a></td>
						</tr>
					</c:forEach>
					<!-- } --> 
				</tbody>

			</table>
			
			<hr>
			<div class="container text-other">
			<h3 class="text-center">List of Lessons</h3>
				<a href="<%=request.getContextPath()%>/newLesson" class="btn btn-success">Add
					New Lesson</a>
				<a href="http://localhost/scheduler-howto-php-connector-master/basic.html" class="btn btn-success">View Timetable</a>
			</div>
			<br>
						<table class="table table-bordered">
				<thead>
					<tr>
						<th>ID</th>
						<th>Name</th>
						<th>Start Date & Time</th>
						<th>End Date & Time</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<!--   for (Todo todo: todos) {  -->
					<c:forEach var="lesson" items="${listLesson}">
						<tr>
							<td><c:out value="${lesson.id}" /></td>
							<td><c:out value="${lesson.name}" /></td>
							<td><c:out value="${lesson.start_date}" /></td>
							<td><c:out value="${lesson.end_date}" /></td>
							<td><a href="editLesson?id=<c:out value='${lesson.id}' />">Edit Students</a>
								&nbsp;&nbsp;&nbsp;&nbsp; <a
								href="deleteLesson?id=<c:out value='${lesson.id}' />">Delete</a></td>
						</tr>
					</c:forEach>
					<!-- } -->
				</tbody>
			</table>

		</div>
	</div>

</body>
</html>
