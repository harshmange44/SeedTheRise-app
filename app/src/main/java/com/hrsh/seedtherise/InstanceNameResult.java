package com.hrsh.seedtherise;

import com.google.gson.annotations.SerializedName;

public class InstanceNameResult {
    @SerializedName("name")
    private String name;

    public InstanceNameResult(String name) {
        this.name = name;
    }
    public String getInstName() {
        return name;
    }
}
