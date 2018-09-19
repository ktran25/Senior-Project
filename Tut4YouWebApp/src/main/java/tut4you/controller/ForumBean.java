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
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import tut4you.model.*;


/**
 * Loads subjects, courses, questions, and responses from the database
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Named
@ViewScoped
public class ForumBean implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("ForumBean");
    @EJB
    private Tut4YouApp tut4youApp;
    private Question question;
    private Responses responses;
    private Subject subject;
    private List<Subject> subjectList = new ArrayList();
    private List<Course> courseList = new ArrayList();
    private List<Question> questionList = new ArrayList();
    private List<Responses> responseList = new ArrayList();
    
    /**
     * Gets the subject
     * @return subject
     */
    public Subject getSubject(){
        return subject;
    }
    
    /**
     * sets the subject
     * @param subject 
     */
    public void setSubject(Subject subject){
        this.subject = subject;
    }
    
    /**
     * gets a list of subjects
     * @return subjectList
     */
    public List<Subject> getSubjectList(){
        if (subjectList.isEmpty()){
            subjectList = tut4youApp.getSubjects();
        }
        return subjectList;
    }
    
    /**
     * sets a list of subjects
     * @param subjectList list being set
     */
    public void setSubjectList(List<Subject> subjectList){
        this.subjectList = subjectList;
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
     * @param courseList list being set
     */
    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
    
    /**
     * gets a list of responses
     * @return responseList
     */
    public List<Responses> getResponseList(){
        return responseList;
    }
    
    /**
     * sets a list of responses
     * @param responseList list being set
     */
    public void setResponseList(List<Responses> responseList) {
        this.responseList = responseList;
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
     * @param questionList list being set
     */
    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
    
    /**
     * changes the courseList based off the subject name
     * @param name subject name being passed in
     */
    public void changeSubject(String name) {
        courseList = tut4youApp.getCourses(name);
    }
    
    /**
     * changes the questionList based off the course name
     * @param name course name being passed in
     */
    public void changeCourse(String name) {
        questionList = tut4youApp.getQuestions(name);
    }
    
    /**
     * gets a question
     * @return question
     */
    public Question getQuestion(){
        return question;
    }
    
    /**
     * sets a question
     * @param question to be set
     */
    public void setQuestion(Question question){
        this.question = question;
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
     * Updates response list based off the question title
     * @param questionTitle name of the question
     */
    public void getResponses(String questionTitle) {
        responseList = tut4youApp.getResponses(questionTitle);
    }
    
    /**
     * gets the username of whoever asked the question
     * @param title name of the question
     * @return username 
     */
    public String getQuestionTitle(String title){
        question = tut4youApp.findQuestionTitle(title);
        return question.getStudent().getUsername();
    }
    
    /**
     * gets a question object depending on the courseName and question title
     * @param courseName name of the course
     * @param title name of the question
     * @return question
     */
    public Question getQuestionInfo(String courseName, String title) {
        questionList = tut4youApp.getQuestions(courseName);
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).getTitle().equals(title)) {
                this.question = questionList.get(i);
            }
        }
        return question;
    }
    
    /**
     * Checks if the logged in user asked the question and if so they can view the answer
     * @param questionTitle title of the question
     * @return isUserQuestion either true or false
     */
    public boolean isUserQuestion(String questionTitle) {
        UserBean userBean = new UserBean();
        User user;
        String currentUserEmail = userBean.getEmailFromSession();
        boolean isUserQuestion = false;
        String userName;
        
        if (currentUserEmail == null) {
            isUserQuestion = false;
        } else {
            user = tut4youApp.findUser(currentUserEmail);
            userName = user.getUsername();
            if(tut4youApp.findQuestionTitle(questionTitle) == null){
                isUserQuestion = false;
            } else {
                Question questions = tut4youApp.findQuestionTitle(questionTitle);
                if (questions.getStudent().getUsername().equals(userName)) {
                    isUserQuestion = true;
                }
            }
        }
        return isUserQuestion;
    }
}