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
 * Gets and Sets the parsed data
 * http://pojo.sodhanalibrary.com
 * @author Keith
 */
public class ZipCodeAPI {
    
    private DataList[] DataList;

    /**
     * Gets the data list from the zipcode API
     * @return DataList from the zipcode API
     */
    public DataList[] getDataList ()
    {
        return DataList;
    }

    /**
     * Sets the data list from the zipcode API
     * @param DataList 
     */
    public void setDataList (DataList[] DataList)
    {
        this.DataList = DataList;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [DataList = "+ DataList +"]";
    }
}