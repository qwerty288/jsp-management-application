package net.usermanagement.web;

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

import net.usermanagement.dao.UserDAO;
import net.usermanagement.model.Lesson;
import net.usermanagement.model.User;



@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	public void init() {
		userDAO = new UserDAO();
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
				insertUser(request, response);
				break;
			case "/insertLesson":
				insertLesson(request, response);
				break;
			case "/delete":
				deleteUser(request, response);
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
				updateUser(request, response);
				break;
			case "/updateLesson":
				updateLesson(request, response);
				break;
			case "/newLesson":
				showLessonForm(request, response);
				break;
			case "/listUser":
				listUser(request, response);
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

	private void insertUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String number = request.getParameter("number");
		User newUser = new User(name, email, number);
		userDAO.insertUser(newUser);
		response.sendRedirect("list");
	}	
	
	private void insertLesson(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String[] selectedUsers = request.getParameterValues("selectedUsers");
		String temp = request.getParameter("weeks");
		int selectedWeeks = 0;
		if (!temp.equals("")) {
			selectedWeeks = Integer.valueOf(request.getParameter("weeks"));
		}
		String start_date =  request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		String name = request.getParameter("name");
		Lesson newLesson = new Lesson(name, start_date, end_date);
		userDAO.insertLesson(newLesson, selectedWeeks, Arrays.toString(selectedUsers).replace("[", "").replace("]", "").replace(" ", ""));
		response.sendRedirect("list");
	}

	private void listLesson(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Lesson> listLesson = userDAO.selectAllLessons();
		request.setAttribute("listLesson", listLesson);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}
	
	private void listAll(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<User> listUser = userDAO.selectAllUsers();
		request.setAttribute("listUser", listUser);
		List<Lesson> listLesson = userDAO.selectAllLessons();
		request.setAttribute("listLesson", listLesson);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}

	private void listUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<User> listUser = userDAO.selectAllUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}

	private void showLessonForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<User> listUser = userDAO.selectAllUsers();
		request.setAttribute("listUser", listUser);	
		List<User> listDate = new ArrayList<>();
		for (int i=0;i<52;i++) {
			listDate.add(new User(i, "", "", ""));
		}
		request.setAttribute("listDate", listDate);
		RequestDispatcher dispatcher = request.getRequestDispatcher("lesson-form.jsp");
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser = userDAO.selectUser(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-edit-form.jsp");
		request.setAttribute("user", existingUser);
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
		Lesson existingLesson = userDAO.selectLesson(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("lesson-edit-form.jsp");
		request.setAttribute("lesson", existingLesson);
		List<User> listUser = userDAO.selectAllUsers();
		//Need to check users
		//1. get string
		String userListFromLesson = userDAO.getLessonUserList(id);
		//2. process string and set users as checked
		int stringPos = 0;
		String tempString = "     ";
		for (int i=0;i<userListFromLesson.length();i++) {
			System.out.println("i:"+i);
			System.out.println(userListFromLesson.charAt(i));
			if (userListFromLesson.charAt(i)==",".charAt(0))  {
				//set checked here
				listUser.get(Integer.valueOf(tempString.trim())-1).setChecked();
				stringPos = 0;
				tempString = "      ";
			} else {
				tempString = addChar(tempString, userListFromLesson.charAt(i), stringPos);
				stringPos++;
				if (i==userListFromLesson.length()-1) {
					listUser.get(Integer.valueOf(tempString.trim())-1).setChecked();
				}
			}
		}
		request.setAttribute("listUser", listUser);
		dispatcher.forward(request, response);
	}	
	
	private void updateUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String number = request.getParameter("number");

		User book = new User(id, name, email, number);
		userDAO.updateUser(book);
		response.sendRedirect("list");
	}
	
	private void updateLesson(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		String[] selectedUsers = request.getParameterValues("selectedUsers");
		Lesson book = new Lesson(id, name, start_date, end_date);
		userDAO.updateLesson(book, Arrays.toString(selectedUsers).replace("[", "").replace("]", "").replace(" ", ""));
		response.sendRedirect("list");
	}	
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		userDAO.deleteUser(id);
		response.sendRedirect("list");
	}

	private void deleteLesson(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		userDAO.deleteLesson(id);
		response.sendRedirect("list");
	}
	
}
