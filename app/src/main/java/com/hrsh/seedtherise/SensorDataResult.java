package com.hrsh.seedtherise;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SensorDataResult {

    @SerializedName("air")
    private Integer air;

    @SerializedName("soil")
    private Integer soil;

    @SerializedName("ldr")
    private Integer ldr;

    @SerializedName("temperature_humidity")
    private Integer temperature_humidity;

    public SensorDataResult(Integer air, Integer soil, Integer ldr, Integer temperature_humidity) {
        this.air = air;
        this.soil = soil;
        this.ldr = ldr;
        this.temperature_humidity = temperature_humidity;
    }

    public Integer getAirSensorData() {
        return air;
    }
    public Integer getSoilSensorData() {
        return soil;
    }
    public Integer getLDRSensorData() {
        return ldr;
    }
    public Integer getTemperatureAndMoistureSensorData() {
        return temperature_humidity;
    }
}

