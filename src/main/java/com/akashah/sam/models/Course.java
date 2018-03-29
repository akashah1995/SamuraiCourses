package com.akashah.sam.models;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="courses")
public class Course {
 
 @Id
 @GeneratedValue
 private Long id;
 private String courseName;
 @DateTimeFormat(pattern = "HH:mm:ss")
 private Date startTime;
 @DateTimeFormat(pattern = "HH:mm:ss")
 private Date endTime;
 
 private int timesEnrolled;
 private String CRN;
 private String description;
 
 
 public String getCRN() {
	return CRN;
}

public void setCRN(String cRN) {
	CRN = cRN;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public int getTimesEnrolled() {
	return timesEnrolled;
}

public void setTimesEnrolled(int timesEnrolled) {
	this.timesEnrolled = timesEnrolled;
}

public void setStartTime(Date startTime) {
	this.startTime = startTime;
}

public void setEndTime(Date endTime) {
	this.endTime = endTime;
}


private int units;
 public int getUnits() {
	return units;
}

public void setUnits(int units) {
	this.units = units;
}


 private String instructor;
 public String getInstructor() {
	return instructor;
}

public void setInstructor(String instructor) {
	this.instructor = instructor;
}

@ManyToMany(fetch = FetchType.LAZY)
 @JoinTable(
	     name = "courses_days", 
	     joinColumns = @JoinColumn(name = "course_id"), 
	     inverseJoinColumns = @JoinColumn(name = "day_id"))
 private List<Day> days;
 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "timespan_id")
 private Timespan timespan;
 
 @ManyToMany(mappedBy = "courses")
 private List<Schedule> schedules;
 
private int capacity;

public int getCapacity() {
	return capacity;
}

public void setCapacity(int capacity) {
	this.capacity = capacity;
}

public List<Schedule> getSchedules() {
	return schedules;
}

public void setSchedules(List<Schedule> schedules) {
	this.schedules = schedules;
}

public Date getStartTime() {
	return startTime;
}


public Date getEndTime() {
	return endTime;
}

public String formatTime(Date input) {
	DateFormat df = new SimpleDateFormat("hh:mm a");
	String time = df.format(input);
	return time;
}


public void setStartTime(Time startTime) {
	this.startTime = startTime;
}

public void setEndTime(Time endTime) {
	this.endTime = endTime;
}

public List<Day> getDays() {
	return days;
}

public void setDays(List<Day> days) {
	this.days = days;
}

public void setCourseName(String courseName) {
	this.courseName = courseName;
}

public Course() {
 }
 
 public Long getId() {
     return id;
 }
 public void setId(Long id) {
     this.id = id;
 }
 public String getCourseName() {
     return courseName;
 }
 public void setName(String name) {
     this.courseName = name;
 }

public Timespan getTimespan() {
	return timespan;
}

public void setTimespan(Timespan timespan) {
	this.timespan = timespan;
}
 
}
