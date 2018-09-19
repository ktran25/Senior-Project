/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to find a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.
 
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import tut4you.model.*;


/**
 * Adds new questions and responses to the database
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Named
@ViewScoped
public class QuestionBean implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("QuestionBean");
    @EJB
    private Tut4YouApp tut4youApp;
    
    private Question newQuestion;
    private Question question;
    private Responses responses;
    private User student;
    private List<Course> courseList = new ArrayList();
    private List<Question> questionList = new ArrayList();
    private Course course;
    
    /**
     * Creates a new instance of questionIdentity
     */
    @PostConstruct
    public void QuestionBean(){
        newQuestion = new Question();
        responses = new Responses();
    }
    
    /**
     * Gets the question to being asked
     * @return question
     */
    public Question getQuestion(){
        return newQuestion;
    }

    /**
     * Sets the question the be asked
     *
     * @param newQuestion
     */
    public void setQuestion(Question newQuestion){
        this.newQuestion = newQuestion;
    }
    
    /**
     * gets a list of courses
     * @return courseList
     */
    public List<Course> getCourseList(){
        return courseList;
    }
    
    /**
     * sets a list of courses
     * @param courseList list to be set
     */
    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
    
    /**
     * gets a list of questions
     * @return questionList
     */
    public List<Question> getQuestionList(){
        return questionList;
    }
    
    /**
     * sets a list of questions
     * @param questionList list to be set
     */
    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
    
    /**
     * gets the student asking the question
     * @return student
     */
    public User getStudent(){
        return student;
    }
    
    /**
     * Sets the student to the question
     * @param student to be set
     */
    public void setStudent(User student){
        this.student = student;
    }
    
    /**
     * gets a course
     * @return course
     */
    public Course getCourse(){
        return course;
    }
    
    /**
     * sets a course
     * @param course to be set
     */
    public void setCourse(Course course){
        this.course = course;
    }
    
    /**
     * gets a response
     * @return responses
     */
    public Responses getResponses(){
        return responses;
    }
    
    /**
     * sets a response
     * @param responses to be set
     */
    public void setResponse(Responses responses){
        this.responses = responses;
    }
    
    /**
     * updates courseList based on the subject 
     * and gets the course if it equals the courseName
     * @param subjectName subject name to update the course list
     * @param courseName course name to compare with course names in the list
     */
    public void getCourseList(String subjectName, String courseName) {
        courseList = tut4youApp.getCourses(subjectName);
        for (int i = 0; i < courseList.size(); i++){
            if(courseList.get(i).getCourseName().equals(courseName)){
                this.course = courseList.get(i);
            }
        }
    }
    
    /**
     * returns a question based on the title
     * @param title name of the question
     */
    public void getQuestionTitle(String title){
        this.question = tut4youApp.findQuestionTitle(title);
    }
    
    /**
     * returns a question list based on the course name
     * and checks to see if the title matches any questions
     * @param courseName name of the course
     * @param title name of the question
     */
    public void getQuestionInfo(String courseName, String title) {
        questionList = tut4youApp.getQuestions(courseName);
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).getTitle().equals(title)) {
                this.question = questionList.get(i);
            }
        }
    }
    
    /**
     * Adds a new question to the database
     * @return newQuestion
     */
    public String askNewQuestion(){
        this.newQuestion.setCourse(course);
        tut4youApp.askNewQuestion(newQuestion);
        return "newQuestion";
    }
    
    /**
     * Adds a new response to the database
     * @return newResponse
     */
    public String submitResponses(){
        this.responses.setQuestion(question);
        tut4youApp.responses(responses);
        return "newResponse";
    }
}
