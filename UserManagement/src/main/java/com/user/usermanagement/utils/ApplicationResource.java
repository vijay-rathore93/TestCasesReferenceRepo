package com.user.usermanagement.utils;

public class ApplicationResource {
    public static final String USER_NOT_FOUND = "User doesn't exist!";
    public static final String INVALID_TOKEN = "Invalid token!";
    public static final String VERIFICATION_LINK_SENT = "Verification link sent to your registered email!.";
    public static final String VERIFICATION_LINK_SUCCESS = "Verification success!";
    public static final String VERIFICATION_LINK_ERROR = "There was an error during verification!";
    public static final String INACTIVE_USER = "User has never been activated!";
    public static final String OTP_SEND_ERROR = "Couldn't send OTP!";
    public static final String OTP_SEND_SUCCESS = "OTP successfully sent!";
    public static final String OTP_VERIFICATION_ERROR = "Error verifying OTP!";
    public static final String OTP_VERIFICATION_SUCCESS = "OTP successfully verified!";
    public static final String RESET_PASSWORD = " You can now reset your password!";

    public static final String INVALID_EMAIL = "Invalid email!";
    public static final String INVALID_PASSWORD = "Invalid password!";
    public static final String INVALID_CONTACT_NUMBER = "Contact number must be 10 digits!";

    public final static String EMAIL_OTP_MESSAGE = "Hi {0},\nPlease use the following OTP to reset your password.\n OTP: {1}";
    public final static String EMAIL_OTP_SUBJECT= "OTP: Reset Password";
    public final static String EMAIL_REGISTRATION_MESSAGE = "To confirm your e-mail address, please click the link below:\n{0}tokenValidator?token={1}";
    public final static String EMAIL_REGISTRATION_SUBJECT= "Registration Confirmation";

}