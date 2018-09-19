/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;


/**
 *
 * @author Saadman
 */
public class CourseList {
    private String courseName;

    public CourseList()
    {
        
    }
    public CourseList(String courseName)
    {
        this.courseName = courseName;
    }
    
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
}