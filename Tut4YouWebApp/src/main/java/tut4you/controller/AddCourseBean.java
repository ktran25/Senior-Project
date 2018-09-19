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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import tut4you.exception.CourseExistsException;
import tut4you.model.*;

/**
 * Adds a new course to the database and tutor.
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Named
@RequestScoped
public class AddCourseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("AddNewCourseBean");

    @EJB
    private Tut4YouApp tut4youApp;

    private Subject subject;
    private Course course;
    private boolean addNewCourse;
    private List<Subject> subjects = new ArrayList();

    /**
     * Creates a new instance of AddCourseBean
     */
    @PostConstruct
    public void createAddCourseBean() {
        addNewCourse = false;
        course = new Course();
    }
    
    /**
     * Destroys a new instance of AddCourseBean
     */
    @PreDestroy
    public void destroyAddCourseBean() {
    }

    /**
     * Gets the subject
     * @return subject
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * Sets the subject
     * @param subject 
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * Gets the course
     * @return course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course
     * @param course 
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * gets boolean attribute to ask tutors if they want to add more courses
     * @return addNewCourse
     */
    public boolean isAddNewCourse() {
        return addNewCourse;
    }

    /**
     * sets boolean attribute for modal to show to ask if they want to add more courses
     * @param addNewCourse 
     */
    public void setAddNewCourse(boolean addNewCourse) {
        this.addNewCourse = addNewCourse;
    }
    
    /**
     * Gets the list of subjects from the database
     * @return subjects
     */
    public List<Subject> getSubjects() {
        if (subjects.isEmpty()) {
            subjects = tut4youApp.getSubjects();
        }
        return subjects;
    }

    /**
     * Sets the list of subjects
     * @param subjects 
     */
    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    /**
     * Adds course to the tutor's course list
     */
    public void addNewCourse() {
        try {
            this.course.setSubject(subject);
            this.course = tut4youApp.addNewCourse(course);
            if (this.course != null) {
                addNewCourse = true;
            }
        }
        catch (CourseExistsException see) {
            FacesContext.getCurrentInstance().addMessage("addNewCourseForm:cn", new FacesMessage("This course already exists."));
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
   }
}
