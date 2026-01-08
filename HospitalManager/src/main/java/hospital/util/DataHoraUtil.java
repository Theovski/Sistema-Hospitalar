package hospital.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataHoraUtil {
    
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public static String formatar(LocalDateTime dataHora) {
        return dataHora.format(FORMATTER);
    }
    
    public static LocalDateTime parse(String dataHoraStr) {
        return LocalDateTime.parse(dataHoraStr, FORMATTER);
    }
}