package com.akashah.sam.controllers;

import java.security.Principal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akashah.sam.models.User;
import com.akashah.sam.models.Course;
import com.akashah.sam.models.Day;
import com.akashah.sam.models.Schedule;
import com.akashah.sam.models.Semester;
import com.akashah.sam.models.Timespan;
import com.akashah.sam.services.CourseService;
import com.akashah.sam.services.UserService;
import com.akashah.sam.validator.UserValidator;

@Controller
public class Users {
    
	private UserService userService;
	private CourseService courseService;
	private UserValidator userValidator;
    
    public Users(UserService userService, CourseService courseService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }
    
    
    // Displays page for registering a User.
    @RequestMapping("/registration")
    public String registerForm(@Valid @ModelAttribute("user") User user) {
        return "registrationPage.jsp";
    }
    
    // The actual registering process
    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
    	userValidator.validate(user, result); //makes sure there are no errors in the potential user.
        if (result.hasErrors()) {
            return "registrationPage.jsp";
        }
        userService.saveWithUserRole(user); // saves user in database.
        return "redirect:/login";
    }
    
    //Displays the page for a user to login.
    @RequestMapping("/login")
    public String login(@RequestParam(value="error", required=false) String error, @RequestParam(value="logout", required=false) String logout, Model model, @Valid @ModelAttribute("user") User user) {
        if(error != null) { //Makes sure there isn't an error in the login.
            model.addAttribute("errorMessage", "Invalid Credentials, Please try again.");
        }
        if(logout != null) {
            model.addAttribute("logoutMessage", "Logout Successful!");
        }
        return "loginPage.jsp";
    }
    
    // The home route displaying the home page for Samurai Courses.
    @RequestMapping(value = {"/", "/home"})
    public String home(Principal principal, Model model) { 
    		List <Schedule> schedules = userService.allSchedules(); 
    		model.addAttribute("schedules", schedules);
    		model.addAttribute("check", false);
        String username = principal.getName();
        model.addAttribute("currentUser", userService.findByUsername(username));
        List<Timespan> timespans = userService.allTimespans();
        System.out.println(timespans.size());
        model.addAttribute("timespans", timespans);
        return "homePage.jsp";
    }
    // This route has "admin" because only an admin can add a new semester or quarter.
    @RequestMapping(value = "/admin/timespan" )
    public String Semester(Model model, @Valid @ModelAttribute("timespan") Timespan timespan) {
    		return "timespan.jsp";
    }
    //This is the post route where a new timespan gets added to the database after validation.
    @PostMapping(value = "/timespan")
    public String createSemester(@Valid @ModelAttribute("timespan") Timespan timespan, BindingResult result, Model model, RedirectAttributes redirAttrs) {
    		if (result.hasErrors()){
    			redirAttrs.addFlashAttribute("errors","There are errors in your semester, dates must be Valid!");
    			return "timespan.jsp";
    		}else {
    			userService.saveTimespan(timespan);
    			return "redirect:/admin/timespan";
    		}
    }
    //Only admins can create courses. This is the page for creating a course. 
    @RequestMapping(value = "/admin/createCourse")
    public String courseCreator(Model model) {
    		List<Day> days = userService.allDays();
    		List<Timespan> timespans = userService.allTimespans();
    		
    		model.addAttribute("days", days);
    		model.addAttribute("timespans", timespans);
    		
    		return "courseCreate.jsp";
    }
    // This is the post route... adds a new course to the database after validation.
    @PostMapping(value = "/createCourse")
    public String createCourse(RedirectAttributes redirAttrs, Model model, @RequestParam("description") String description, @RequestParam("crn") String crn, @RequestParam("name") String name, @RequestParam("capacity") Integer capacity, @RequestParam("day") String[] dayStringId, @RequestParam("timespan") String spanStringId, @RequestParam("instructor") String instructor, @RequestParam("start") String start, @RequestParam("end") String end, @RequestParam("units") Integer units) {
    		boolean isError = false;
    		long msStart = 0;
    		long msEnd = 0;
    		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    		StringBuilder errors = new StringBuilder("");
    		
    		try {
    			msStart = sdf.parse(start).getTime(); // used milliseconds to compare times.
    			msEnd = sdf.parse(end).getTime(); 
 
		} catch (ParseException e) {
			isError = true;
			errors.append("Fill out time field correctly");
		}
    		if (msStart >= msEnd) { 
    			isError = true;
    			errors.append("Start time cannot be after end time! ");
    		}
    		if (dayStringId.length == 1 || name.isEmpty() || capacity.equals(null) || capacity < 0 || units < 1 || units > 6){ 
    			isError = true;
    			errors.append(" All field must be filled in correctly for a course to be valid!");
    		}
    		if(isError) {
    			redirAttrs.addFlashAttribute("errors", errors);
    			return "redirect:/admin/createCourse";
    		}
    		
    		Date startTime = new Time(msStart);
    		Date endTime = new Time(msEnd);
    		
    		List<Day> courseDays = userService.createListDays(dayStringId); //get the days
//    		System.out.println(courseDays.size());
    		
    		Timespan courseSpan = userService.getTimespan((long) Integer.parseInt(spanStringId));
    		
    		Course newCourse = new Course();
    		// Because I did not use form binding (due to a few of the courses' instance variables being custom objects) I had to bind everything here.
    		newCourse.setName(name);
    		newCourse.setCapacity(capacity);
    		newCourse.setDays(courseDays);
    		newCourse.setTimespan(courseSpan);
    		newCourse.setInstructor(instructor);
    		newCourse.setStartTime(startTime);
    		newCourse.setEndTime(endTime);
    		newCourse.setUnits(units);
    		newCourse.setDescription(description);
    		newCourse.setCRN(crn);
    		userService.saveCourse(newCourse);

    		return "redirect:/";
    }
    // Displays the page for creating a schedule
    @RequestMapping(value = "/createSchedule/{id}")
    public String scheduleCreator(Model model, @PathVariable("id") String returnId) { 
    		model.addAttribute("returnId", returnId);
    		List<Timespan> timespans = userService.allTimespans();
    		model.addAttribute("timespans", timespans);
    		
    		return "scheduleMaker.jsp";
    }
    // The post route for error checking a schedule and adding it to the database.
    @PostMapping(value = "/createSchedule")
    public String createSchedule(Principal principal, RedirectAttributes redirAttrs, Model model, @RequestParam("name") String name, @RequestParam("timespan") String spanStringId, @RequestParam("unitCap") String units) {
    		boolean isErrors = false;
    		StringBuilder errors = new StringBuilder("");
    		if (name.length() < 3) {
			errors.append("Name is too short, must be 3 characters");
			isErrors = true;
		}
//    		if (priority < 0) { // We are not currently taking priority into consideration at all. 
//    			errors.append(" Priority must be positive");
//    			isErrors = true;
//    		}
    		if (isErrors) {
    			redirAttrs.addFlashAttribute("errors", errors);
    			return "redirect:/createSchedule";
    		}
		Timespan scheduleSpan = userService.getTimespan((long) Integer.parseInt(spanStringId));
		Schedule newSchedule = new Schedule();
		newSchedule.setName(name);
		newSchedule.setUnitCap(Integer.parseInt(units));
//		newSchedule.setPriority(priority);
		newSchedule.setTimespan(scheduleSpan);
		String username = principal.getName();
        User currUser = userService.findByUsername(username);
        newSchedule.setUser(currUser);
        userService.saveSchedule(newSchedule);
        newSchedule = userService.getNewestSchedule();
		return "redirect:/displaySchedule/" + newSchedule.getId();
		
    }
    // Reroutes the user to a page where he can view his created schedules for a specific time period.
    @PostMapping(value = "/byTime")
    public String redirectByTime(Model model, @RequestParam("timespan") String spanStringId) {
    		Integer spanId = Integer.parseInt(spanStringId);
    		return "redirect:/schedules/" + spanId;
    }
    // The actual page displaying the schedules for a specific time period. 
    @RequestMapping(value = "/schedules/{id}")
    public String presentSchedulesForTime(Principal principal, Model model, @PathVariable("id") String idSpan) {
    		String username = principal.getName();
    		User currUser = userService.findByUsername(username);
    		Timespan currTimespan = userService.getTimespan(Integer.parseInt(idSpan));
    		List<Schedule> timeSchedules = userService.getSchedulesByTime(currUser, Integer.parseInt(idSpan));
//    		userService.sortSchedulesByPriority(timeSchedules); // not taking priority into consideration anymore
    		model.addAttribute("timespanId", currTimespan.getId());
    		model.addAttribute("timeSchedules", timeSchedules);
    		model.addAttribute("currTimespan", currTimespan);
    		return "timeSchedules.jsp";
    }
    //Redirects to the route/ handler that displays the actual schedule contents.
    @PostMapping("/displaySchedule")
    public String displayScheduleRedirect(Model model, @RequestParam("schedule") String scheduleId) {
    		
    		return "redirect:/displaySchedule/"+ scheduleId;
    }
    // This page displays the schedule contents and dynamically allows you to modify the schedule by adding or removing courses
    // from the list of available courses presented.
    @RequestMapping("/displaySchedule/{id}")
    public String displayScheduleContent(Model model, @PathVariable("id") String scheduleId, Principal principal) {
    	
    	User user = userService.findByUsername(principal.getName());
    	Schedule currSchedule = userService.getScheduleById((long) Integer.parseInt(scheduleId));
    		model.addAttribute("schedule", currSchedule);
    		Timespan currTimespan = currSchedule.getTimespan();
    		model.addAttribute("timespan", currTimespan);
    		List<Course> timespanCourses = userService.getCoursesByTimespan(currTimespan);
    		userService.sortCoursesByTime(timespanCourses);
    		model.addAttribute("courses", timespanCourses);
    		List<Course> scheduleCourses = currSchedule.getCourses();
    		model.addAttribute("scheduleCourses", scheduleCourses);
    		userService.sortCoursesByTime(scheduleCourses);
    		List<List<Course>> coursesByDay = userService.sortCoursesByDayNew(scheduleCourses);
    		System.out.println(coursesByDay.get(1).size() + " " + coursesByDay.get(2).size() + " " + coursesByDay.get(3).size() + " " + coursesByDay.get(4).size());
    		model.addAttribute("dayCourses", coursesByDay);
    		
    		List<Schedule> timeSchedules = userService.getSchedulesByTime(user, currTimespan.getId().intValue());
    		model.addAttribute("timeSchedules", timeSchedules);
    		
    		String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    		model.addAttribute("days", days);
    		return "displayContents.jsp";
    		
    }
    // When a user clicks *add course* on a course, this is what checks whether the course can actually be added.
    // Checks time conflicts, capacity, units, etc.
    @RequestMapping("/addCourse/{id1}/{id2}")
    public String addCourse(RedirectAttributes redirAttrs, Principal principal, Model model, @PathVariable("id1") String sId, @PathVariable("id2") String cId) {
    		StringBuilder errors = new StringBuilder("");
    		StringBuilder success = new StringBuilder("");
    		Course currCourse = userService.getCourse(Integer.parseInt(cId));
    		Schedule currSchedule = userService.getScheduleById((long) Integer.parseInt(sId));
    		List <Course> scheduleCourses = currSchedule.getCourses();
    	
    		boolean isError = false;
    		if (currSchedule.getCourseUnits() + currCourse.getUnits() > currSchedule.getUnitCap()) {
    			errors.append("Adding this course would put you over the units restriction! \n");
    			isError = true;
    		}
    		
    		if(!userService.fitSchedule(currCourse, scheduleCourses)) {
    			errors.append(" Adding this course would cause a time conflict! \n");
    			isError = true;
    		}
    		
    		if (currCourse.getTimesEnrolled() == currCourse.getCapacity()) {
    			errors.append(" This course is full, you can add it to your schedule but you will have to waitlist! \n");
    		}
    		
    		if(!isError) {
    			
    			scheduleCourses.add(currCourse);
    			currSchedule.setCourses(scheduleCourses);
    			currSchedule.setCourseUnits(currSchedule.getCourseUnits() + currCourse.getUnits());
    			userService.updateSchedule(currSchedule);
    			success.append(" Course has been successfully added \n");
    		}
    		
    		redirAttrs.addFlashAttribute("errors", errors);
    		redirAttrs.addFlashAttribute("success", success);
    		
    		
    		return "redirect:/displaySchedule/" + sId;
    }
    // Removes the course from the schedule.
    @RequestMapping("/dropCourse/{id1}/{id2}")
    public String dropCourse(Model model, RedirectAttributes redirAttrs, @PathVariable("id1") String sId, @PathVariable("id2") String cId) {
    		StringBuilder errors = new StringBuilder("");
		StringBuilder success = new StringBuilder("");
		
    		Course dropCourse = userService.getCourse(Integer.parseInt(cId));
    		Schedule currSchedule = userService.getScheduleById((long) Integer.parseInt(sId));
    		List <Course> scheduleCourses = currSchedule.getCourses();
    		
    		boolean notDropped = scheduleCourses.remove(dropCourse);
    		if (!notDropped) {
    			errors.append("Unable to remove course.");
    		}else {
    			success.append("Removed course successfully");
    		}
    		
    		currSchedule.setCourses(scheduleCourses);
    		
    		redirAttrs.addFlashAttribute("errors", errors);
    		redirAttrs.addFlashAttribute("success", success);
    		
    		int currUnits = currSchedule.getCourseUnits();
    		currSchedule.setCourseUnits(currUnits - dropCourse.getUnits());
    	
    		
    		
    		userService.updateSchedule(currSchedule);
    		
    		return "redirect:/displaySchedule/" + sId;
    }
    // Removes the schedule from the users saved schedules.
    @RequestMapping("/deleteSchedule/{id1}/{id2}")
    	public String deleteSchedule(Model model, @PathVariable("id1") String spanId,  @PathVariable("id2") String scheduleId ) {
    		
    		userService.deleteSchedule((long) Integer.parseInt(scheduleId));
    		System.out.println();
    		return "redirect:/schedules/" + spanId;
    	
    }
    // Displays information about a specific course. (An elaborate description about the course that can include info like prereqs, etc.)
    @RequestMapping("/courseDetails/{id1}/{id2}")
    public String courseDetails(Model model, @PathVariable("id1") String courseId, @PathVariable("id2") String scheduleId) {
    		
    		Course course = userService.getCourseById((long) Integer.parseInt(courseId));
    		model.addAttribute("course", course);
    		model.addAttribute("scheduleId", scheduleId);
    		
    		return "courseDetails.jsp";
    		
    }
    
    
    
    
    
    
    
    
    

    
    
    
    
    
    
    
    
    
}
