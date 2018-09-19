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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import tut4you.model.Payment;
import tut4you.model.Request;
import tut4you.model.Session;
import tut4you.model.Tut4YouApp;
import tut4you.model.Tutor;

/**
 * Payments are made to tutors by students. This class binds the payment inputs
 * to the EJB.
 *
 * @author Syed Haider<shayder426@gmail.com>
 */
@Named
@ViewScoped
public class PaymentBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Payment payment;
    private List<Payment> paymentList = new ArrayList(); //list of payments
    private Tutor tutor;
    private Request request;
    private Session session;
    private boolean paymentStatus;

    @EJB
    private Tut4YouApp tut4youApp;

    /**
     * Creates an instance of the sessionBean
     */
    @PostConstruct
    public void createSessionBean() {
        payment = new Payment();
    }

    /**
     * Destroys an instance of the sessionBean
     */
    @PreDestroy
    public void destroySessionBean() {
    }

    /**
     * Gets the tutor of a session involved with a payment
     *
     * @return the tutor of a session
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Sets the tutor of a session involved with a payment
     *
     * @param tutor the tutor of the session
     */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    /**
     * Gets a payment
     *
     * @return payment - the payment of a session
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * Sets a payment
     *
     * @param payment - the payment of a session
     */
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    /**
     * Gets a list of payments
     *
     * @return paymentList - list of payments
     */
    public List<Payment> getPaymentList() {
        paymentList = tut4youApp.getPaymentList();
        return paymentList;
    }

    /**
     * Sets a list of payments
     *
     * @param paymentList - list of payments
     */
    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    /**
     * Gets the session
     *
     * @return session
     */
    public Session getSession() {
        return session;
    }

    /**
     * Sets the session
     *
     * @param session - the session to be paid for
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * Gets the paymentStatus
     *
     * @return paymentStatus - true if payment is completed
     */
    public boolean isTransactionStatus() {
        return paymentStatus;
    }

    /**
     * Sets the paymentStatus
     *
     * @param paymentStatus - true if payment is completed
     */
    public void setTransactionStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * This checks to see if a payment has a staus of "COMPLETED" in the
     * database. If it does, the "Pay Now" button will not appear.
     *
     * @param payKey - the identifier of a Payment
     * @return true if the payment is completed
     */
    public boolean checkCompletedStatus(String payKey) {
        if (payKey != null && !payKey.isEmpty()) {
            paymentStatus = !tut4youApp.checkCompletedStatus(payKey);
        } else {
            paymentStatus = true;
        }
        return paymentStatus;
    }

    /**
     * This will be run when a student clicks the "Pay Now" button on the "My
     * Sessions" page. Student will be redirected to Paypal to pay for the
     * tutoring session.
     *
     * @param request the completed request in the tutoring session
     */
    public void payForTutoringSession(Request request) {
        String payKey;
        this.session = request.getSession();
        this.tutor = request.getTutor();
        String email = tutor.getEmail();
        double hourlyRate = tutor.getHourlyRate();
        payKey = tut4youApp.generatePayKey(email, hourlyRate, session.getElapsedTimeOfSession());
        
        //This redirects the user to an external website (paypal's payment sandbox URL)
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String url = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey=" + payKey;
        
        //Creates the payment in the database
        payment = tut4youApp.createPayment(payKey, session, request);
        try {
            externalContext.redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
