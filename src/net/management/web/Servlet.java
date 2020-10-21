package net.management.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.management.dao.DAO;
import net.management.model.Lesson;
import net.management.model.Student;



@WebServlet("/")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DAO dao;

	public void init() {
		dao = new DAO();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertStudent(request, response);
				break;
			case "/insertLesson":
				insertLesson(request, response);
				break;
			case "/delete":
				deleteStudent(request, response);
				break;
			case "/deleteLesson":
				deleteLesson(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/editLesson":
				showEditLessonForm(request, response);
				break;
			case "/update":
				updateStudent(request, response);
				break;
			case "/updateLesson":
				updateLesson(request, response);
				break;
			case "/newLesson":
				showLessonForm(request, response);
				break;
			case "/listStudent":
				listStudent(request, response);
				break;
			case "/listLesson":
				listLesson(request, response);
				break;
			default:
				listAll(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void insertStudent(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String number = request.getParameter("number");
		Student newStudent = new Student(name, email, number);
		dao.insertStudent(newStudent);
		response.sendRedirect("list");
	}	
	
	private void insertLesson(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String[] selectedStudents = request.getParameterValues("selectedStudents");
		String temp = request.getParameter("weeks");
		int selectedWeeks = 0;
		if (!temp.equals("")) {
			selectedWeeks = Integer.valueOf(request.getParameter("weeks"));
		}
		String start_date =  request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		String name = request.getParameter("name");
		Lesson newLesson = new Lesson(name, start_date, end_date);
		dao.insertLesson(newLesson, selectedWeeks, Arrays.toString(selectedStudents).replace("[", "").replace("]", "").replace(" ", ""));
		response.sendRedirect("list");
	}

	private void listLesson(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Lesson> listLesson = dao.selectAllLessons();
		request.setAttribute("listLesson", listLesson);
		RequestDispatcher dispatcher = request.getRequestDispatcher("student-list.jsp");
		dispatcher.forward(request, response);
	}
	
	private void listAll(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Student> listStudent = dao.selectAllStudents();
		request.setAttribute("listStudent", listStudent);
		List<Lesson> listLesson = dao.selectAllLessons();
		request.setAttribute("listLesson", listLesson);
		RequestDispatcher dispatcher = request.getRequestDispatcher("student-list.jsp");
		dispatcher.forward(request, response);
	}

	private void listStudent(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Student> listStudent = dao.selectAllStudents();
		request.setAttribute("listStudent", listStudent);
		RequestDispatcher dispatcher = request.getRequestDispatcher("student-list.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void showLessonForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Student> listStudent = dao.selectAllStudents();
		request.setAttribute("listStudent", listStudent);	
		List<Student> listDate = new ArrayList<>();
		for (int i=0;i<52;i++) {
			listDate.add(new Student(i, "", "", ""));
		}
		request.setAttribute("listDate", listDate);
		RequestDispatcher dispatcher = request.getRequestDispatcher("lesson-form.jsp");
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Student existingStudent = dao.selectStudent(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("student-edit-form.jsp");
		request.setAttribute("student", existingStudent);
		dispatcher.forward(request, response);
	}

	public String addChar(String str, char ch, int position) {
	    StringBuilder sb = new StringBuilder(str);
	    sb.insert(position, ch);
	    return sb.toString();
	}
	
	private void showEditLessonForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Lesson existingLesson = dao.selectLesson(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("lesson-edit-form.jsp");
		request.setAttribute("lesson", existingLesson);
		List<Student> listStudent = dao.selectAllStudents();
		String studentListFromLesson = dao.getLessonStudentList(id);
		int stringPos = 0;
		String tempString = "     ";
		for (int i=0;i<studentListFromLesson.length();i++) {
			if (studentListFromLesson.charAt(i)==",".charAt(0))  {
				listStudent.get(Integer.valueOf(tempString.trim())-1).setChecked();
				stringPos = 0;
				tempString = "      ";
			} else {
				tempString = addChar(tempString, studentListFromLesson.charAt(i), stringPos);
				stringPos++;
				if (i==studentListFromLesson.length()-1) {
					listStudent.get(Integer.valueOf(tempString.trim())-1).setChecked();
				}
			}
		}
		request.setAttribute("listStudent", listStudent);
		dispatcher.forward(request, response);
	}	
	
	private void updateStudent(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String number = request.getParameter("number");

		Student book = new Student(id, name, email, number);
		dao.updateStudent(book);
		response.sendRedirect("list");
	}
	
	private void updateLesson(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		String[] selectedStudents = request.getParameterValues("selectedStudents");
		Lesson book = new Lesson(id, name, start_date, end_date);
		dao.updateLesson(book, Arrays.toString(selectedStudents).replace("[", "").replace("]", "").replace(" ", ""));
		response.sendRedirect("list");
	}	
	
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		dao.deleteStudent(id);
		response.sendRedirect("list");
	}

	private void deleteLesson(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		dao.deleteLesson(id);
		response.sendRedirect("list");
	}
	
}
