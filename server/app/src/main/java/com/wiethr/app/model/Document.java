package com.wiethr.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EqualsAndHashCode
@Data
public abstract class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    private String nameAtSigning;

    private Date dateIssued;

    private boolean signed;

    @Nullable
    private Date dateSigned;

    @Nullable
    @ManyToOne
    @JoinTable(
            name = "Document_Signed_By",
            joinColumns = {@JoinColumn(name = "document")},
            inverseJoinColumns = {@JoinColumn(name = "signerId")}
    )
    private Employee signedBy;

    private Date dateFrom;

    @Nullable
    private Date dateTo;


    public void sign(Employee employee) {
        signedBy = employee;
        signed = true;
    }

}
