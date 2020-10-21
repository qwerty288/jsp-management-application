package net.management.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.management.model.Lesson;
import net.management.model.Student;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DAO {
	private String jdbcURL = "jdbc:mysql://localhost/demo?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";

	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, number) VALUES "
			+ " (?, ?, ?);";
	private static final String INSERT_LESSONS_SQL = "INSERT INTO events" + "  (start_date, end_date, text, users) VALUES "
			+ " (?, ?, ?, ?);";
	private static final String SELECT_STUDENT_BY_ID = "select id,name,email,number from users where id =?";
	private static final String SELECT_LESSON_BY_ID = "select id,start_date,end_date,text from events where id =?";
	private static final String SELECT_LESSON_USERLIST_BY_ID = "select users from events where id =?";
	private static final String SELECT_ALL_STUDENTS = "select * from users";
	private static final String SELECT_ALL_LESSONS = "select * from events";
	private static final String DELETE_STUDENTS_SQL = "delete from users where id = ?;";
	private static final String DELETE_LESSONS_SQL = "delete from events where id = ?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, number =? where id = ?;";
	private static final String UPDATE_LESSONS_SQL = "update events set text =?, users =? where id = ?;";
	public DAO() {
	}
	/**
	* Reads the SQL Table and returns a list of lesson objects.
	* @return A list of lessons, sorted by ID.
	**/
	public List<Lesson> selectAllLessons() {
		// Declare empty list.
		List<Lesson> lessons = new ArrayList<>();
		// Establish connection to SQL database.
		try (Connection connection = getConnection();
			// Create a statement using connection object.
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_LESSONS);) {
			// Execute the query 
			ResultSet rs = preparedStatement.executeQuery();
			// Process the ResultSet object, and populate the list.
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("text");
				String start_date = (rs.getTimestamp("start_date").toString()).substring(0, (rs.getTimestamp("start_date").toString()).length() - 5);
				String end_date = (rs.getTimestamp("end_date").toString()).substring(0, (rs.getTimestamp("end_date").toString()).length() - 5);
				String userList = rs.getString("users");
				lessons.add(new Lesson(id, name, start_date, end_date));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		// Return populated list.
		return lessons;
	}
	/**
	* Inserts a new student into the SQL Table.
	* @param student The user object, containing information about the student to be added.
	*/
	public void insertStudent(Student student) throws SQLException {
		// Establish connection with the SQL Database.
		try (Connection connection = getConnection();
		     		// Create a statement using connection object.
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			// Update statement parameters.
			preparedStatement.setString(1, student.getName());
			preparedStatement.setString(2, student.getEmail());
			preparedStatement.setString(3, student.getNumber());
			// Execute the query.
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
	/**
	* Updates the details of an existing lesson in the SQL Table.
	* @param lesson The lesson object, containing the lesson start-date, end-date and name of the lesson.
	* @param studentList A string containing the list of ids of expected students in the lesson.
	*/
	public boolean updateLesson(Lesson lesson, String studentList) throws SQLException {
		boolean rowUpdated;
		// Establish connection with the SQL Database.
		try (Connection connection = getConnection();
		     		// Create a statement using connection object.
				PreparedStatement statement = connection.prepareStatement(UPDATE_LESSONS_SQL);) {
			// Update statement parameters.
			statement.setString(1, lesson.getName());
			statement.setString(2, studentList);
			statement.setInt(3, lesson.getId());
			// Execute the query.
			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}	
	/**
	* Inserts a new lesson into the SQL Table.
	* @param lesson The lesson object, containing the lesson start-date, end-date and name of the lesson.
	* @param weeks The number of consecutive weeks to add the lesson to.
	* @param studentList A string containing the list of ids of expected students in the lesson.
	**/	
	public void insertLesson(Lesson lesson, int weeks, String studentList) throws SQLException {
		// Establish connection with the SQL Database.
		try (Connection connection = getConnection();
		     		// Create a statement using connection object.
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LESSONS_SQL)) {
			// Update statement parameters.
			String start_date = lesson.getstart_date();
			String end_date = lesson.getend_date();
			String name = lesson.getName();
			preparedStatement.setString(1, start_date);
			preparedStatement.setString(2, end_date);
			preparedStatement.setString(3, name);
			preparedStatement.setString(4, studentList);
			// Execute the query.
			preparedStatement.executeUpdate();
			// Repeat above steps to add the lesson to additional consecutive weeks.
			Calendar calendar = Calendar.getInstance();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
			Timestamp sq = null;
			if (weeks>0) {
				for (int i=0;i<weeks;i++) {
					try {
						calendar.setTime((Date)formatter.parse(start_date));
					} catch (Exception e) {
						;
					}
			        	calendar.add(Calendar.DAY_OF_YEAR, 7);
			      	  	sq = new java.sql.Timestamp(calendar.getTime().getTime());
			        	start_date = sq.toString();
			        	preparedStatement.setString(1, start_date);
			            
			        	try {
						calendar.setTime((Date)formatter.parse(end_date));
					} catch (Exception e) {
						;
					}
			        	calendar.add(Calendar.DAY_OF_YEAR, 7);
			        	sq = new java.sql.Timestamp(calendar.getTime().getTime());
			        	end_date = sq.toString();
			        	preparedStatement.setString(2, end_date);
					preparedStatement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
	/**
	* Returns a lesson with the given ID from the SQL Table.
	* @param id The ID of the lesson.
	* @return the lesson object, containing all the lesson name, start date and end date.
	**/
	public Lesson selectLesson(int id) {
		// Declare lesson object.
		Lesson lesson = null;
		// Establish connection with the SQL Database.
		try (Connection connection = getConnection();
				// Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LESSON_BY_ID);) {
			preparedStatement.setInt(1, id);
			// Execute the query.
			ResultSet rs = preparedStatement.executeQuery();
			// Process the ResultSet object.
			while (rs.next()) {
				String name = rs.getString("text");
				String start_date = rs.getString("start_date");
				String end_date = rs.getString("end_date");
				lesson = new Lesson(id, name, start_date, end_date);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		// Return lesson object.
		return lesson;
	}	
	/**
	* Returns the list of expected students for a lesson with a given ID.
	* @param id The id of the lesson.
	* @return A string containing the list of IDs of expected students.
	**/
	public String getLessonStudentList(int id) {
		String userList = null;
		// Establish connection with the SQL Database.
		try (Connection connection = getConnection();
				// Create a statement using connection object.
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LESSON_USERLIST_BY_ID);) {
			preparedStatement.setInt(1, id);
			// Execute the query.
			ResultSet rs = preparedStatement.executeQuery();
			// Process the ResultSet object.
			while (rs.next()) {
				userList = rs.getString("users");
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return userList;
	}		
	/**
	* Returns a user with a given ID from the SQL Database.
	* @param id the ID of the user to return.
	* @return The user object.
	**/	
	public Student selectStudent(int id) {
		// Declare user object.
		Student user = null;
		// Establish connection to SQL Database.
		try (Connection connection = getConnection();
				// Create statement using connection object.
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENT_BY_ID);) {
			preparedStatement.setInt(1, id);
			// Execute the query.
			ResultSet rs = preparedStatement.executeQuery();
			// Process the ResultSet object.
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String number = rs.getString("number");
				user = new Student(id, name, email, number);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		// Return user object.
		return user;
	}
	/**
	* Reads the SQL Table and returns a list of all the users.
	* @return A list of users, sorted by ID.
	**/
	public List<Student> selectAllStudents() {
		// Declare empty list.
		List<Student> users = new ArrayList<>();
		// Establish connection to SQL Database.
		try (Connection connection = getConnection();
			// Create a statement using connection object.
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_STUDENTS);) {
			// Execute the query.
			ResultSet rs = preparedStatement.executeQuery();
			// Process the ResultSet object.
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String number = rs.getString("number");
				users.add(new Student(id, name, email, number));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		// Return populated list.
		return users;
	}
	/**
	* Deletes a user with a given id, from the SQL Table.
	* @param id The ID of the user to delete.
	* @return true if the operation was successful, or false otherwise.
	**/
	public boolean deleteStudent(int id) throws SQLException {
		boolean rowDeleted;
		// Establish connection to SQL Database.
		try (Connection connection = getConnection();
		     	// Create a statement using connection object.
				PreparedStatement statement = connection.prepareStatement(DELETE_STUDENTS_SQL);) {
			// Update statement parameters.
			statement.setInt(1, id);
			// Execute the query.
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	/**
	* Deletes a lesson with a given id, from the SQL Table.
	* @param id The id of the lesson to delete
	* @return true if the operation was successful, or false otherwise.
	**/
	public boolean deleteLesson(int id) throws SQLException {
		boolean rowDeleted;
		// Establish connection to SQL Database.
		try (Connection connection = getConnection();
		     	// Create a statement using the connection object.
				PreparedStatement statement = connection.prepareStatement(DELETE_LESSONS_SQL);) {
			// Update statement parameters.
			statement.setInt(1, id);
			// Execute the query.
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}	
	/**
	* Updates the details of an existing student in the SQL Table.
	* @param student The user object, containing the user start-date, end-date and name.
	* @return true if the operation was successful, or false otherwise.
	*/	
	public boolean updateStudent(Student student) throws SQLException {
		boolean rowUpdated;
		// Establish connection to SQL Database.
		try (Connection connection = getConnection();
		     	// Create a statement using the connection object.
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			// Update statement parameters.
			statement.setString(1, student.getName());
			statement.setString(2, student.getEmail());
			statement.setString(3, student.getNumber());
			statement.setInt(4, student.getId());
			// Execute the query.
			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

}
