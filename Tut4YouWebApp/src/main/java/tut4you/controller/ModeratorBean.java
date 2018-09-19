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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import static org.omnifaces.util.Faces.getServletContext;
import org.primefaces.model.UploadedFile;
import tut4you.model.FlaggedUser;
import tut4you.model.ModeratorApplication;
import tut4you.model.Tut4YouApp;
import tut4you.model.Tutor;
import tut4you.model.User;


/**
 * Moderator bean that has moderator features
 * @author Keith Tran <Keithtran25@gmail.com>
 */
@Named
@ViewScoped
public class ModeratorBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("TranscriptBean");
    @Inject
    private ComplaintBean complaintBean;
    @Inject
    FlaggedUserBean flaggedUserBean;
    @Inject
    private UserBean userbean;

    @EJB
    private Tut4YouApp tut4youApp;
    private UploadedFile file; //uploaded file for AWS
    private String reason; //string reason for the complaint
    private ModeratorApplication moderatorApplication; //moderator application object
    private List<ModeratorApplication> moderatorApplicationList = new ArrayList(); //list of moderator applications
    private List<Tutor> lowRatingTutorList = new ArrayList(); //list of low rating tutors
    @ManagedProperty("#{param.username}")
    private String username; //string username used as a parameter
    private Tutor tutor;
    private User user;
    private String url;
    
    /**
     * PostConstruct for Moderatorbean
     */
    @PostConstruct
    public void createModeratorBean() {
    }
    
    /**
     * Destroys an instance of the ModeratorBean
     */
    @PreDestroy
    public void destroyModeratorBean() {
    }
    
    /**
     * get ComplaintBean
     * @return complaintBean
     */
    public ComplaintBean getComplaintBean() {
        return complaintBean;
    }
    /**
     * set ComplaintBean
     * @param complaintBean 
     */
    public void setComplaintBean(ComplaintBean complaintBean) {
        this.complaintBean = complaintBean;
    }
    
    /**
     * gets url
     * @return url
     */
    public String getURL(){
        return url;
    }
    
    /**
     * set url
     * @param url 
     */
    public void setURL(String url){
        this.url = url;
    }
        
    /**
     * set low rating tutor list
     * @param lowRatingTutorList 
     */
    public void setLowRatingTutorList(List<Tutor> lowRatingTutorList) {
        this.lowRatingTutorList = lowRatingTutorList;
    }
    
    /**
     * get tutor
     * @return 
     */
    public Tutor getTutor() {
        return tutor;
    }
    
    /**
     * set tutor
     * @param tutor 
     */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
    
    /**
     * get Username
     * @return 
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * set Username
     * @param username 
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * get moderator application
     * @return moderator application
     */
    public ModeratorApplication getModeratorApplication() {
        return moderatorApplication;
    }
    
    /**
     * set moderator application
     * @param moderatorApplication 
     */
    public void setModeratorApplication(ModeratorApplication moderatorApplication) {
        this.moderatorApplication = moderatorApplication;
    }
    
    /**
     * get reason
     * @return reason
     */
    public String getReason() {
        return reason;
    }
    
    /**
     * set reason
     * @param reason 
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    /**
     * get user
     * @return user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * set user
     * @param user 
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    /**
     * set uploaded file
     * @param file 
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    /**
     * get uploaded file
     * @return 
     */
    public UploadedFile getFile() {
        return file;
    }
    
    /**
     * Gets a list of the moderator Applications in the EJB
     * @return a list of subjects
     */
    public List<ModeratorApplication> getModeratorApplicationList() {
        if (moderatorApplicationList.isEmpty()) {
            moderatorApplicationList = tut4youApp.getModeratorApplications();
        }
        return moderatorApplicationList;
    }
    
    /**
     * Sets the moderatorApplicationList
     * @param moderatorApplicationList
     */
    public void setModeratorApplicationList(List<ModeratorApplication> moderatorApplicationList) {
        this.moderatorApplicationList = moderatorApplicationList;
    }
    
    /**
     * gets list of tutors with ratings at or below a 2.0, 
     * and removes tutors from the list that have been flagged before in the past 5 minutes,
     * or if tutor has less than 2 reviews
     * @return tutor list
     * @throws ParseException 
     */
    public List<Tutor> getLowRatingTutorList() throws ParseException {
        Date currentDate = new Date();
        lowRatingTutorList = tut4youApp.findLowRatingTutors();
        for(int i = 0; i < lowRatingTutorList.size(); i++) {
            FlaggedUser flaggedUser = flaggedUserBean.findFlaggedUser(lowRatingTutorList.get(i).getEmail());
            Tutor lowRatingTutor = tut4youApp.findTutor(lowRatingTutorList.get(i).getEmail());
           //if user has never been flagged before, remove user from list
            if( lowRatingTutorList.get(i).getRatings().size() < 2) {
                lowRatingTutorList.remove(lowRatingTutorList.get(i));
            }
            else {
                double timeDifference = currentDate.getTime() - flaggedUser.getDateFlagged().getTime();
                double minutes = (timeDifference / 1000) / 60;
                //if time since last suspension has been less than 5 min,
                //or tutor has not been banned yet, remove 
                //tutor from low rating tutor list
                if(minutes < 5 || flaggedUser.getCount() >= 4) {
                    lowRatingTutorList.remove(lowRatingTutorList.get(i));
                }
            }
            
        }
        return lowRatingTutorList;
    }
    
    /**
     * return outcome to for redirection
     * @return viewLowRatingTutors
     */
    public String viewLowRatingTutors() {
        return "viewLowRatingTutors";
    }
    
    /**
     * show low rating tutor username which is used when passing in the username parameter
     * @param username 
     */
    public void showLowRatingTutorUsername(String username) {
        tutor = findLowRatingTutor(username);
    }
    
    /**
     * showUsername method used when passing the parameter between 
     * jsf pages for moderatorApplication
     * @param username 
     */
    public void showUsername(String username) {
        moderatorApplication  = findModeratorApplication(username);
        user = tut4youApp.findUser(moderatorApplication.getUser().getUsername());
    }
    
    /**
     * find moderator application based on username
     * @param username
     * @return moderator application
     */
    public ModeratorApplication findModeratorApplication(String username) {
        return tut4youApp.findModeratorApplication(username);
    }
    
    /**
     * find low rating tutor based on username
     * @param username
     * @return tutor
     */
    public Tutor findLowRatingTutor(String username) {
        return tut4youApp.findTutorByUsername(username);
    }
    
    /**
     * accept moderator application
     * @param moderatorApplication 
     * @return  outcome string
     */
    public String acceptModeratorApplication(ModeratorApplication moderatorApplication) {
        tut4youApp.acceptModeratorApplication(moderatorApplication);
        return "viewModeratorApplications";
    }
    
    /**
     * decline moderator application
     * @param moderatorApplication 
     * @return  outcome string
     */
    public String declineModeratorApplication(ModeratorApplication moderatorApplication) {
        tut4youApp.declineModeratorApplication(moderatorApplication);
        return "viewModeratorApplications";
    }
    
    /**
     * generate secure url to view resume
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public String generatePresignedUrlRequest() throws FileNotFoundException, IOException {
        user = tut4youApp.findUserByUsername(username);
        String keyName = user.getModeratorApplication().getResumeFilePath();
        String worked = "";
        if (keyName == null){
                FacesMessage message = new FacesMessage("No resume found");
                FacesContext.getCurrentInstance().addMessage(null, message); 
                worked = "noViewFile";
        } else {
            Properties prop = new Properties();
            InputStream propstream = new FileInputStream(getServletContext().getRealPath("WEB-INF/s3.properties"));
            prop.load(propstream);
            AWSCredentials credentials = new BasicAWSCredentials(prop.getProperty("AWSAccessKeyId"), prop.getProperty("AWSSecretKey"));
            String bucketName = prop.getProperty("bucketName");
            // source: https://stackoverflow.com/questions/4bucketName1951978/amazons3clientcredentials-is-deprecated
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
            Date expiration = new Date();
            long msec = expiration.getTime();
            msec += 1000 * 60 * 60; //expires in 1 hour.
            expiration.setTime(msec);
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, keyName);
            generatePresignedUrlRequest.setMethod(HttpMethod.GET);
            generatePresignedUrlRequest.setExpiration(expiration);
            URL s = s3.generatePresignedUrl(generatePresignedUrlRequest);
            this.url = s.toString();
            worked = "viewResume";
            
        }
        return worked;
    }
    
    
    /**
     * download resume from S3
     * @throws IOException 
     */
    public void downloadResume() throws IOException {
        Properties prop = new Properties();
        InputStream propstream = new FileInputStream(getServletContext().getRealPath("WEB-INF/s3.properties"));
        prop.load(propstream);
        AWSCredentials credentials = new BasicAWSCredentials(
                    prop.getProperty("AWSAccessKeyId"),
                    prop.getProperty("AWSSecretKey"));
        String bucketName = prop.getProperty("bucketName");

        //Taken from: https://stackoverflow.com/questions/4bucketName1951978/amazons3clientcredentials-is-deprecated
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Write);
        try {
            user = tut4youApp.findUserByUsername(username);
                 String keyName = user.getModeratorApplication().getResumeFilePath();
            if (keyName == null){
                FacesMessage message = new FacesMessage("No resume uploaded yet");
                FacesContext.getCurrentInstance().addMessage(null, message); 
            }
            else {
                String resumeName = user.getUsername()+"RESUME";

                S3Object s3Object = s3.getObject(new GetObjectRequest(bucketName, keyName));
                S3ObjectInputStream stream = s3Object.getObjectContent();

                String home = System.getProperty("user.home");
                Path path = Paths.get(home);
                File file = new File(home, resumeName.concat(".pdf"));
                int i = 1;
                while (file.exists() == true) { //if file exists at that location increment the file name by 1 
                    file = new File(home, resumeName.concat("(" + i + ").pdf"));
                    i++;
                }
                FileUtils.copyInputStreamToFile(stream, file);
                FacesMessage message = new FacesMessage("Succesfully downloaded file to: " + path + "/" + file);
                FacesContext.getCurrentInstance().addMessage(null, message);
                stream.close();
            }  
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was "
                    + "rejected with an error response for some reason.");
            System.out.println("Error Message:  " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:  " + ase.getErrorCode());
            System.out.println("Error Type:    " + ase.getErrorType());
            System.out.println("Request ID:    " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while "
                    + "trying to communicate with S3, such as not being able to access the network.");
        }
    }
    
    /**
     * upload resume to S3
     * @return result
     * @throws IOException 
     */
    public String uploadResume() throws IOException {
        String result;
        Properties prop = new Properties();
        InputStream propstream = new FileInputStream(getServletContext().getRealPath("WEB-INF/s3.properties"));
        prop.load(propstream);
        AWSCredentials credentials = new BasicAWSCredentials(
                    prop.getProperty("AWSAccessKeyId"),
                    prop.getProperty("AWSSecretKey"));
        String bucketName = prop.getProperty("bucketName");
        try {
            //System.out.println("Key: " + credentials.getAWSAccessKeyId() + ", Secret: " + credentials.getAWSSecretKey());
        } catch (Exception e) {
            throw new AmazonClientException(e);
        }
        //Taken from: https://stackoverflow.com/questions/4bucketName1951978/amazons3clientcredentials-is-deprecated
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Write);
        try {
            String userName = userbean.getEmailFromSession();
            user = tut4youApp.findUser(userName);
            String resumeName = user.getUsername();
            String filePath = "resume/";
            String keyName = filePath.concat(resumeName.concat(".pdf"));
            
            try (S3Object s3Object = new S3Object()) {
                ObjectMetadata omd = new ObjectMetadata();
                omd.setContentType(file.getContentType());
                omd.setContentLength(file.getSize());
                omd.setHeader("filename", file.getFileName());
                InputStream input = file.getInputstream();
                s3Object.setObjectContent(input);
                s3.putObject(new PutObjectRequest(bucketName, keyName, input, omd).withAccessControlList(acl));
                FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                tut4youApp.addResumeFileLocation(keyName, reason);
            }
            result = "success";
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was "
                    + "rejected with an error response for some reason.");
            System.out.println("Error Message:  " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:  " + ase.getErrorCode());
            System.out.println("Error Type:    " + ase.getErrorType());
            System.out.println("Request ID:    " + ase.getRequestId());
            result = "failure";
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while "
                    + "trying to communicate with S3, such as not being able to access the network.");
            result = "failure";
        }
        return result;
    }
}
