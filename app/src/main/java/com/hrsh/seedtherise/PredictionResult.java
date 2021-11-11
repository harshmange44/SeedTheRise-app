package com.hrsh.seedtherise;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PredictionResult {

    @SerializedName("health")
    private String health;

    public PredictionResult(String health) {
        this.health = health;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }
}

