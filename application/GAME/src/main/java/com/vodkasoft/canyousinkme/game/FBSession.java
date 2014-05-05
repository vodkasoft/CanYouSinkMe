package com.vodkasoft.canyousinkme.game;

public class FBSession {
    private static String FacebookID = "";
    private static String Name;
    private static String CountryCode;

    public static String getCountryCode() {
        return CountryCode;
    }

    public static void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public static String getName() {
        return Name;
    }

    public static void setName(String name) {
        Name = name;
    }

    public static String getFacebookID() {
        return FacebookID;
    }

    public static void setFacebookID(String facebookID) {
        FacebookID = facebookID;
    }
}
