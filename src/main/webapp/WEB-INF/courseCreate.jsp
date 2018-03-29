<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome Page</title>
</head>
<body>
    <h1>Welcome to Course Creator</h1>
    <p><c:out value = "${errors}"/></p>
    <form method="POST" action="/createCourse">
    		<p>
    		 Name: <input type = "text" name = "name" id = "name">
    		</p>
    		<p>
    		Instructor: <input type = "text" name = "instructor" id = "instructor">
    		</p>
    		<p>
    		Description: <input type = "text" name = "description" id = "description" value = "None">
    		</p>
    		<p>
    		CRN: <input type = "text" name = "crn" id = "crn" value = "None">
    		</p>
    		<p>
    		 Start Time: <input type="time" name="start">
    		</p>
    		<p>
    		 End Time: <input type="time" name="end">
    		</p>
    		<p>
    		Capacity: <input type="number" name="capacity" id = "capacity">
    		</p>
    		<p>
    		Units: <input type="number" name="units" id = "units">
    		</p>
    		<p> Course days: </p>
    		<c:forEach var = "day" items = "${days}">
    		<input type="checkbox" name="day" value= "${day.getId()}"><c:out value = "${day.getName()}"/>
  		</c:forEach> <br>
  		<input type="checkbox" name = "day" value="" style="display:none" checked="checked" />
    		<p> Course Time-frame: </p>
    		<p>
    		<select name="timespan">
    		<c:forEach var = "span" items = "${timespans}">
    			<option value = "<c:out value = "${span.getId()}"/>"><c:out value = "${span.getName()}"/></option>
    		</c:forEach>
    		</select>
    		</p>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="submit" value="Create" />
    </form>
</body>
</html>