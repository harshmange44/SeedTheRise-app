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

    @SerializedName("temperature")
    private Integer temperature;

    @SerializedName("humidity")
    private Integer humidity;

    public SensorDataResult(Integer air, Integer soil, Integer ldr, Integer temperature, Integer humidity) {
        this.air = air;
        this.soil = soil;
        this.ldr = ldr;
        this.temperature = temperature;
        this.humidity = humidity;
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
}

