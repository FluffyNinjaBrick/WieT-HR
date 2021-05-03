package com.wiethr.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;

//@MappedSuperclass
@EqualsAndHashCode
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Document {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "docGen")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    private String nameAtSigning;

    private LocalDate dateIssued;

    private boolean signed;

    @Nullable
    private LocalDate dateSigned;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Document_Signed_By",
            joinColumns = {@JoinColumn(name = "document")},
            inverseJoinColumns = {@JoinColumn(name = "signerId")}
    )
    private Employee signedBy;

    private LocalDate dateFrom;

    @Nullable
    private LocalDate dateTo;


    public void sign(Employee employee) {
        signedBy = employee;
        signed = true;
    }

    /**
     * NOTE: this method actually returns the employee's ID
     * @return the ID of the employee, to whom the document pertains
     */
    public long getEmployee() {
        return this.employee.getId();
    }

    public Employee employeeObject() {
        return this.employee;
    }

    public Long getSignedBy() {
        if (!signed) return null;
        else return this.signedBy.getId();
    }

    public Employee signedByObject() {
        return this.signedBy;
    }

}
