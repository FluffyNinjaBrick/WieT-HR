package com.wiethr.app.repository;
import com.wiethr.app.model.DaysOffRequest;
import com.wiethr.app.model.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<? extends Document> getDocumentsBetweenDates(ArrayList<? extends Document> requests, long id, LocalDate from, LocalDate to){
        return requests.stream().filter(
                req -> {
                    if (req.getEmployee().getId() != id) return false;
                    if (req.getDateTo() != null){
                        return !req.getDateTo().isBefore(from) && !req.getDateFrom().isAfter(to);
                    } else return !req.getDateFrom().isAfter(to);
                })
                .sorted(Comparator.comparing(Document::getDateFrom).reversed())
                .collect(Collectors.toList());
    }

}
