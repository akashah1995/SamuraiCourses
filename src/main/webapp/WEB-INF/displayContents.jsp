<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style>

	body{
		background-color:#white;
	}

	.navbar-header{
		font-family: URW Chancery L;
		color: red;
	}

	#navy{
		margin-left:1em;
	}

	h1{
		text-align: center;
		background-color: #47476b;
		color:white;
	}

	#top{

		text-align:center;
		border: 1px solid #666699;
	}
	
	#break{
		margin-top:5em;
		height: 2em;
		background-color:#1a1a1a;
	}
	
	#bottom-middle{
		margin-top: 5em;
		background-color: white;
	
	}
	
	#bottom{
		margin-top: 1em;
		height: 20em;
		overflow-y: scroll;
		margin-left: .75em;
	}
	.schedule{
		display: inline-block;
		margin: 0 auto;
		background-color:white;
	}
	
	.courses th{
		width: 12em;
		padding-left:1em;
	}
	
	.courses td{
		margin-right: 5em;
		width: 12em;
	}
	
	.courses td{
		font-size: 12px; 
		
	}
	
	.center{
		text-align:center;
		padding-right: 1.5em;
	}
	
	#headRow th{
		padding-bottom: 1em;
	}
	
	
	.firstrow{
		background-color: #666699;
		color: white;
		padding: em;
	}
	
	.bodyRows, #courses.td { transition: .1s ease-in; }
	
	.bodyRows:hover {background:#b3ecff ; pointer-events: visible;}
	
	.bodyRows:hover {transform: scale(1.05); font-weight: 600; box-shadow: 0px 3px 7px rgba(0, 0, 0, 0.5);}
	
	.bodyRows{
		background-color: #f0f0f5;
	}
	
	#days{
		margin: 0 auto;
	}
	.dayNames{
		width: 10.5em;
		height: 5em;
		text-align:center;
	}
	.headData{
		font-weight: bold;
		padding-bottom: .5em;
		border-bottom: 1px solid red;
	}
	.data{
		width: 10em;
		height: 10em;
		text-align:center;
	}
	
	.schedule:hover {background: #ffe6e6; pointer-events: visible;}
	
	.schedule:hover { transform: scale(1.0); font-weight: 700;}
	
	.thin{
		height: 80px;
	}
	
	#error{
		color: red;
	}
	
	#success{
		color: green;
	}
	
	.column{
		 border-left: 1px solid #000;
    		 border-right: 1px solid #000;
	}
	
	.smaller{
		font-size: 10px;
	}
	
	.medium{
		font-size: 12px;
	}
	
	#space{
		height: 20em;
	}
	
	.logout{
		margin-left: 20em;
		margin-top: 5em;
	}
	
	.logoutUp{
		margin-top: 5em;
		margin-left: 8.5em;
	}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> ${schedule.getName()} </title>
