package com.groceryapp.Models;

import com.google.gson.annotations.SerializedName;

public class All_categories_model {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
