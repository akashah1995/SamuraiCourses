package com.akashah.sam.services;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.akashah.sam.models.*;
import com.akashah.sam.repositories.*;



@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private DayRepository dayRepository;
    private SemesterRepository semesterRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private CourseRepository courseRepository;
    private TimespanRepository timespanRepository;
    private ScheduleRespository scheduleRepository;
    
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, DayRepository dayRepository, SemesterRepository semesterRepository, CourseRepository courseRepository, TimespanRepository timespanRepository, ScheduleRespository scheduleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.dayRepository = dayRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.courseRepository = courseRepository;
        this.timespanRepository = timespanRepository;
        this.scheduleRepository = scheduleRepository;
    }
    
    ////////////////////////
    ///////USER////////////
    ///////////////////////
    
    
    
    
    
    
    
    
    
    
    //Saves a user with a "user" role. This means the user has "student" access. They cannot create courses or timespans. 
    public void saveWithUserRole(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }
     
     //Saves a user with an admin role. Can create courses and timespans.
    public void saveUserWithAdminRole(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepository.findByName("ROLE_ADMIN"));
        userRepository.save(user);
    }    
    
    //Allows you to find a user by UserName.
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
       
    }
    
    
    
    
    
    
    
    /////////////////////////////
    ////////COURSE///////////////
    /////////////////////////////
    
    
    
    
    
    
    
    
    
    
    // Get a course by the course Id.
    public Course getCourseById(long id) {
    		return courseRepository.findOneById(id);
    }
    
    // A optimized custom function that sorts the courses into a List of List of courses, so I can easily display them
    // in a calendar format in the jsp file
    public List<List<Course>> sortCoursesByDayNew(List <Course> courses) { // this function depends on the courses coming into the function being presorted by time.
    		List<List<Course>> output = new ArrayList<List<Course>>();
    		Course blankCourse = new Course();
    		// there are 7 lists -> each one for the different days of the week.
    		for(int i = 0; i < 7; i++){
    			output.add(new ArrayList<Course>());
    		}
    		
    		// Each day has a max of 12 time slots. We fill the list up with blank courses in the beginning.
    		// Each slot represents on hour of time
    		for (int i = 0; i < 7; i++) {
    			for (int j = 0; j < 12; j++) {
    				output.get(i).add(blankCourse);
    			}
    		}
    		
    		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    		String terminate = "21:00"; // we stop looking for courses after this hour (they don't exist)
    		String start = "08:00"; // we start looking for courses in between this time slot.
    		String end = "09:00";
    		Date startD;
    		int startIndex = 0; //we start by looking at the first hour gap. this increments to 12.
    		Date endD;
    		Date terminateLoop;
  
		
    		try { //converts the strings to actual dates.
			terminateLoop = sdf.parse(terminate); 
			startD = sdf.parse(start);
			endD = sdf.parse(end);
			
		} catch (ParseException e) {
			return output;
		}
    		Long hourAdd = (long) 3600000; // this is an hour in milliseconds. This increments the "start and end" so we can look at different spans of time.
    		Long startms = startD.getTime();
    		Long endms = endD.getTime();
    		Long termms = startms + (hourAdd * 12);
    		
    		while(startms < termms) { // this monitors our incrementing.
//    			System.out.println("Got in the while loop");
    				
    			for (int courseIndex = 0; courseIndex < courses.size(); courseIndex++) {
    				
	    			Course currCourse = courses.get(courseIndex);
	    			Long courseStart = currCourse.getStartTime().getTime();
	    			// this checks if the course we are looking at's start time fits the frame of time we are looking at.
	    			if ((courseStart < endms) && ((courseStart > startms) || (courseStart.equals(startms)))) {
		    			
	    			
    					List<Day> days= currCourse.getDays(); // we check what days this course is on.
    					// the way my system works is based on the fact that a course can only occur at a single time across multiple days.
    			
    					for (int j = 0; j < days.size(); j++) {
		    			
		    				Day day = days.get(j);
		    				String name = day.getName();
		    				
		    				if (name.equals("Sunday")) {
		    				
		    			
		    					output.get(0).set(startIndex, currCourse);  // go to index for Sunday. Then add the current course to that index replacing the blank.
		    				}else if (name.equals("Monday")) {
		    					
		    				
		    					
		    					output.get(1).set(startIndex, currCourse);
		    				}else if (name.equals("Tuesday")) {
		    				
		    				
		    					output.get(2).set(startIndex, currCourse);
		    				}else if (name.equals("Wednesday")) {
		    				
		    				
		    					output.get(3).set(startIndex, currCourse);
		    				}else if (name.equals("Thursday")) {
		    					
		    				
		    					output.get(4).set(startIndex, currCourse);
		    				}else if (name.equals("Friday")) {
		    				
		    					
		    					output.get(5).set(startIndex, currCourse);
		    				}else if (name.equals("Saturday")) {
		    				
		    					output.get(6).set(startIndex, currCourse);
		    				}
    					}
	    			}
    			}
    			
    			startIndex++; // now look at the next time slot (index).
    			startms = startms + hourAdd; // increment the time slot.
    			endms = endms + hourAdd;
    			
    		}
    		
    		ArrayList<Integer> emptyRows = new ArrayList<Integer>();
    		//find all the "rows", or common indexes among the different lists (for each day) and mark the ones that are fully empty
    		// There is no need to keep a row unoccupied by any courses in the calendar.
    		for (int i = 0; i < 12; i++) {
    			if (output.get(0).get(i).equals(blankCourse) && output.get(1).get(i).equals(blankCourse) && output.get(2).get(i).equals(blankCourse) && output.get(3).get(i).equals(blankCourse) && output.get(4).get(i).equals(blankCourse) && output.get(5).get(i).equals(blankCourse) && output.get(6).get(i).equals(blankCourse)) {
    				emptyRows.add(i);
    			}
    		}
    		// Goes through the process of removing empty indexes (representing the empty row) from every list.
    		for(int i = 0; i < emptyRows.size(); i++) {
    			int index = emptyRows.get(i);
    			for (int j = 0; j < 7; j++) {
    				output.get(j).remove(index);
    			}
    			for (int k = 0; k < emptyRows.size(); k++){
    				int current = emptyRows.get(k);
    				if (current > index) {
    					emptyRows.set(k, current - 1);
    				}
    			}
    		}
    		
    		
    		return output; // This contains a calendar of courses with empty rows removed.
    		
    }
    
     // Much more complicated way of doing what I did above. Complexity is much higher.
    
