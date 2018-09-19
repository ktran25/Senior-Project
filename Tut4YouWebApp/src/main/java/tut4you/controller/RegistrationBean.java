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
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import tut4you.model.*;
import tut4you.exception.*;

/**
 * RegistrationBean encapsulates all the functions/services involved in
 * registering as a User or Tutor.
 *
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 * @author Keith Tran <keithtran25@gmail.com>
 */
@Named
@ViewScoped
public class RegistrationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("RequestBean");

    @EJB
    private Tut4YouApp tut4youApp;

    private User newUser;
    private String confirmPassword;
    private String userType;
    private String hourlyRate;
    private Boolean isStudent;
    private String defaultZip;
    private Date joinedDateAsTutor;
    
    /**
     * Creates a new instance of Registration
     */
    @PostConstruct
    public void  createRegistrationBean() {
        newUser = new User();
    }
    
    /** 
     * Destroy a new instance of Registration
     */
    @PreDestroy
    public void destroyRegistrationBean() {
        
    }

    /**
     * Gets the new user who just registered
     * @return the new User entity
     */
    public User getNewUser() {
        return newUser;
    }

    /**
     * Sets the user to be a User
     * @param newUser student who has just registered
     */
    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    /**
     * Gets the field of the confirm Password
     * @return the field of the confirm Password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the value in the confirm password to check if password match
     * @param confirmPassword the password matching the typed password
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Gets the userType
     * @return the userType is Tutor or User
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Sets the userType
     * @param userType the userType is tutor or student
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    /**
     * get price rate
     * @return hourlyRate
     */
    public String getHourlyRate() {
        return hourlyRate;
    }
    
    /**
     * set price rate
     * @param hourlyRate 
     */
    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
    /**
     * get default zip
     * @return defaultZip
     */
    public String getDefaultZip() {
        return defaultZip;
    }
    
    /**
     * set defaultZip
     * @param defaultZip 
     */
    public void setDefaultZip(String defaultZip) {
        this.defaultZip = defaultZip;
    }
    
    /**
     * Checks to see if the user is a student
     * @return true if user is a student
     */
    public Boolean getIsStudent() {
        return isStudent.equals("Student");
    }
    
    /**
     * Gets join Date as a tutor
     * @return joinedDateAsTutor
     */
    public Date getJoinedDateAsTutor() {
        return joinedDateAsTutor;
    }
    
    /**
     * Sets join Date as a tutor
     * @param joinedDateAsTutor 
     */
    public void setJoinedDateAsTutor(Date joinedDateAsTutor) {
        this.joinedDateAsTutor = joinedDateAsTutor;
    }
    
    /**
     * JSF Action that uses the information submitted in the registration page
     * to add user as a registered User user.
     * @return either failure, success, or register depending on successful
     * registration.
     */
    public String createUser() {
        String result = "failure";
        try {
            String gRecaptchaResponse = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("g-recaptcha-response"); //get reCAPTCHA request param
            boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse);
            if (verify) {
                if (newUser.isInformationValid(confirmPassword)) {
                    newUser.setPassword(tut4you.controller.HashPassword.getSHA512Digest(newUser.getPassword()));
                    try {
                        double hr = 0;
                        if (hourlyRate != null) {
                            hr = Double.parseDouble(hourlyRate);
                        }
                        joinedDateAsTutor = getCurrentDate();
                        tut4youApp.registerUser(newUser, userType, hr, defaultZip, joinedDateAsTutor);
                        result = "success";
                    }
                    catch (UserExistsException see) {
                        FacesContext.getCurrentInstance().addMessage("registration:em", new FacesMessage("A user with that information already exists, try again."));
                        result = "register";
                    }
                }
            }
            else {
                FacesContext.getCurrentInstance().addMessage("registration:captcha", new FacesMessage("We need to make sure that you are human!"));
                result = "register";
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            result = "failure";
        }
        return result;
    }
    
    /**
     * https://stackoverflow.com/questions/33098603/convert-localtime-java-8-to-date
     * gets the current date which is used for date joined attribute in tutor
     * @return date joined
     * @throws ParseException 
     */
    public Date getCurrentDate() throws ParseException {
        LocalTime d = LocalTime.now();
        Instant instant = d.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        Date time = Date.from(instant);
        return time;
    }
}