</head>
<body>
	<div class = "row">
	<div class = "col-md-12">
	<nav class="navbar navbar-inverse">
  		<div class="container-fluid">
  			<div id = "navy" class="navbar-header">
      			<a class="navbar-brand" href="#">${schedule.getName()}</a>
    			</div>
    			<ul class="nav navbar-nav">
		      <li><a href="/">Home</a></li>
		      <li><a href="/createSchedule/${timespan.getId()}">New Schedule</a></li>
		      <li class="dropdown">
		        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Alternative Schedules
		        <span class="caret"></span></a>
		        <ul class="dropdown-menu">
		       	<c:forEach var = "schedule" items = "${timeSchedules}">
		          <li><a href="/displaySchedule/${schedule.getId()}">${schedule.getName()}</a></li>
		        </c:forEach>
		        </ul>
		      </li>
		      <li><a href = "/deleteSchedule/${timespan.getId()}/${schedule.getId()}">Delete</a></li>
		    </ul>
  		</div>
	</nav>
	</div>
	</div>
	<div class = "row">
		<div class = "col-md-12">
			<p id = "error"><c:out value = "${errors}"/></p>
			<p id = "success"><c:out value = "${success}"/></p>
		</div>
	</div>
	<div class = "row">
	<div class = "col-md-10 col-md-offset-1" id = "top">
		<c:set var="index" value= "${0}"/>
		<c:forEach items = "${dayCourses}" var = "listCourse" varStatus = "loop">
			<table class = "schedule">
			<tr class = "headRow">
				<td class = "headData"> ${days[index]} </td>
			</tr>
			<c:set var="index" value= "${index + 1}"/>
			<c:forEach items = "${listCourse}"  var = "course" varStatus = "loop">
				<tr class = "thin">
				<td class = "data">
				<c:if test = "${course.getCourseName() != null}" > 
				<p> 
					<span class = "medium">${course.getCourseName()}</span><br>
					<span class = "smaller">${course.formatTime(course.getStartTime())} - ${course.formatTime(course.getEndTime())}</span>
					<span class = "smaller"><br> <a href = "/dropCourse/${schedule.getId()}/${course.getId()}"> DROP</a> </span>
				</p>
				</c:if>
				<c:if test = "${course.getCourseName() == null}" > 
				<p></p>
				</c:if>
				</td>
				</tr>
			</c:forEach>
			</table>
		</c:forEach>
		</div>
		<form id="logoutForm" method="POST" action="/logout">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
   			<input type="submit" value="Logout" class = "logoutUp"/>
		</form> 
	</div>
	<div class = "row" id = "break">
		<div class = "col-md-12" id = "breakDiv">

		</div>
	</div>
	<div class = "row">
		<div class = "col-md-8 col-md-offset-2" id = "bottom-middle">
		<table class = "courses">
			<thead>
				<tr id = "headRow">
					<th> Course Name: </th>
					<th> Course Instructor: </th>
					<th> Course Vacancy: </th>
					<th> Course Time: </th>
					<th> Course Days: </th>
					<th> Course CRN: </th>
					<th> Available Actions: </th>
				</tr>
			</thead>
		</table>
		</div>
		</div>
		<div class = "row" id = "bottom">
		<div class = "col-md-8 col-md-offset-2">
		<table class = "courses">
		<tbody>
		<c:forEach items = "${courses}" var = "course" varStatus = "loop">
	    			<tr class = "bodyRows">
	    			<td><a href= "/courseDetails/${course.getId()}/${schedule.getId()}">${course.getCourseName()}</a></td>
	    			<td>${course.getInstructor()}</td>
	    			<td class = "center">
	    				<c:if test = "${course.getTimesEnrolled() == course.getCapacity()}" >
		    				<p>FULL</p>
		    			</c:if>
		    			<c:if test = "${course.getTimesEnrolled() != course.getCapacity()}" >
		    				<p>${course.getTimesEnrolled()} / ${course.getCapacity()} </p>
		    			</c:if>
	    			</td>
	    			<td>${course.formatTime(course.getStartTime())} - ${course.formatTime(course.getEndTime())} </td>
	    			
	    			<td>
	    				<p>
		    				<c:forEach items = "${course.getDays()}" var = "day" varStatus = "loop">
		    					<c:out value = "${day.getName()}"/>
		    				</c:forEach>
	    				</p>
	    			</td>
	    			
	    			<td class = "center">${course.getCRN()}</td>
	    			<td class = "center">
	    				<c:if test = "${scheduleCourses.contains(course)}" >
	    					<a href = "/dropCourse/${schedule.getId()}/${course.getId()}"> DROP</a>
	    				</c:if>
	    				<c:if test = "${!scheduleCourses.contains(course)}" >
	    					<a href = "/addCourse/${schedule.getId()}/${course.getId()}">ADD</a>
	    				</c:if>
	    			</td>
	    			</tr>
	    	</c:forEach>
	    	</tbody>
	    	</table>
	    	</div>
    	</div>
    	<div class = "row" id = "space">
    	<form id="logoutForm" method="POST" action="/logout">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
   			<input type="submit" value="Logout" class = "logout"/>
	</form> 
    	</div>
</body>

</html>



