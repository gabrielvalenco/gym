package app.gym.util;

import androidx.room.TypeConverter;
import java.util.Date;

/**
 * Conversor para transformar Date em Long e vice-versa para armazenamento no Room
 */
public class DateConverter {
    
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
    
    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }
}
