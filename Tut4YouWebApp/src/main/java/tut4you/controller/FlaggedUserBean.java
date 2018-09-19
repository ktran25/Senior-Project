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
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import tut4you.model.FlaggedUser;
import tut4you.model.Tut4YouApp;


/**
 * FlaggedUserBean flags a user
 * @author Keith Tran <keithtran25@gmail.com>
 */
@Named
@ViewScoped
public class FlaggedUserBean implements Serializable {
        
    @EJB
    private Tut4YouApp tut4youApp;
    
    private FlaggedUser flaggedUser;
    
    /**
     * Creates an instance of the flagUserBean
     */
    @PostConstruct
    public void createFlaggedUserBean() {
        
    }
    
    /**
     * Destroys an instance of the flagUserBean
     */
    @PreDestroy
    public void destroyFlaggedUserBean() {
        
    }
    
    /**
     * get flagged User
     * @return flaggedUser
     */
    public FlaggedUser getFlaggedUser() {
        return flaggedUser;
    }
    
    /**
     * set flagged user
     * @param flaggedUser 
     */
    public void setFlaggedUser(FlaggedUser flaggedUser) {
        this.flaggedUser = flaggedUser;
    }
    
    /**
     * find flagged user based off email
     * @param email
     * @return flagged User
     */
    public FlaggedUser findFlaggedUser(String email) {
        flaggedUser = tut4youApp.findFlaggedUser(email);
        return flaggedUser;
    }
    
    /**
     * checks the time that the user was flagged and the length in time since being flagged
     * @param logInTime
     * @param flaggedUser
     * @return boolean
     */
    public boolean checkIfSuspended(Date logInTime, FlaggedUser flaggedUser) {
        double diff = logInTime.getTime() - flaggedUser.getDateFlagged().getTime();
        double minutes = (diff / 1000) / 60;
        int count = flaggedUser.getCount();
        if(count == 1 && minutes < 1) {
            return true;
        }
        else if(count == 2 && minutes < 3) {
            return true;
        }
        else if(count == 3 && minutes < 5) {
            return true;
        }
        else if(count >= 4) {
            return true;
        }
        else {
            return false;
        }
    }
}