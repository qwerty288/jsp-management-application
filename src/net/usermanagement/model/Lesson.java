package net.usermanagement.model;
import java.util.*;

public class Lesson {
	protected int id;
	protected String name;
	protected String start_date, end_date, weeks;
	protected String[] userList;

	public Lesson() {
	}

	public Lesson(String name, String start_date, String end_date) {
		super();
		this.name = name;
		this.start_date = start_date;
		this.end_date = end_date;
	}

	public Lesson(int id, String name, String start_date, String end_date) {
		super();
		this.id = id;
		this.name = name;
		this.start_date = start_date;
		this.end_date = end_date;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getstart_date() {
		return start_date;
	}
	public void setstart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getend_date() {
		return end_date;
	}
	public void setend_date(String end_date) {
		this.end_date = end_date;
	}
	public String getweeks() {
		return weeks;
	}
	public void setweeks(String weeks) {
		this.weeks = weeks;
	}

}
