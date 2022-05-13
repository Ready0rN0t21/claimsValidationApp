package com.alliance.claimsvalidationapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "claim")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "claim_id")
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "billing_date", columnDefinition = "DATE")
    private String billing_date;

    @Column(name = "transactionNo", columnDefinition = "VARCHAR(255)")
    private String transactionNo;

    @Column(name = "filename", columnDefinition = "VARCHAR(255)")
    private String filename;

    @Column(name = "invoice", columnDefinition = "LONGBLOB")
    private byte[] invoice;

    @Column(name = "company", columnDefinition = "VARCHAR(255)")
    private String company;

    @Column(name = "net_amt", columnDefinition = "DECIMAL(10,2)", nullable = true)
    private Double net_amt;

    @Column(name = "commission", columnDefinition = "DECIMAL(4,2)", nullable = true)
    private Double commission;

    @Column(name = "release_date", columnDefinition = "DATE")
    private String release_date;

    @Column(name = "collection_date", columnDefinition = "DATE")
    private String collection_date;
}
