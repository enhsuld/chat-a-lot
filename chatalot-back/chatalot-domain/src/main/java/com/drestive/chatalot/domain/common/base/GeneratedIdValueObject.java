package com.drestive.chatalot.domain.common.base;

import javax.persistence.*;

/**
 * Created by mustafa on 08/01/2016.
 */
@MappedSuperclass
public abstract class GeneratedIdValueObject implements ValueObject, Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
