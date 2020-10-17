package net.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.usermanagement.model.Lesson;
import net.usermanagement.model.User;

public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost/demo?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";

	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, number) VALUES "
			+ " (?, ?, ?);";
	private static final String INSERT_LESSONS_SQL = "INSERT INTO events" + "  (start_date, end_date, text, users) VALUES "
			+ " (?, ?, ?, ?);";
	private static final String SELECT_USER_BY_ID = "select id,name,email,number from users where id =?";
	private static final String SELECT_LESSON_BY_ID = "select id,start_date,end_date,text from events where id =?";
	private static final String SELECT_LESSON_USERLIST_BY_ID = "select users from events where id =?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String SELECT_ALL_LESSONS = "select * from events";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
	private static final String DELETE_LESSONS_SQL = "delete from events where id = ?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, number =? where id = ?;";
	private static final String UPDATE_LESSONS_SQL = "update events set text =?, users =? where id = ?;";
	public UserDAO() {
	}
	
	public List<Lesson> selectAllLessons() {
		//System.out.println("Listing lessons...");

		// using try-with-resources to avoid closing resources (boiler plate code)
		List<Lesson> lessons = new ArrayList<>();
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();

				// Step 2:Create a statement using connection object
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_LESSONS);) {
			//System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("text");
				//System.out.println("Adding lesson...");
				//System.out.println(name);
				//System.out.println(rs.getTimestamp("start_date").toString());
				String start_date = rs.getTimestamp("start_date").toString();
				String end_date = rs.getTimestamp("end_date").toString();
				String userList = rs.getString("users");
				//Process userLists
				
				lessons.add(new Lesson(id, name, start_date, end_date));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return lessons;
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

	public void insertUser(User user) throws SQLException {
		// try-with-resource statement will auto close the connection.
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getNumber());
			//System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
//Need to add to student, then add search function
	public boolean updateLesson(Lesson lesson, String userList) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_LESSONS_SQL);) {
			statement.setString(1, lesson.getName());
			statement.setString(2, userList);
			statement.setInt(3, lesson.getId());

			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}	
	
	public void insertLesson(Lesson lesson, String[] weeks, String userList) throws SQLException {
		// try-with-resource statement will auto close the connection.
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LESSONS_SQL)) {
			String start_date = lesson.getstart_date();
			System.out.println(start_date);
			String end_date = lesson.getend_date();
			String name = lesson.getName();
			preparedStatement.setString(1, start_date);
			preparedStatement.setString(2, end_date);
			preparedStatement.setString(3, name);
			preparedStatement.setString(4, userList);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public Lesson selectLesson(int id) {
		Lesson lesson = null;
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LESSON_BY_ID);) {
			preparedStatement.setInt(1, id);
			//System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				String name = rs.getString("text");
				String start_date = rs.getString("start_date");
				String end_date = rs.getString("end_date");
				lesson = new Lesson(id, name, start_date, end_date);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return lesson;
	}	


	public String getLessonUserList(int id) {
		String userList = null;
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LESSON_USERLIST_BY_ID);) {
			preparedStatement.setInt(1, id);
			//System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();
			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				userList = rs.getString("users");
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return userList;
	}		
	
	public User selectUser(int id) {
		User user = null;
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
			preparedStatement.setInt(1, id);
			//System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String number = rs.getString("number");
				user = new User(id, name, email, number);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return user;
	}

	public List<User> selectAllUsers() {

		// using try-with-resources to avoid closing resources (boiler plate code)
		List<User> users = new ArrayList<>();
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();

				// Step 2:Create a statement using connection object
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
			//System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String number = rs.getString("number");
				users.add(new User(id, name, email, number));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return users;
	}

	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean deleteLesson(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_LESSONS_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}	
	
	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getNumber());
			statement.setInt(4, user.getId());

			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
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
