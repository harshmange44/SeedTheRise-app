package com.hrsh.seedtherise;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
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

import com.google.android.material.snackbar.Snackbar;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropHealthActivity extends AppCompatActivity {

    String instanceName;
    Spinner cropNameSpinner;
    TextView currCropName, currCropStatus;
    ImageView cropStatusImgView, graphImgView;
    LinearLayout linearLayoutCropHealth;
    Interpreter tflite;
    SensorDataResult sensorData;

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
        graphImgView = findViewById(R.id.graphImgView);

        sensorData = new SensorDataResult(0, 0, 0, 0, 0);
        fetchInstanceData(instanceName, sensorData);

        try {
            tflite = new Interpreter(loadModelFile());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        String[] cropNames = getResources().getStringArray(R.array.cropNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CropHealthActivity.this, R.layout.support_simple_spinner_dropdown_item, cropNames);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cropNameSpinner.setAdapter(adapter);
        cropNameSpinner.setSelection(0);

        graphImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent graphActivity = new Intent(CropHealthActivity.this, GraphActivity.class);
                graphActivity.putExtra("instName", instanceName);
                startActivity(graphActivity);
            }
        });

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
                String prediction=doInference(sensorData.getTemperatureSensorData(), sensorData.getHumiditySensorData());
                Toast.makeText(CropHealthActivity.this, "Prediction: "+prediction, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String doInference(int temp, int humidity) {
        float[] input_temp = {temp};
        float[] input_humidity = {humidity};
        float[][] inputVal = {input_temp, input_humidity};
        float[] input=new float[2];
        input[0] = Float.parseFloat(String.valueOf("27"));
        input[1] = Float.parseFloat(String.valueOf("90"));
        float[][] output=new float[1][1];
        Map<Integer, Object> outputs = new HashMap<>();
//        outputs.put(0, output);
        tflite.run(input,output);
        return String.valueOf(output[0][0]);
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor=this.getAssets().openFd("linear.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset=fileDescriptor.getStartOffset();
        long declareLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declareLength);
    }

    private void fetchInstanceData(String instName, SensorDataResult sensorDataResult) {
        Call<List<InstanceResult>> call = RetrofitClient.getInstance().getMyApi().getInstanceData(instName);
        call.enqueue(new Callback<List<InstanceResult>>() {
            @Override
            public void onResponse(Call<List<InstanceResult>> call, Response<List<InstanceResult>> response) {

                List<InstanceResult> instanceResults = response.body();

                sensorDataResult.setAir(instanceResults.get(0).getSensorData().getAirSensorData());
                sensorDataResult.setSoil(instanceResults.get(0).getSensorData().getSoilSensorData());
                sensorDataResult.setLdr(instanceResults.get(0).getSensorData().getLDRSensorData());
                sensorDataResult.setTemperature(instanceResults.get(0).getSensorData().getTemperatureSensorData());
                sensorDataResult.setHumidity(instanceResults.get(0).getSensorData().getHumiditySensorData());
                Toast.makeText(CropHealthActivity.this, "Air: "+instanceResults.get(0).getSensorData().getAirSensorData() + " Soil: " + instanceResults.get(0).getSensorData().getSoilSensorData()
                        + " LDR: " + instanceResults.get(0).getSensorData().getLDRSensorData() + " Temp: " + instanceResults.get(0).getSensorData().getTemperatureSensorData()+ " Humidity: " + instanceResults.get(0).getSensorData().getHumiditySensorData(), Toast.LENGTH_LONG).show();
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
                        + sensorResults.getLDRSensorData() + " " + sensorResults.getTemperatureSensorData() + " " + sensorResults.getHumiditySensorData());

            }

            @Override
            public void onFailure(Call<SensorDataResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

}
