package com.wiethr.app.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDaysOffDetails {

    List<EmployeeDaysOffDetails> employeeDaysOffDetails;
    int totalDaysOffUsed;
    int totalDaysOffLeft;

}
