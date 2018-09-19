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

import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import tut4you.model.*;

/**
 * Binds the message inputs to the EJB.
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Named
@ViewScoped
public class MessageBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("MessageBean");
    
    @EJB
    Tut4YouApp tut4youapp;
    private User user;
    private Message message;
    
    /**
     * Creates a new instance of MessageBean
     */
    public MessageBean() {
    }
    
    /**
     * gets the user
     * @return 
     */
    public User getUser() {
        return user;
    }

    /**
     * sets the user
     * @param user 
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * gets the message
     * @return 
     */
    public Message getMessage() {
        return message;
    }

    /**
     * sets the message
     * @param message 
     */
    public void setMessage(Message message) {
        this.message = message;
    }
    
    /**
     * sets the timestamp and send message
     * @return 
     */
    public Message sendMessage() {
        this.message.setDateSent(new Date(System.currentTimeMillis()));
        return message;
    }
}
