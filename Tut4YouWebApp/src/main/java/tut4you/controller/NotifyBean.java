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
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import tut4you.model.Request;
import tut4you.model.*;

/**
 * Has to do with notifications tab such as listing pending requests from
 * students and show the number of notifications.
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Named
@RequestScoped
public class NotifyBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("NotifyBean");

    @EJB
    private Tut4YouApp tut4youApp;
    private Request request;
    private List<Request> pendingRequestList;
    private int numofNotif;

    /**
     * Gets the request
     *
     * @return request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the request
     *
     * @param request
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Gets the list of pending requests
     *
     * @return pendingRequestList
     */
    public List<Request> getPendingRequestList() {
        pendingRequestList = tut4youApp.getPendingRequestForTutor();
        numofNotif = pendingRequestList.size();
        return pendingRequestList;
    }

    /**
     * Sets the list of pending requests
     *
     * @param pendingRequestList
     */
    public void setPendingRequestList(List<Request> pendingRequestList) {
        this.pendingRequestList = pendingRequestList;
    }

    /**
     * Gets the state of hasNotif
     *
     * @return true/false
     */
    public boolean isHasNotif() {
        return numofNotif != 0;
    }

    /**
     * Gets the number of notifications
     *
     * @return the number of notification
     */
    public int getNumofNotif() {
        return numofNotif;
    }

    /**
     * Sets the number of notifications
     *
     * @param numofNotif
     */
    public void setNumofNotif(int numofNotif) {
        this.numofNotif = numofNotif;
    }
}
