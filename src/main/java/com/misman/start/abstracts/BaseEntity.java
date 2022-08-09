package com.misman.start.abstracts;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Column(name = "created_on")
    protected LocalDateTime createdOn;

    @Column(name = "updated_on")
    protected LocalDateTime updatedOn;

    @Column(name = "deleted_on")
    protected LocalDateTime deletedOn;

    @PrePersist
    protected void onCreate() {
        this.createdOn = LocalDateTime.now();
        this.updatedOn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedOn = LocalDateTime.now();
    }



}
