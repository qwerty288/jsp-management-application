package net.management.model;
import java.util.*;

public class Student {
	protected int id;
	protected String name;
	protected String email;
	protected String number;
	protected String checked;

	public Student() {
	}

	public Student(String name, String email, String number) {
		super();
		this.name = name;
		this.email = email;
		this.number = number;
		this.checked = null;
	}

	public Student(int id, String name, String email, String number) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.number = number;
		this.checked = null;
	}

	public void setChecked() {
		this.checked = "checked";
	}
	public String getChecked() {
		return this.checked;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public void addLesson(String lessonName) {
		this.lessons.add(lessonName);
	}

	public void removeLesson(String lessonName) {
		this.lessons.remove(lessonName);
	}

}
