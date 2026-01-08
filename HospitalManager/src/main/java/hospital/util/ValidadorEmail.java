package hospital.util;

import java.util.regex.Pattern;

public class ValidadorEmail {

    private static final String EMAIL_REGEX = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
    
    public static boolean validar(String email) {

        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        email = email.trim();
        
       
        if (email.length() > 254) { 
            return false;
        }
        
        
        int countArroba = 0;
        for (char c : email.toCharArray()) {
            if (c == '@') countArroba++;
        }
        if (countArroba != 1) {
            return false;
        }
        

        return pattern.matcher(email).matches();
    }
    
  
}