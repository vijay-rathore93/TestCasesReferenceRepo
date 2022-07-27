package com.user.usermanagement.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserInputValidator {

    private Pattern emailPattern;
    private Pattern passwordPattern;
    private Matcher emailMatcher;
    private Matcher passwordMatcher;

    /*
     * The combination means, email address must start with “_A-Za-z0-9-\\+” , optional follow by “.[_A-Za-z0-9-]”, and end with a “@” symbol.
     *  The email’s domain name must start with “A-Za-z0-9-“, follow by first level Tld (.com, .net) “.[A-Za-z0-9]”
     * and optional follow by a second level Tld (.com.au, .com.my) “\\.[A-Za-z]{2,}”, where second level Tld must start with a dot “.”
     * and length must equal or more than 2 characters.
     */
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /*
     * At least one upper case character (A-Z)
     * At least one lower case character (a-z)
     * At least one digit (0-9)
     * At least one special character (Punctuation)
     * Password should not start with a digit
     * Password should not end with a special character
     */
    private static final String PASSWORD_PATTERN = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\p{Punct})^[^\\d].*[a-zA-Z\\d]$";

    public UserInputValidator() {
        emailPattern = Pattern.compile(EMAIL_PATTERN);
        passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     *
     * @param password email for validation
     * @return true valid email, false invalid email
     */
    public boolean validatePassword(final String password) {
        passwordMatcher = passwordPattern.matcher(password);
        return passwordMatcher.matches();
    }

    /**
     * Validate email with regular expression
     *
     * @param email email for validation
     * @return true valid email, false invalid email
     */
    public boolean validateEmail(final String email) {
        emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }
}
