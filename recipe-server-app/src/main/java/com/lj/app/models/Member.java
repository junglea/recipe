package com.lj.app.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="members")
public class Member {

    @Id
    private String id;

    private String name;

    public Member() {
        super();
    }

    public Member(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
}