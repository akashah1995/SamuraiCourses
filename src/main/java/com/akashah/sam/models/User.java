package com.akashah.sam.models;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {
 
 @Id
 @GeneratedValue
 private Long id;

@Size(min=5, message="Email must be valid")
 private String username;
 public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}

@Size(min=5, message="Password must be greater than 5 characters")
private String password;
@Transient
private String passwordConfirmation;
@Column(updatable=false)
private Date createdAt;
private Date updatedAt;

private String firstName;
 
 
 public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}

@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
 private List<Schedule> schedules;
 
 public List<Schedule> getSchedules() {
	return schedules;
}
public void setSchedules(List<Schedule> schedules) {
	this.schedules = schedules;
}
public Date getCreatedAt() {
	return createdAt;
}
@ManyToMany(fetch = FetchType.EAGER)
 @JoinTable(
     name = "users_roles", 
     joinColumns = @JoinColumn(name = "user_id"), 
     inverseJoinColumns = @JoinColumn(name = "role_id"))
private List<Role> roles;

 
 public User() {
	 
 }
 
 public Long getId() {
     return id;
 }
 public void setId(Long id) {
     this.id = id;
 }
 
 public String getPassword() {
     return password;
 }
 public void setPassword(String password) {
     this.password = password;
 }
 public String getPasswordConfirmation() {
     return passwordConfirmation;
 }
 public void setPasswordConfirmation(String passwordConfirmation) {
     this.passwordConfirmation = passwordConfirmation;
 }
 public Date getCreatedAtcopy() {
     return createdAt;
 }
 public void setCreatedAt(Date createdAt) {
     this.createdAt = createdAt;
 }
 public Date getUpdatedAt() {
     return updatedAt;
 }
 public void setUpdatedAt(Date updatedAt) {
     this.updatedAt = updatedAt;
 }
 public List<Role> getRoles() {
     return roles;
 }
 public void setRoles(List<Role> roles) {
     this.roles = roles;
 }
 
 @PrePersist
 protected void onCreate(){
     this.createdAt = new Date();
 }
 @PreUpdate
 protected void onUpdate(){
     this.updatedAt = new Date();
 }
 
}
