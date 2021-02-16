package com.quoteme.qmservice.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "creation_date")
    Long creationDate;

    @Column(name = "last_modification_date")
    Long lastModificationDate;

}
