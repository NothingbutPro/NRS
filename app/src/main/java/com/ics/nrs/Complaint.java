package com.ics.nrs;

/**
 * Created by android on 5/25/2018.
 */

public class Complaint {

    int prod_image;
    String complaint_code;
    String complaint_descrip;
    //String status;
    String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    String closed;

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    String feedback_image;
    String name;
    String mobile_no;
    String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    Complaint(){}


   /* Complaint(String complaint_code, String name,String mobile_no,String location)
    {
        this.name=name;
        this.mobile_no=mobile_no;
        this.location=location;
        this.prod_image=prod_image;
        this.complaint_code=complaint_code;
        this.complaint_descrip=complaint_descrip;
        this.status=status;
        this.feedback_image=feedback_image;
    }*/

    public int getProd_image() {
        return prod_image;
    }

    public void setProd_image(int prod_image) {
        this.prod_image = prod_image;
    }

    public String getComplaint_code() {
        return complaint_code;
    }

    public void setComplaint_code(String complaint_code) {
        this.complaint_code = complaint_code;
    }

    public String getComplaint_descrip() {
        return complaint_descrip;
    }

    public void setComplaint_descrip(String complaint_descrip) {
        this.complaint_descrip = complaint_descrip;
    }

   /* public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }*/

    public String getFeedback_image() {
        return feedback_image;
    }

    public void setFeedback_image(String feedback_image) {
        this.feedback_image = feedback_image;
    }
}
