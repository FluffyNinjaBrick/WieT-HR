package com.wiethr.app.model;

import lombok.*;

import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class DelegationRequest extends Document {

    private String destination;
}


