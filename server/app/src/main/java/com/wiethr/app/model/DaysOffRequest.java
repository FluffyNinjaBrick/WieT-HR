package com.wiethr.app.model;

import com.wiethr.app.model.enums.LeaveType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class DaysOffRequest extends Document {

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;
}
