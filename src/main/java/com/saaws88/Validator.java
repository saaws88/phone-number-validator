package com.saaws88;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

  private Pattern MOBILE_PHONE_PATTERN = Pattern.compile("^[7-8][9][\\d]{9}|^[9][\\d]{9}$");

  public Validator() {

  }

  public boolean isMobilePhone(String phoneNumber) {

    Matcher matcher = MOBILE_PHONE_PATTERN.matcher(phoneNumber);
    return matcher.matches();

  }

}
