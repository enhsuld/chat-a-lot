package com.drestive.chatalot.domain.identity;

import com.drestive.chatalot.domain.common.base.UserSetIdValueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by mustafa on 02/01/2016.
 */
@Entity
@Table(name = "role")
public class Role extends UserSetIdValueObject {

    @Column(name = "name")
    protected String name;

    @Column(name = "description")
    protected String description;

    @ManyToMany(mappedBy="roles")
    protected List<User> users;

    public Role() {
    }

    public Role(String id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
