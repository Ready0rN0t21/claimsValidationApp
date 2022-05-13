package com.alliance.claimsvalidationapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "lastName", columnDefinition = "VARCHAR(50)")
    private String lastName;

    @Column(name = "firstName", columnDefinition = "VARCHAR(50)")
    private String firstName;

    @Column(name = "email", columnDefinition = "VARCHAR(70)")
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(name = "usertype", columnDefinition = "VARCHAR(50)")
    private String usertype;

    @Column(name = "user_status", columnDefinition = "VARCHAR(50)")
    private String userStatus;

}
