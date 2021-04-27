package com.wiethr.app.repository;
import java.time.LocalDate;

public class Utilities {

    public static int getTotalWorkingDays(LocalDate from, LocalDate to){
        int totalWorkingDays = 0;
        LocalDate current = from;

        if(to.isBefore(from)){
            throw new IllegalArgumentException("Error: getTotalWorkingDays function wrong dates: from, to");
        }

        do {
            switch (current.getDayOfWeek()){
                case SATURDAY:
                case SUNDAY:
                    break;
                case FRIDAY:
                case MONDAY:
                case TUESDAY:
                case THURSDAY:
                case WEDNESDAY:
                    totalWorkingDays++;
                    break;
                default:
                    throw new IllegalArgumentException("Error: getTotalWorkingDays getDayOfWeek");
            }
            current = current.plusDays(1);
        } while(!current.isAfter(to));

        return totalWorkingDays;
    }
}
