package com.hrsh.seedtherise;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MLInputObject {

    @SerializedName("air")
    private Integer air;

    @SerializedName("soil")
    private Integer soil;

    @SerializedName("ldr")
    private Integer ldr;

    @SerializedName("temperature")
    private Integer temperature;

    @SerializedName("humidity")
    private Integer humidity;

    @SerializedName("crop_name")
    private String cropName;

    public void setAir(Integer air) {
        this.air = air;
    }

    public void setSoil(Integer soil) {
        this.soil = soil;
    }

    public void setLdr(Integer ldr) {
        this.ldr = ldr;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public MLInputObject(Integer air, Integer soil, Integer ldr, Integer temperature, Integer humidity, String cropName) {
        this.air = air;
        this.soil = soil;
        this.ldr = ldr;
        this.temperature = temperature;
        this.humidity = humidity;
        this.cropName = cropName;
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
    public Integer getTemperatureSensorData() {
        return temperature;
    }
    public Integer getHumiditySensorData() {
        return humidity;
    }
    public String getCropName() {
        return cropName;
    }
}

