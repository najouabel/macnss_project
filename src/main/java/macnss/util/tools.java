package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tools {


    // Try to parse a string to an integer, return 0 if it's not a valid integer
    public static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Check if an email has a valid format
    // Return true if the email is valid, false if not
    public static boolean isValidEmailFormat(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    // Check if a password is valid
    public static boolean isValidPassword(String password) {
        String regex = "^(?=\\S+$).{8,}$";
        // Must have 8 characters or more
        // No whitespaces allowed
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
    public static double percentageStringToDouble(String percentage) {
        try {
            // Remove the '%' character from the percentage string
            String cleanedPercentage = percentage.replaceAll("%", "");

            // Parse the cleaned string to a double and divide by 100
            double result = Double.parseDouble(cleanedPercentage) / 100.0;

            return result;
        } catch (NumberFormatException e) {
            // Handle invalid input, such as non-numeric or improperly formatted strings
            throw new IllegalArgumentException("Invalid percentage string: " + percentage);
        }
    }
}
