package kore.ntnu.no.safespace.utils;

import kore.ntnu.no.safespace.data.User;

/**
 * The purpose of this class is to enable retrieval of fields used for different purposes.
 *
 * @author Kristoffer
 */
public class IdUtils {
    public static User CURRENT_USER;
    public final static String PROJECTS = "kore.ntnu.no.safespace.activities.ID.PROJECTS";

    public static final String USER = "kore.ntnu.no.safespace.activities.ID.USER";

    public static final String USERNAME = "kore.ntnu.no.safespace.activities.ID.USERNAME";
    public static final String PASSWORD = "kore.ntnu.no.safespace.activities.ID.PASSWORD";

    public static final String URL = "https://roberris-ss.uials.no:8080";

    public static final String REPORT = "kore.ntnu.safespace.activities.ID.REPORT";

    public static final String MAPS_LAT = "kore.ntnu.safespace.activities.MAPS.LAT";
    public static final String MAPS_LOG= "kore.ntnu.safespace.activities.MAPS.LOG";
    public static final String MAPS_RAD= "kore.ntnu.safespace.activities.MAPS.RAD";

    public static final int TAKE_PICTURE_REQUEST = 88;

    public static final int REQUEST_CODE = 0;
}