//    public List<List<Course>> sortCoursesByDay(List <Course> courses) {
//    		List<List<Course>> output = new ArrayList<List<Course>>();
//    		for(int i = 0; i < 7; i++){
//    			output.add(new ArrayList<Course>());
//    		}
//    		
//    		Course blankCourse = new Course();
//    		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
////    		String beginning = "9:00";
////    		String ending = "10:00";
//    		String terminate = "21:00";
//    		String start = "08:00";
//    		String end = "09:00";
//    		Date startD;
//    		Date endD;
//    		Date terminateLoop;
//  
//		
//    		try {
//			terminateLoop = sdf.parse(terminate);
//			startD = sdf.parse(start);
//			endD = sdf.parse(end);
//			
//		} catch (ParseException e) {
//			return null;
//		}
//    		Long hourAdd = (long) 3600000;
//    		Long startms = startD.getTime();
//    		Long endms = endD.getTime();
//    		Long termms = startms + (hourAdd * 12);
//    		System.out.println(startms);
//    		System.out.println(termms);
//    		
//    		
//    	
//    		
//		while(startms < termms) {
//			ArrayList<Integer> indexes = new ArrayList<Integer>();
//			ArrayList<Integer> tempIndexes = new ArrayList<Integer>();
//	    		for (int k = 0; k < courses.size(); k++) {
//	    			Course currCourse = courses.get(k);
//	    			Course prevCourse;
//	    			if (k > 0) {
//	    				prevCourse = courses.get(k-1);
//	    			}else {
//	    				prevCourse = courses.get(k);
//	    			}
//	    			Long courseStart = currCourse.getStartTime().getTime();
//	    			Long prevCourseStart = prevCourse.getStartTime().getTime();
////	    			boolean part1 = courseStart < endms;
////	    			boolean part2 = courseStart  >  startms;
////	    			boolean part3 = courseStart.equals(startms);
////	    			System.out.print(part1 + " " + part2 + " " + part3);
//	    			
//	    			
//    			   if (!prevCourseStart.equals(courseStart)) { 
//    				   	System.out.println(currCourse.getCourseName());
//    				   	System.out.println(prevCourseStart);
//    				   	System.out.println(courseStart);
//    				   	
//    					for(int w = 0; w < indexes.size(); w++) {
//    						int addBlankTo = indexes.get(w);
//    						output.get(addBlankTo).add(blankCourse);
//    					}
//    					
//    					indexes.clear();
//    				}
//	    			
//	    			if ((courseStart < endms) && ((courseStart > startms) || (courseStart.equals(startms)))) {
//	    			
//	    					List<Day> days= currCourse.getDays();
//	    			
//	    					for (int j = 0; j < days.size(); j++) {
//			    			
//			    				Day day = days.get(j);
//			    				String name = day.getName();
//			    				
//			    				if (name.equals("Sunday")) {
//			    					tempIndexes.add(0);
//			    			
//			    					output.get(0).add(currCourse);
//			    				}else if (name.equals("Monday")) {
//			    					tempIndexes.add(1);
//			    					
//			    					output.get(1).add(currCourse);
//			    				}else if (name.equals("Tuesday")) {
//			    					tempIndexes.add(2);
//			    				
//			    					output.get(2).add(currCourse);
//			    				}else if (name.equals("Wednesday")) {
//			    					tempIndexes.add(3);
//			    				
//			    					output.get(3).add(currCourse);
//			    				}else if (name.equals("Thursday")) {
//			    					tempIndexes.add(4);
//			    				
//			    					output.get(4).add(currCourse);
//			    				}else if (name.equals("Friday")) {
//			    					tempIndexes.add(5);
//			    					
//			    					output.get(5).add(currCourse);
//			    				}else if (name.equals("Saturday")) {
//			    					tempIndexes.add(6);
//			    				
//			    					output.get(6).add(currCourse);
//			    				}
//	    					}
//	    					
//	    					for (int i = 0; i < 7; i++) {
//	    						if (!tempIndexes.contains(i)) {
//	    				
//	    							indexes.add(i);
//	    						}
//	    					}
//	    					
//	    					tempIndexes.clear();
//	    			}
//	    			}
//	    			
//	    			startms = startms + hourAdd;
//	    			endms = endms + hourAdd;
////	    			System.out.println(startms);
//	    		
//			}	
//		
//    			return output;
// 
//    		}
//    
    
   // Sorts (quicksort) the courses by their start times. 
   public void sortCoursesByTime(List<Course> input) {
		int size = input.size();
		
		if (size > 1) {
			quickSort2(0,size - 1, input);
		}
		
    }
   // helper function 
   public void quickSort2(int start, int end, List<Course> input) {
	

	if (start < end) {
		int pivot = partition2(start, end, input);
		
		quickSort2(start, pivot - 1, input);

		
		quickSort2(pivot + 1, end, input);
		
	
	}
}
   // partition helper function
   public int partition2(int start, int end, List<Course> input) {
	int i = start;
	Course pivotCourse = input.get(end);
	Date pivot = pivotCourse.getStartTime();
	int p = start;
	while (i < end) {
		if (input.get(i).getStartTime().before(pivot) || input.get(i).getStartTime().equals(pivot)) {
			Course temp = input.get(i);
			input.set(i, input.get(p));
			input.set(p, temp);
			p++;
		}
		i++;
	}
	Course temp = input.get(end);
	input.set(end, input.get(p));
	input.set(p, temp);
	
	return p;
	
}
    // saves a course to the database.
    public void saveCourse(Course course) {
    		courseRepository.save(course);
    }
    
   
    // gets all the created courses from the database.
    public void getCourses() {
    		courseRepository.findAll();
    }
    
    // returns all the courses for a specific period of time (ex. Spring 2018 courses)
    public List<Course> getCoursesByTimespan(Timespan timespan) {
    		List<Course> allCourses = (List<Course>) courseRepository.findAll();
    		List<Course> output = new ArrayList<Course> ();
    		for (int i = 0; i < allCourses.size(); i++) {
    			Course currCourse = allCourses.get(i);
    			if (currCourse.getTimespan().equals(timespan)){
    				output.add(currCourse);
    			}
    		}
    		
    		return output;
    }
    
    // gets a course by its database id.
    public Course getCourse(int id) {
    		return courseRepository.findOneById((long) id);
    }
    
    //checks whether a course can actually fit into an existing schedule based on day/time conflicts.
    public boolean fitSchedule(Course course, List<Course> existing) {
    		Date courseStart = course.getStartTime();
    		Date courseEnd = course.getEndTime();
    		List <Day> courseDays = course.getDays();
    		
    		
    		for(int i = 0; i < existing.size(); i++) {
    			Course currCourse = existing.get(i);
    			List <Day> days = currCourse.getDays();
    			for(int k = 0; k < courseDays.size(); k++) {
    				Day courseDay = courseDays.get(k);
    				for(int j = 0; j < days.size(); j++) {
    					Day day = days.get(j);
    					if( day.equals(courseDay)) {
    						Date currStart = currCourse.getStartTime();
    		    				Date currEnd = currCourse.getEndTime();
    		    				if (courseStart.before(currEnd) && courseStart.after(currStart)) {
    		        				return false;
    		        			}else if(courseEnd.before(currEnd) && courseEnd.after(currStart)) {
    		        				return false;
    		        			}else if(courseEnd.equals(currEnd) || courseStart.equals(currStart)) {
    		        				return false;
    		        			}
    					}
    				}
    			}
    			
    			
    		}
    		
    		return true;
    		}
    
    
    
    
    
    
    
    		
    
    /////////////////////////////
    ///////////DAY//////////////
    ////////////////////////////
    
    
    
    
    
    
    
    
    
    
    
    
    // saves a day into the database.
    public void saveDay(Day day) {
    		dayRepository.save(day);
    }
    //returns all the days. There are only 7 but they are unique objects.
    public List<Day> allDays(){
    		return (List<Day>) dayRepository.findAll();
    }
    
    //takes in an array of Strings. Each string is the string version of a specific day id.
    // This converts an array of strings  -> list of actual days.
    public List<Day> createListDays(String[] dayArr){
    		List<Day> addthis = new ArrayList <Day>();
    		for (int i = 0; i < dayArr.length - 1; i++) {  // accounts for the last element being an empty spot.
    			Integer id = Integer.parseInt(dayArr[i]);
    			Day day = this.dayRepository.findOneById(id);
    			if (day != null) {
    				addthis.add(day);
    			}
    		}
    		return addthis;
    }
    
    
    
    
    
    
    ///////////////////////////
    //////////TIMESPAN//////////
    ///////////////////////////
    
    
    
    
    
    
    
    
    
    
    
    
    
    // Timespans are either quarters or semesters.
    
    
    
    //save a timespan to the database.
    public void saveTimespan(Timespan timespan) {
    		timespanRepository.save(timespan);
    }
    //get all the timespans from the database.
    public List<Timespan> allTimespans(){
    		return (List<Timespan>) timespanRepository.findAll();
    }
    // get a timespan by its unique id.
    public Timespan getTimespan(long id) {
    		return timespanRepository.findOneById(id);
    }
    
    
    
    
    
    
    
    /////////////////////////
    ////////Schedule////////
    ///////////////////////
    
    
    
    
    
    
    
    
    
    
    // returns a list of all created schedules
    public List <Schedule> allSchedules(){
    		return (List<Schedule>) scheduleRepository.findAll();
    }
    // returns the newest created schedule (newest added to the database)
    public Schedule getNewestSchedule() {
    		List <Schedule> allSchedules = (List<Schedule>) scheduleRepository.findAll();
    		
    		return allSchedules.get(allSchedules.size()-1);
    }
    // saves a schedule to the database
    public void saveSchedule(Schedule schedule) {
    		scheduleRepository.save(schedule);
    }
    // deletes a schedules from the database.
    public void deleteSchedule(long id) {
    		scheduleRepository.deleteById(id);
    }
    // returns a schedule using its specific id.
    public Schedule getScheduleById(long id) {
    		return scheduleRepository.findOneById(id);
    }
    // returns a list of schedules that are for specific timespan (semester)
    public List<Schedule> getSchedulesByTime(User user, Integer spanId){
    		List<Schedule> userSchedules = user.getSchedules();
    		List<Schedule> output = new ArrayList<Schedule>();
    		Timespan currSpan = getTimespan((long) spanId);
    		for (int i = 0; i < userSchedules.size(); i++) {
    			Schedule currSchedule = userSchedules.get(i);
    			if (currSchedule.getTimespan().equals(currSpan)){
    				output.add(currSchedule);
    			}
    		}
    		return output;
    }
    
    
    // sorts all schedules by their priority (custom quicksort)
    public void sortSchedulesByPriority(List<Schedule> input){
    		int size = input.size();
    		if (size > 1) {
    			quickSort(0,size - 1, input);
    		}
    }
    
   
    public void quickSort(int start, int end, List<Schedule> input) {
    		
    		if (start > input.size() - 1) {
    			start = input.size() - 1;
    		}if (end > input.size() - 1) {
    			end = input.size() - 1;
    		}if (start > end) {
    			int pivot = partition(start, end, input);
    			quickSort(start, pivot - 1, input);
    			quickSort(end, pivot + 1, input);
    		}
    }
    
    public int partition( int start, int end, List<Schedule> input) {
    		int i = start;
    		int k = start - 1;
    		int p = end;
    		
    		while (i <= end) {
    			if (input.get(p).getPriority() > input.get(i).getPriority()) {
    				k++;
    				Schedule temp = input.get(i);
    				input.set(i, input.get(k));
    				input.set(k, temp);
    			}
    			if(k == p) {
    				p = i;
    			}
    			i++;
    		}
    		k++;
    		Schedule temp = input.get(p);
		input.set(p, input.get(k));
		input.set(k, temp);
		p=k;
		return p;
    		
    }
    // updates an already existent schedule in the database.
    public void updateSchedule(Schedule schedule) {
    		scheduleRepository.save(schedule);
    }
    
    
   
}