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
	<title> Existing Schedules for <c:out value="${currTimespan.getName()}"></c:out></title>
	<style>
	
		body{
			background: url('https://pm1.narvii.com/6265/cf42b85e212b063e68f7a5da0099a3870e555438_hq.jpg') no-repeat;
	    		background-size: 110em 50em;
		}
		.about{
			margin-top: 4em;
		}
		.container{
			-webkit-animation: fadein 5s; /* Safari, Chrome and Opera > 12.1 */
       		-moz-animation: fadein 5s; /* Firefox < 16 */
        		-ms-animation: fadein 5s; /* Internet Explorer */
         	-o-animation: fadein 5s; /* Opera < 12.1 */
             animation: fadein 5s;
		}
		
		@-webkit-keyframes fadein {
    			0% { opacity: 0; }
    			66% { opacity: 0; }
    			100%  { opacity: 1; }
		}
		
		@keyframes fadein {
    			from { opacity: 0; }
    			to   { opacity: 1; }
		}

		/* Firefox < 16 */
		@-moz-keyframes fadein {
    			from { opacity: 0; }
    			to   { opacity: 1; }
		}
		/* Internet Explorer */
		@-ms-keyframes fadein {
    			from { opacity: 0; }
    			to   { opacity: 1; }
		}

		/* Opera < 12.1 */
		@-o-keyframes fadein {
    			from { opacity: 0; }
    			to   { opacity: 1; }
		}
		
		#link{
			font-size: 1.2em;
		}
		.logout{
			margin-top: 4em;
			border-radius: .5em;
			border: 1px solid black;
			opacity: .5;
		}
		
		#link2{
			margin-top: 2em;
			margin-left: .5em;
		}
	</style>
</head>
<body>		<div class = "about">
    			  <div class = "container">
               <div class="row">
                    <div class="col-md-6 col-md-offset-3">
                         <div class="about-info">
                              <form id = "selectSchedule" method = "POST" action  = "/displaySchedule">
                              <h2 class="wow fadeInUp animated"  style="visibility: visible; animation-delay: 0.6s; animation-name: fadeInUp;"> <c:out value="${currTimespan.getName()}"></c:out> Schedules</h2>
                              <div class="wow fadeInUp animated"  style="visibility: visible; animation-delay: 0.8s; animation-name: fadeInUp;">
                                   <p>You can modify one of your previously created schedules </p>
                                   <p>
 										<select name="schedule" onchange="this.form.submit();">
 											<option disabled selected value>Seach:</option>
 											<c:forEach var = "schedule" items = "${timeSchedules}">
 												<option value = "<c:out value = "${schedule.getId()}"/>"><c:out value = "${schedule.getName()}"/></option>
 											</c:forEach>
 										</select>
    								  </p>
    								  <br>
    								  <br>
    								  <br>
    								  <br>
    								  <p> Or you can .. <a id = "link" href = "/createSchedule/${timespanId}">Create a New Schedule </a></p>
    								   
                             </div>
                         <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                         </form>
                         	<form id="logoutForm" method="POST" action="/logout">
	        					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	        					<input type="submit" value="Logout" class = "logout"/>
    						</form> 
    						<p id = "link2"><a href = "/"> Back</a></p>
                         </div>
                    </div>
               </div>
         	</div>
         	</div>
    
</body>
</html>

