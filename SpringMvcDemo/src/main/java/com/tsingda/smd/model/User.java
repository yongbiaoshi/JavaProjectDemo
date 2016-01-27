package com.tsingda.smd.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class User {
    @NotBlank(groups = { ValidatorGroups.UserAdd.class }, message = "{user.add.name.NotBlank.message}")
    @Size(max = 50, min = 6, message = "{user.add.name.Size.message}")
    private String name;

    @Range(max = 100, min = 18, message = "{user.add.age.Range.message}")
    private int age;

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
