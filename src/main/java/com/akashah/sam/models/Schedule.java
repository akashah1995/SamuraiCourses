package com.akashah.sam.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="schedules")
public class Schedule {
	
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
	private User user;
	private int unitCap;
	private int courseUnits;

	public int getCourseUnits() {
		return courseUnits;
	}

	public void setCourseUnits(int courseUnits) {
		this.courseUnits = courseUnits;
	}

	public int getUnitCap() {
		return unitCap;
	}

	public void setUnitCap(int unitCap) {
		this.unitCap = unitCap;
	}
	private int priority;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="timespan_id")
	private Timespan timespan;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		     name = "schedules_courses", 
		     joinColumns = @JoinColumn(name = "schedule_id"), 
		     inverseJoinColumns = @JoinColumn(name = "course_id"))
	private List<Course> courses;
	
	public Schedule() {
		
	}

	public Timespan getTimespan() {
		return timespan;
	}

	public void setTimespan(Timespan timespan) {
		this.timespan = timespan;
	}

	public Schedule(String name, User user, Semester semester) {
		this.user = user;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

}
