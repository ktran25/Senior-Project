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
package json;

/**
 * Contains parsed JSON data in POJO class
 * @author Keith
 */
public class DataList {
    
    private String County;
    private String State;
    private String Latitude;
    private String Longitude;
    private String Code;
    private String City;
    
    /**
     * Gets the country
     * @return country
     */
    public String getCounty ()
    {
        return County;
    }

    /**
     * Sets the country
     * @param County 
     */
    public void setCounty (String County)
    {
        this.County = County;
    }

    /**
     * Gets the state
     * @return State
     */
    public String getState ()
    {
        return State;
    }

    /**
     * Sets the state
     * @param State 
     */
    public void setState (String State)
    {
        this.State = State;
    }

    /**
     * Gets the latitude
     * @return latitude
     */
    public String getLatitude ()
    {
        return Latitude;
    }

    /**
     * Sets the latitude
     * @param Latitude 
     */
    public void setLatitude (String Latitude)
    {
        this.Latitude = Latitude;
    }

    /**
     * Gets the longitude
     * @return longitude
     */
    public String getLongitude ()
    {
        return Longitude;
    }

    /**
     * Sets the longitude
     * @param Longitude 
     */
    public void setLongitude (String Longitude)
    {
        this.Longitude = Longitude;
    }

    /**
     * Gets the zipcode
     * @return zipcode
     */
    public String getCode ()
    {
        return Code;
    }

    /**
     * Sets the zipcode
     * @param Code 
     */
    public void setCode (String Code)
    {
        this.Code = Code;
    }

    /**
     * Gets the city
     * @return city
     */
    public String getCity ()
    {
        return City;
    }

    /**
     * Sets the city
     * @param City 
     */
    public void setCity (String City)
    {
        this.City = City;
    }

    @Override
    public String toString()
    {
        return Code;
    }
}