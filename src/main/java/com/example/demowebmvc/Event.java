package com.example.demowebmvc;
import com.sun.istack.internal.NotNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Event {

    interface ValidateLimit{}
    interface ValidateName{}

    private Integer id;

    @NotBlank
    private String name;

    @Min(0)
    private Integer limit;

    private String coment;

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
