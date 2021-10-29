package com.hrsh.seedtherise;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class InstanceResult {

    @SerializedName("name")
    private String name;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("sensor_data")
    private SensorDataResult sensorData;

    @SerializedName("sensor_data_array")
    private List<SensorDataResult> sensorDataArray;

    public InstanceResult(String name, String createdAt, String updatedAt, SensorDataResult sensorData, List<SensorDataResult> sensorDataArray) {
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sensorData = sensorData;
        this.sensorDataArray = sensorDataArray;
    }

    public String getInstanceName() {
        return name;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public SensorDataResult getSensorData() {
        return sensorData;
    }
    public List<SensorDataResult> getSensorDataArray() {
        return sensorDataArray;
    }

//    public int getAirSensorData() {
//        try {
//            return Integer.parseInt(sensorData.getString("air"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//    public int getSoilSensorData() {
//        try {
//            return Integer.parseInt(sensorData.getString("soil"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//    public int getLDRSensorData() {
//        try {
//            return Integer.parseInt(sensorData.getString("ldr"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//    public int getTemperatureAndMoistureSensorData() {
//        try {
//            return Integer.parseInt(sensorData.getString("temperature_humidity"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
}

