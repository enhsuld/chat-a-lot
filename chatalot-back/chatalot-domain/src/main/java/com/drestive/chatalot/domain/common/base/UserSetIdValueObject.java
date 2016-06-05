package com.drestive.chatalot.domain.common.base;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by mustafa on 08/01/2016.
 */
@MappedSuperclass
public abstract class UserSetIdValueObject implements ValueObject, Identifiable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    protected UserSetIdValueObject() {
    }

    protected UserSetIdValueObject(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserSetIdValueObject that = (UserSetIdValueObject) o;

        return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
