package com.hrsh.seedtherise;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropHealthActivity extends AppCompatActivity {

    String instanceName;
    Spinner cropNameSpinner;
    TextView currCropName, currCropStatus;
    ImageView cropStatusImgView;
    LinearLayout linearLayoutCropHealth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_health);

        instanceName = getIntent().getStringExtra("instName");
        cropNameSpinner = findViewById(R.id.cropNameSpinner);
        currCropName = findViewById(R.id.cropName);
        currCropStatus = findViewById(R.id.cropStatusTextView);
        cropStatusImgView = findViewById(R.id.cropStatusImageView);
        linearLayoutCropHealth = findViewById(R.id.linearCropHealth);

        String[] cropNames = getResources().getStringArray(R.array.cropNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CropHealthActivity.this, R.layout.support_simple_spinner_dropdown_item, cropNames);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cropNameSpinner.setAdapter(adapter);
        cropNameSpinner.setSelection(0);

        cropNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currCropName.setText(cropNames[position]);

                if(position==1 ||position==4 ||position==5){
                    currCropStatus.setText("Your Plant Health is Critical    :(");
                    cropStatusImgView.setImageResource(R.drawable.ic_baseline_mood_bad_24);
                    linearLayoutCropHealth.setBackgroundResource(R.drawable.shape_crop_bad_health);
                }else{
                    currCropStatus.setText("Your Plant is Healthy    :)");
                    cropStatusImgView.setImageResource(R.drawable.ic_baseline_mood_24);
                    linearLayoutCropHealth.setBackgroundResource(R.drawable.shape_crop_good_health);
                }
                fetchInstanceData(instanceName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchInstanceData(String instName) {
        Call<List<InstanceResult>> call = RetrofitClient.getInstance().getMyApi().getInstanceData(instName);
        call.enqueue(new Callback<List<InstanceResult>>() {
            @Override
            public void onResponse(Call<List<InstanceResult>> call, Response<List<InstanceResult>> response) {

                List<InstanceResult> instanceResults = response.body();

                Toast.makeText(CropHealthActivity.this, "Air: "+instanceResults.get(0).getSensorData().getAirSensorData() + " Soil: " + instanceResults.get(0).getSensorData().getSoilSensorData()
                        + " LDR: " + instanceResults.get(0).getSensorData().getLDRSensorData() + " Temp: " + instanceResults.get(0).getSensorData().getTemperatureAndMoistureSensorData(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<InstanceResult>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void fetchSensorData(String instName) {
        Call<SensorDataResult> call = RetrofitClient.getInstance().getMyApi().getSensorData(instName);
        call.enqueue(new Callback<SensorDataResult>() {
            @Override
            public void onResponse(Call<SensorDataResult> call, Response<SensorDataResult> response) {
//                System.out.println("DEBUG: response: "+response.body().toString());
//
                SensorDataResult sensorResults = response.body();

                System.out.println("DEBUG: sensor data: "+sensorResults.getAirSensorData() + " " + sensorResults.getSoilSensorData() + " "
                        + sensorResults.getLDRSensorData() + " " + sensorResults.getTemperatureAndMoistureSensorData());

            }

            @Override
            public void onFailure(Call<SensorDataResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

}
