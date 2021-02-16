package com.quoteme.qmservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User extends BaseEntity {

    @Id
    @Column(name = "id")
    @Type(type="org.hibernate.type.UUIDCharType")
    UUID id;

    @Column(name = "firstname")
    String firstName;

    @Column(name = "secondname")
    String secondName;

    @Column(name = "email")
    String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    String password;

    @Column(name = "last_login_date")
    Long lastLoginDate;

    @Column(name = "deleted")
    boolean deleted;
}
