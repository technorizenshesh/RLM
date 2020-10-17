package com.rlm.Constant;

public class BaseClass {
    private String BaseUrl = "http://rlm.com.sa/webservice/";
//    private String BaseUrl = "http://rlm.com.sa/test/webservice/";

    public static BaseClass get() {
        return new BaseClass();
    }

    public String SignUp() {
        return BaseUrl.concat("signup.php");
    }

    public String Login() {
        return BaseUrl.concat("login.php");
    }
    public String forgotPassword() {
        return BaseUrl.concat("forgot_password.php");
    }public String getService() {
        return BaseUrl.concat("service_list.php");
    }public String getDoctors() {
        return BaseUrl.concat("doctor_list.php");
    }public String getDoctorByService() {
        return BaseUrl.concat("doctor_by_service.php");
    }public String addServiceRating() {
        return BaseUrl.concat("add_service_rating.php");
    }public String addDoctorRating() {
        return BaseUrl.concat("add_doctor_rating.php");
    }public String getBranchList() {
        return BaseUrl.concat("branch_list.php");
    }public String getSection() {
        return BaseUrl.concat("section_list.php");
    }public String addAppointment() {
        return BaseUrl.concat("add_appointment.php");
    }public String getAvailableTime() {
        return BaseUrl.concat("get_available_time.php");
    }public String getAboutUs() {
        return BaseUrl.concat("about_us.php");
    }public String getOfferList() {
        return BaseUrl.concat("offerlist.php");
    }public String getNotificationList() {
        return BaseUrl.concat("notification_list.php");
    }public String DeleteNotification() {
        return BaseUrl.concat("delete_notification.php");
    }
}
