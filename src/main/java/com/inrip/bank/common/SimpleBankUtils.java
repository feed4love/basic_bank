package com.inrip.bank.common;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SimpleBankUtils {

    public static String GenerateUUID(){
		return UUID.randomUUID().toString();
	}

    public static Date TransformDateIfExists(Date date, boolean truncate){        
        if(date==null) return null;
        Calendar cal = Calendar.getInstance();
		cal.setTime(date);
        if(truncate) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }        
        return cal.getTime();
    }
    public static Date getToday(boolean truncate){
        Calendar cal = Calendar.getInstance();
        if(truncate) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
        return cal.getTime();
    }

    public static Date getYesterday(boolean truncate){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        if(truncate) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
        return cal.getTime();
    }

    public static Date getTomorrow(boolean truncate){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        if(truncate) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
        return cal.getTime();
    }


}
