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
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import tut4you.model.*;

/**
 * Shows information of specific tutors including rating
 *
 * @author Amanda Pan<shayder426@gmail.com>
 * modifid by Syed Haider<shayder426@gmail.com>
 */
@Named
@ViewScoped
public class ProfileBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("ProfileBean");

    @EJB
    private Tut4YouApp tut4youapp;

    @Inject
    private UserBean userBean;

    @ManagedProperty("#{param.username}")
    private String username;
    private User user;
    private String dateJoinedAsTutor;
    private boolean userRated;

    /**
     * Creates a new instance of ProfileBean
     */
    @PostConstruct
    public void createProfileBean() {
    }

    /**
     * Gets username of profile
     * @return username of User
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username of profile
     * @param username of User
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets user
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user
     * @param user 
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets userRated
     * 
     * @return userRated true if current user is writer of review
     */
    public boolean isUserRated() {
        return userRated;
    }
    
    /**
     * Sets userRated to true or false
     * @param userRated 
     */
    public void setUserRated(boolean userRated) {
        this.userRated = userRated;
    }

    /**
     * Checks to see if current user logged in
     * wrote a review. If true, they can edit the review.
     * 
     * @param email email of the logged in user
     * @return true if the logged in user is the writer of the review
     */
    public boolean checkIfUserRated(String email) {
        return tut4youapp.checkIfUserRated(email);
    }

    /**
     * Gets the date joined as a tutor
     * @param username
     * @return dateJoinedAsTutor 
     */
    public String getDateJoinedAsTutor(String username) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Tutor tutor = findTutorByUsername(username);
        dateJoinedAsTutor = sdf.format(tut4youapp.getDateJoinedAsTutor(tutor.getEmail()));
        return dateJoinedAsTutor;
    }

    /**
     * Sets the date joined as a tutor
     * @param dateJoinedAsTutor 
     */
    public void setDateJoinedAsTutor(String dateJoinedAsTutor) {
        this.dateJoinedAsTutor = dateJoinedAsTutor;
    }

    /**
     * This method is used to display the information
     * about a tutor on their profile.
     * @param username 
     */
    public void showUsername(String username) {
        Tutor tutor = findTutorByUsername(username);
        user = tut4youapp.findUser(tutor.getEmail());
    }

    /**
     * This finds the tutor based off their username
     * @param username
     * @return tutor 
     */
    public Tutor findTutorByUsername(String username) {
        return tut4youapp.findTutorByUsername(username);
    }
}