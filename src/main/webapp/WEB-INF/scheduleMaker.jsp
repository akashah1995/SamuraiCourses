<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style>
	body{
		background: linear-gradient(
	      rgba(0, 0, 0, 0.3), 
	      rgba(0, 0, 0, 0.3)
	    ),
		url('http://img06.deviantart.net/7ccb/i/2013/106/2/2/japan_village_by_b_zuleta-d4vzl9x.jpg') no-repeat;
	}
	.container{
		margin-top:10em;
	}
	
	#vessle{
		opacity: .8;
		background-color:white;
		border: 1px solid black;
		-webkit-animation: fadein 5s; /* Safari, Chrome and Opera > 12.1 */
       	-moz-animation: fadein 5s; /* Firefox < 16 */
        	-ms-animation: fadein 5s; /* Internet Explorer */
         -o-animation: fadein 5s; /* Opera < 12.1 */
         animation: fadein 5s;
	}
	@-webkit-keyframes fadein {
    			0% { opacity: 0; }
    			66% { opacity: 0; }
    			100%  { opacity: .8; }
		}
		
		@keyframes fadein {
    			from { opacity: 0; }
    			to   { opacity: .8; }
		}

		/* Firefox < 16 */
		@-moz-keyframes fadein {
    			from { opacity: 0; }
    			to   { opacity: .8; }
		}
		/* Internet Explorer */
		@-ms-keyframes fadein {
    			from { opacity: 0; }
    			to   { opacity: .8; }
		}

		/* Opera < 12.1 */
		@-o-keyframes fadein {
    			from { opacity: 0; }
    			to   { opacity: .8; }
		}
	
	#create{
		margin-top:1em;
	}
	
	#link{
		margin-top: 3em;
		margin-left: .2em;
	}


</style>


<title>Schedule Creator</title>
</head>
<body>
    
    <div class = "container">
    <div class = "row">
    <div class = "col-md-2 col-md-offset-5" id = "vessle">
	    <h2>Schedule Details</h2>
	    <p><c:out value = "${errors}"/></p>
	    <form method="POST" action="/createSchedule">
	    		<p>
	    		 Schedule Name: <input type = "text" name = "name" id = "name">
	    		<p>
	    		<p>Time Span:</p>
	    		<p>
	    		<select name="timespan">
	    		<c:forEach var = "span" items = "${timespans}">
	    			<option value = "<c:out value = "${span.getId()}"/>"><c:out value = "${span.getName()}"/></option>
	    		</c:forEach>
	    		</select>
	    		</p>
	    		<p> Unit Cap: </p>
	    		<p>
	    		<select name="unitCap">
	    		<c:forEach var = "num" begin = "10" end = "28" >
	    			<option value = "${num}">${num}</option>
	    		</c:forEach>
	    		</select>
	    		</p>
	        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	        <input type="submit" value="Create" id = "create"/>
	        <p id = "link"> <a href = "/schedules/${returnId}"> Back </a></p>
    </form>
    </div>
    </div>
    </div>
</body>
</html>