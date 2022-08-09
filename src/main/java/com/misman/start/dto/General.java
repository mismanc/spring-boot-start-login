package com.misman.start.dto;

public class General {

    private Long id;
    private String name;
    private String value;

    public General() {
    }

    public General id(Long id){
        this.id = id;
        return this;
    }

    public General name(String name){
        this.name = name;
        return this;
    }

    public General value(String value){
        this.value = value;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
