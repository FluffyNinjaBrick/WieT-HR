package com.wiethr.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;

//@MappedSuperclass
@EqualsAndHashCode
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
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
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "Document_Signed_By",
            joinColumns = {@JoinColumn(name = "document")},
            inverseJoinColumns = {@JoinColumn(name = "signerId")}
    )
    private Employee signedBy;

    private LocalDate dateFrom;

    @Nullable
    private LocalDate dateTo;

    public Document(Employee employee, LocalDate dateIssued, LocalDate dateFrom, @Nullable LocalDate dateTo) {
        this.employee = employee;
        this.dateIssued = dateIssued;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void sign(Employee signer) {
        signedBy = signer;
        signed = true;
        dateSigned = LocalDate.now();
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
