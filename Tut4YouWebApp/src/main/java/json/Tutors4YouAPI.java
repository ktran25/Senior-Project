/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import json.CourseList;
import json.TutorList;
import tut4you.model.Availability;
import tut4you.model.Course;
import tut4you.model.Subject;
import tut4you.model.Tutor;

/**
 *
 * @author Syed Haider
 */
@Path("/api")
@Stateless
public class Tutors4YouAPI {

    @PersistenceContext(unitName = "tut4youWebAppPU")
    private EntityManager em;

    @GET
    @Path("/subjects")
    @Produces({MediaType.APPLICATION_JSON})
    public String getSubjectAndCourses() {
        List<Subject> subj = new ArrayList<>();
        TypedQuery<Subject> query = em.createNamedQuery(Subject.FIND_ALL_SUBJECTS, Subject.class);
        subj = query.getResultList();
        List<String> subjects = new ArrayList<>();
        for (int x = 0; x < subj.size(); x++) {
            subjects.add(subj.get(x).getSubjectName());
        }

        List<Course> course = new ArrayList<>();
        Map<String, List> map = new HashMap<>();
        for (int x = 0; x < subjects.size(); x++) {
            TypedQuery<Course> courseQuery = em.createNamedQuery(Course.FIND_COURSE_BY_SUBJECT, Course.class);
            courseQuery.setParameter("name", subjects.get(x));
            List<Course> courses = courseQuery.getResultList();
            List<String> listOfCourses = new ArrayList<>();
            for (int i = 0; i < courses.size(); i++) {
                listOfCourses.add(courses.get(i).getCourseName());
            }
            map.put(subjects.get(x), listOfCourses);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            // Convert object to JSON string and pretty print
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonInString;
    }

    @GET
    @Path("/course")
    @Produces({MediaType.APPLICATION_JSON})
    public String getCourseByString(@QueryParam("subject") String subject) {
        List<Course> courses = new ArrayList<>();
        if (subject == null || subject.equals("")) {
            return "{ \"error\": \"Please enter a subject.\"}";
        }
        TypedQuery<Course> courseQuery = em.createNamedQuery(Course.FIND_COURSE_BY_SUBJECT, Course.class);
        courseQuery.setParameter("name", subject);
        if (courseQuery.getResultList().isEmpty()) {
            return "{ \"error\": \"The subject does not exist OR the subject may have been mispelled. Make"
                    + "sure to capitalize the appropriate letters.\"}";
        }
        courses = courseQuery.getResultList();
        return createJsonInString(courses);
    }

    @GET
    @Path("/tutor")
    @Produces({MediaType.APPLICATION_JSON})
    public String getTutorCourses(@QueryParam("username") String username) {
        String result = "";
        Course course;
        if (username == null || username.equals("")) {
            return "{ \"error\": \"Please enter a username.\"}";
        }
        TypedQuery<Course> courseQuery = em.createNamedQuery(Course.FIND_COURSES_BY_USERNAME, Course.class);
        courseQuery.setParameter("username", username);
        if (courseQuery.getResultList().isEmpty()) {
            return "{ \"error\": \"The username does not exist.\"}";
        }
        List<Course> courses = courseQuery.getResultList();
        return createJsonInString(courses);

    }

    public String createJsonInString(List<Course> courses) {
        List<CourseList> cc = new ArrayList<>();
        for (int x = 0; x < courses.size(); x++) {
            cc.add(new CourseList(courses.get(x).getCourseName()));
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            // Convert object to JSON string and pretty print
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cc);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonInString;
    }

    @GET
    @Path("/request")
    @Produces({MediaType.APPLICATION_JSON})
    public String getTutorsFromCourse(@QueryParam("course") String course, @QueryParam("dayOfWeek") String dayOfWeek, @QueryParam("zipCode") String zipCode) {
        if (course == null && course.equals("") && dayOfWeek == null && dayOfWeek.equals("")
                && zipCode == null && zipCode.equals("")) {
            return "{ \"error\": \"Missing one or more fields.  \"}";
        }
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");

        /*Date time = new Date();
        try {
            time = df.parse(timeOfRequest);
        } catch (ParseException e) {
            return "{ \"error\": \"Invalid format for time of request. Must be hh:mm:ss  \"}";
        }*/
        
        
        TypedQuery<Course> verifyCourseQuery = em.createNamedQuery(Course.VERIFY_COURSE, Course.class);
        verifyCourseQuery.setParameter("courseName",course);
        if(verifyCourseQuery.getResultList().isEmpty())
            return" { \"error\": \"This course does not exist. \"}";
        
           Date time = new Date();
        try {
            time = getCurrentTime();
        } catch (ParseException ex) {
            Logger.getLogger(Tutors4YouAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        TypedQuery<Availability> findAvailabilityQuery = em.createNamedQuery(Availability.FIND_AVAILABILITIES, Availability.class);
        findAvailabilityQuery.setParameter("requestTime",time, TemporalType.TIME);
        findAvailabilityQuery.setParameter("dayOfWeek",dayOfWeek);
        if(findAvailabilityQuery.getResultList().isEmpty())
            return" { \"error\": \"No tutors available at this time.\"}";
        
        
        TypedQuery<Tutor> zipCodeQuery;
        zipCodeQuery = em.createNamedQuery(Tutor.VERIFY_ZIPCODE, Tutor.class);
        zipCodeQuery.setParameter("zipCode",zipCode);
        if(zipCodeQuery.getResultList().isEmpty())
            return" { \"error\": \"There is no tutor associated "
                    + "with this zipcode.\"}";
        
        TypedQuery<Tutor> courseTutorQueryD = em.createNamedQuery(Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME_DZIP, Tutor.class);
        courseTutorQueryD.setParameter("coursename", course);
        courseTutorQueryD.setParameter("dayofweek", dayOfWeek);
        courseTutorQueryD.setParameter("requestTime", time, TemporalType.TIME);
        courseTutorQueryD.setParameter("doNotDisturb", false);
        courseTutorQueryD.setParameter("zipCode", zipCode);
        TypedQuery<Tutor> courseTutorQueryC = em.createNamedQuery(Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME_CZIP, Tutor.class);

        courseTutorQueryC.setParameter("coursename", course);
        courseTutorQueryC.setParameter("dayofweek", dayOfWeek);
        courseTutorQueryC.setParameter("requestTime", time, TemporalType.TIME);
        courseTutorQueryC.setParameter("doNotDisturb", false);
        courseTutorQueryC.setParameter("zipCode", zipCode);
        List<Tutor> allAvailableTutors = new ArrayList();
        if (courseTutorQueryD.getResultList().isEmpty()) {
           return "{ \"error\": \"There are no tutors available.  \"}";
        }System.out.println("sdhiosahndosajdoias");
        allAvailableTutors.addAll(courseTutorQueryD.getResultList());
        for (Tutor x : courseTutorQueryC.getResultList()) {
            if (!allAvailableTutors.contains(x)) {
                allAvailableTutors.add(x);
            }
        }
        List<TutorList> tutors = new ArrayList<>();
        for (int x = 0; x < allAvailableTutors.size(); x++) {
            String name = allAvailableTutors.get(x).getFirstName() + " " + allAvailableTutors.get(x).getLastName();
            tutors.add(new TutorList(name));
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tutors);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonInString;
    }

    
    public Date getCurrentTime() throws ParseException {
        String stringCurrentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date currentTime = sdf.parse(stringCurrentTime);
        return currentTime;
    }
}