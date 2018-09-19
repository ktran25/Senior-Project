/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.

 *  Tutors4You is a web application that students can utilize to findUser a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.

 *  2017 Amanda Pie <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import tut4you.exception.AvailabilityExistsException;
import tut4you.model.*;
import tut4you.model.Tut4YouApp;

/**
 * Stores the availability of a tutor
 *
 * @author Andrew Kaichi <Andrew.Kaichi@student.csulb.edu>
 * modified by Syed Haider <shayder426@gmail.com>
 */
@Named
@ViewScoped
public class AvailabilityBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("AvailabilityBean");

    @EJB
    private Tut4YouApp tut4youApp;

    private Availability availability;
    @Temporal(TemporalType.TIME)
    private java.util.Date startTime;
    @Temporal(TemporalType.TIME)
    private java.util.Date endTime;
    private List<Availability> availabilityList = new ArrayList();
    private boolean addNewAvailability = false;

    /**
     * Creates a new instance of the Availability entity
     */
    public AvailabilityBean() {
        availability = new Availability();
    }

    /**
     * Gets the availability of the tutor
     * @return the availability of the tutor
     */
    public Availability getAvailability() {
        return availability;
    }

    /**
     * Sets the availability of the tutor (not used)
     * @param availability the availability of the tutor
     */
    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    /**
     * Gets the start time
     * @return startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time
     * @param startTime
     */
    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time
     * @return endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the addNewAvailability which determines if the modal will show or
     * not
     * @return true if availability is added
     */
    public boolean isAddNewAvailability() {
        return addNewAvailability;
    }

    /**
     * Sets the addNewAvailability which determines if the modal will show or
     * not
     * @param addNewAvailability true/false if availability is added
     */
    public void setAddNewAvailability(boolean addNewAvailability) {
        this.addNewAvailability = addNewAvailability;
    }

    /**
     * Gets a list of the availabilities of the Tutor from the database
     * @return a list of availabilities
     */
    public List<Availability> getAvailabilityList() {
        availabilityList = tut4youApp.getAvailability();
        return availabilityList;
    }

    /**
     * Adds the availability to the tutor
     * @throws java.text.ParseException
     */
    public void addAvailability() throws ParseException {
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        try {
            availability = tut4youApp.addAvailability(availability);
            addNewAvailability = availability != null;
        } catch (AvailabilityExistsException see) {
            FacesContext.getCurrentInstance().addMessage("addAvailabilityForm:availability", new FacesMessage("You have already set this availability."));
        }
    }
    
    /**
     * Delete the availability from the tutor
     * @param avail
     * @throws java.text.ParseException
     */
    public void deleteAvailability(Availability avail) throws ParseException {
        tut4youApp.deleteAvailability(avail);
    }
}
