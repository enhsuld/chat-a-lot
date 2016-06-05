package com.drestive.chatalot.domain.common.base;

import com.drestive.chatalot.domain.identity.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mustafa on 07/01/2016.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Entity {

    @Id
    @GeneratedValue
    private String id;

    @Column(name = "created_on")
    protected Date createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by_id")
    protected User createdBy;

    @Column(name = "modified_on")
    protected Date modifiedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by_id")
    protected User modifiedBy;


    public AbstractEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @PrePersist
    protected void persist(){
        setCreatedOn(new Date());
    }

    @PreUpdate
    protected void update(){
        setModifiedOn(new Date());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AbstractEntity))
            return false;

        AbstractEntity that = (AbstractEntity) o;

        return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
