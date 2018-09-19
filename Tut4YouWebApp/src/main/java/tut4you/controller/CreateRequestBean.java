/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to findUser a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.
 
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Conversation;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import json.ZipCodeAPI;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import tut4you.model.*;

/**
 * Binds creating a creates request inputs to the EJB.
 *
 * @author Keith Tran <keithtran25@gmail.com>
 */
@Named
@ConversationScoped
public class CreateRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("RequestBean");
    private @Inject Conversation conversation;
    @EJB
    private Tut4YouApp tut4youApp;
    private static final OkHttpClient client = new OkHttpClient();
    private Request request;
    private ZipCode zipCode;
    private ZipCodeByRadius zipCodeByRadius;
    private Subject subject;
    private Course course;
    private String time;
    @Temporal(TemporalType.TIME)
    private Date laterTime;
    private int lengthOfSession;
    private long numOfTutors; //number of tutors who teaches the course
    private List<Subject> subjectList = new ArrayList(); //list of subjects to be loaded to the request form
    private List<Course> courseList = new ArrayList(); //list of courses based on subject to load to the request form
    private List<Tutor> tutorList; //list of available tutors
    private List<Tutor> temp = new ArrayList();//adds this arraylist into tutorList
    private List<String> zipCodesByRadiusList = new ArrayList();//list of zipCodesByRadius based on a ZipCode
    private Date dayOfWeek;
    private Tutor tutor;
    private boolean isCurrentZipNull;

    /**
     * RequestBean encapsulates all the functions/services involved in making a
     * request
     */
    @PostConstruct
    public void RequestBean() {
        request = new Request();
        zipCode = new ZipCode();
        zipCodeByRadius = new ZipCodeByRadius();
        tutorList = new ArrayList();
        time = "Immediate";
        endConversation();
        initConversation();
        
    }
    
    /**
     * initialize the conversation scope
     */
    public void initConversation(){
        if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
            conversation.begin();
        }
    }
    
    /**
     * End the conversation scope
     */
    public void endConversation(){

        if(!conversation.isTransient()){
            conversation.end();
        }
    }
    
    /**
     * get ZipCodeByRadius
     * @return ZipCodeByRadius
     */
    public ZipCodeByRadius getZipCodeByRadius() {
        return zipCodeByRadius;
    }
    
    /**
     * set ZipCodeByRadius
     * @param zipCodeByRadius 
     */
    public void setZipCodeByRadius(ZipCodeByRadius zipCodeByRadius) {
        this.zipCodeByRadius = zipCodeByRadius;
    }
    
    /**
     * gets if current zip id null
     * @return 
     */
    public boolean isIsCurrentZipNull() {
        return isCurrentZipNull;
    }

    /**
     * sets if current zip id null
     * @param isCurrentZipNull 
     */
    public void setIsCurrentZipNull(boolean isCurrentZipNull) {
        this.isCurrentZipNull = isCurrentZipNull;
    }

    /**
     * get current time
     * @return currentTime
     * @throws ParseException 
     */
    public Date getCurrentTime() throws ParseException {
        String stringCurrentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date currentTime = sdf.parse(stringCurrentTime);
        return currentTime;
    }

    /**
     * Gets current day of when the request is made
     *
     * @return string of the current day
     */
    public String getCurrentDayOfWeek() {
        String str = LocalDate.now().getDayOfWeek().name().toLowerCase();
        String currentDay = str.substring(0, 1).toUpperCase() + str.substring(1);
        return currentDay;
    }
    
    /**
     * Get ZipCode
     * @return ZipCode
     */
    public ZipCode getZipCode() {
        return zipCode;
    }
    
    /**
     * Set ZipCode
     * @param zipCode 
     */
    public void setZipCode(ZipCode zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Gets the Request entity
     *
     * @return the request entity
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the Request entity
     *
     * @param request the request entity
     */
    public void setRequest(Request request) {
        this.request = request;
    }
    
    /**
     * get zip codes by Radius
     *
     * @return zipCodesByRadius List
     */
    public List<String> getZipCodesByRadiusList() {
        //zipCodesByRadius = getData(request.getMaxRadius(), request.getZipCode());
        return zipCodesByRadiusList;
    }

    /**
     * sets zip codes by Radius
     * @param zipCodesByRadiusList 
     */
    public void setZipCodesByRadiusList(List<String> zipCodesByRadiusList) {
        this.zipCodesByRadiusList = zipCodesByRadiusList;
    }

    /**
     * Gets the number of tutors who fit the criteria of a request
     *
     * @return the number of tutors available
     */
    public long getNumOfTutors() {
        return numOfTutors;
    }

    /**
     * Sets the number of tutors who fit the criteria of a request
     *
     * @param numOfTutors the tutors who are available
     */
    public void setNumOfTutors(int numOfTutors) {
        this.numOfTutors = numOfTutors;
    }

    /**
     * Gets the subject of the request
     *
     * @return subject of the request
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the request
     *
     * @param s the subject of the request
     */
    public void setSubject(Subject s) {
        subject = s;
    }

    /**
     * Gets the course of the request
     *
     * @return course of the request
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course of the request
     *
     * @param course the course of the request
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Get the time of the request
     *
     * @return the time of the request
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time of the request
     *
     * @param time the time of the request
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Get the time of the request if user set for later
     *
     * @return the time of the request
     */
    public Date getLaterTime() {
        return laterTime;
    }

    /**
     * Sets the time of the request if user wants a request for later
     *
     * @param laterTime the time of the request if for later
     */
    public void setLaterTime(java.util.Date laterTime) {
        this.laterTime = laterTime;
    }

    /**
     * gets length of session
     *
     * @return lengthOfSession
     */
    public int getLengthOfSession() {
        return lengthOfSession;
    }

    /**
     * sets length of session
     *
     * @param lengthOfSession
     */
    public void setLengthOfSession(int lengthOfSession) {
        this.lengthOfSession = lengthOfSession;
    }

    /**
     * Loads all the subjects from the database.
     *
     * @return a list of subjects
     */
    public List<Subject> getSubjectList() {
        if (subjectList.isEmpty()) {
            subjectList = tut4youApp.getSubjects();
        }
        return subjectList;
    }

    /**
     * Sets the list of subjects in the request
     *
     * @param s the list of subjects
     */
    public void setSubjectList(List<Subject> s) {
        subjectList = s;
    }

    /**
     * Gets the courses in the drop-down menu of request
     *
     * @return the list of courses in database
     */
    public List<Course> getCourseList() {
        return courseList;
    }

    /**
     * Sets the course in the drop-down menu of request
     *
     * @param c the list of courses in the drop-down
     */
    public void setCourseList(List<Course> c) {
        courseList = c;
    }

    /**
     * gets list of available tutors
     * @return 
     */
    public List<Tutor> getTutorList() {
        return tutorList;
    }

    /**
     * sets list of avaliable tutors
     * @param c 
     */
    public void setTutorList(List<Tutor> c) {
        tutorList = c;
    }
    
    /**
     * get day of week
     * @return dayOfWeek
     */
    public Date getDayOfWeek() {
        return dayOfWeek;
    }
    
    /**
     * set day of week
     * @param dayOfWeek 
     */
    public void setDayOfWeek(Date dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    /**
     * get tutor
     * @return 
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * set tutor
     * @param tutor 
     */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    /**
     * ajax calls this method to load the courses based on the selected subject
     */
    public void changeSubject() {
        courseList = tut4youApp.getCourses(subject.getSubjectName());
    }

    /**
     * Creates a new request. If successful, get the number of tutors that
     * tutors the course.
     *
     * @return result to be redirected another page
     * @throws java.text.ParseException
     */
    public String createNewRequest() throws ParseException {
        tutorList = new ArrayList();
        zipCodeByRadius = new ZipCodeByRadius();
        String result = "failure";
        if (time.equals("Later")) {
            request.setSessionTime(getLaterTime());
        } else {
            request.setSessionTime(getCurrentTime());
        }
        request.setDayOfWeek(getCurrentDayOfWeek());
        request.setLengthOfSession(lengthOfSession);
        zipCode = tut4youApp.addZipCode(zipCode);
        request.setZipCode(zipCode);
        request = tut4youApp.newRequest(request);
        if (request != null) {
            numOfTutors = tut4youApp.getNumOfTutorsFromCourse(request.getCourse().getCourseName());
            result = "success";
            zipCodesByRadiusList = tut4youApp.findZipCodeByRadius(zipCode.getId());
            
            if (zipCodesByRadiusList.isEmpty()) {
                //use zip code api query 
                for (String str : getData(zipCode.getMaxRadius(), zipCode.getCurrentZipCode())) {
                    zipCodesByRadiusList = Arrays.asList(str.substring(1, str.length() - 1).split(", "));
                }
            }

            for (int i = 0; i < zipCodesByRadiusList.size(); i++) {
                zipCodeByRadius = new ZipCodeByRadius(zipCodesByRadiusList.get(i));
                zipCodeByRadius = tut4youApp.addZipCodeByRadius(zipCode, zipCodeByRadius);
                temp = new ArrayList();
                temp = tut4youApp.getTutorsFromCourse(request.getCourse().getCourseName(), request.getDayOfWeek(), request.getSessionTime(), false, zipCodesByRadiusList.get(i));
                tutorList.addAll(temp); 
            }
            if(tutorList.contains(tut4youApp.findCurrentTutor())) {
                tutorList.remove(tut4youApp.findCurrentTutor());
            }
        }
        return result;
    }

    /**
     * Send request to a specific tutor
     *
     * @param t
     */
    public void sendToTutor(Tutor t) {
        tut4youApp.addPendingRequest(t, request);
        tutorList.remove(t);
    }
    /**
     * http://square.github.io/okhttp/ get request to use for api
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getJSON(String url) throws IOException {
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();

    }

    /**
     * used to retrieve data from zipcode api api used:
     * http://www.zip-codes.com/content/api/samples/FindZipCodesInRadius.html
     *
     * @param maxRadius
     * @param zipCode
     * @return string of zipcodes
     */
    public static String[] getData(int maxRadius, String zipCode) {
        String json = null;
        try {
            json = getJSON("http://api.zip-codes.com/ZipCodesAPI.svc/1.0/FindZipCodesInRadius?zipcode=" + zipCode + "&minimumradius=0&maximumradius=" + maxRadius + "&key=Q24CBEALGGXBA6CPP4U9");
        } catch (IOException e) {

        }

        Gson gson = new Gson();
        ZipCodeAPI zipCodeAPI = gson.fromJson(json, ZipCodeAPI.class);

        return new String[]{
            Arrays.toString(zipCodeAPI.getDataList())
        };
    }
    
    /** 
     * find current zip. If it is null set isCurrentZipNull to true, else false. 
     * @param username 
     */
    public void findTutorByUsername(String username) {
        tutor = tut4youApp.findTutorByUsername(username);
        if(tutor.getCurrentZip().length() == 0) {
            isCurrentZipNull = true;
        }
        else {
            isCurrentZipNull = false;
        }
    }
}