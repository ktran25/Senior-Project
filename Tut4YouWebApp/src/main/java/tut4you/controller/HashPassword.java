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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hash passwords with SHA512.
 *
 * @author Alvaro Monge (amonge at csulb dot edu)
 */
public class HashPassword {

    /**
     * Generates and returns a SHA512 Digest of the clear text password
     *
     * @param clearTextPassword the password in clear text
     * @return a SHA512 Digest of the password
     *
     * @see
     * https://stackoverflow.com/questions/33085493/hash-a-password-with-sha-512-in-java
     */
    public static String getSHA512Digest(String clearTextPassword) {
        String generatedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            byte[] bytes = md.digest(clearTextPassword.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Logger.getLogger(HashPassword.class.getName()).log(Level.SEVERE, null, e);
            generatedPassword = null;
        }
        return generatedPassword;
    }
}
