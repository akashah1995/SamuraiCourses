<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	 <link rel="stylesheet" href="css/tooplate-style.css">
     <link rel="stylesheet" href="css/bootstrap.min.css">
     <link rel="stylesheet" href="css/font-awesome.min.css">
     <link rel="stylesheet" href="css/animate.css">
     <link rel="stylesheet" href="css/owl.carousel.css">
     <link rel="stylesheet" href="css/owl.theme.default.min.css">
	<style>
	
		body{
			background: url('https://pm1.narvii.com/6265/cf42b85e212b063e68f7a5da0099a3870e555438_hq.jpg') no-repeat;
	    		background-size: 110em 50em;
		}
	
	
	</style>
	
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<title>Samurai Courses Home Page</title>
</head>
<body>
    
    <section class="about">
          <div class="container">
               <div class="row">
                    <div class="col-md-6 col-md-offset-3">
                         <div class="about-info">
                         	<form id = "selectTime" method = "POST" action  = "/byTime">
                              <h2 class="wow fadeInUp animated" data-wow-delay="0.6s" style="visibility: visible; animation-delay: 0.6s; animation-name: fadeInUp;">Welcome to Samurai Courses, <c:out value="${currentUser.username}"></c:out></h2>
                              <div class="wow fadeInUp animated" data-wow-delay="0.8s" style="visibility: visible; animation-delay: 0.8s; animation-name: fadeInUp;">
                                   <p>Lets get right to it.</p>
                                   <p>Which semester are you looking to schedule?</p>
                              </div>
                              <figure class="profile wow fadeInUp animated" data-wow-delay="1s" style="visibility: visible; animation-delay: 1s; animation-name: fadeInUp;">
                                   <img src="images/author-image.jpg" class="img-responsive" alt="">
                                   <figcaption>
                                        <h3>Select a Time Frame: </h3>
                                        	
                                        		<p>
    											<select name="timespan" onchange="this.form.submit();">
    											<option>Available Options:</option>
    											<c:forEach var = "span" items = "${timespans}">
    												<option value = "<c:out value = "${span.getId()}"/>"><c:out value = "${span.getName()}"/></option>
    											</c:forEach>
    											</select>
   											</p>
    										
                                   </figcaption>
                              </figure>
                         <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                         </form>
                         	<div class="wow fadeInUp animated" data-wow-delay="0.8s" style="visibility: visible; animation-delay: 0.8s; animation-name: fadeInUp;">
                         	<form id="logoutForm" method="POST" action="/logout">
	        					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	        					<input type="submit" value="Logout" class = "logout"/>
    							</form> 
    							</div>
                         </div>
                      
                    </div>
                    
               </div>
              
          </div>
	</section>
    
</body>
</html>

