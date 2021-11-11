package com.hrsh.seedtherise;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.core.graphics.ColorUtils;

import com.ramijemli.percentagechartview.PercentageChartView;
import com.ramijemli.percentagechartview.callback.AdaptiveColorProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropHealthActivity extends AppCompatActivity {

    String instanceName;
    Spinner cropNameSpinner;
    TextView currCropName, currCropStatus;
    ImageView cropStatusImgView, graphImgView;
    LinearLayout linearLayoutCropHealth;
    SensorDataResult sensorData;
    PercentageChartView percentageChartView;

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
        percentageChartView = findViewById(R.id.healthChart);
        graphImgView = findViewById(R.id.graphImgView);

        sensorData = new SensorDataResult(0, 0, 0, 0, 0);
        fetchInstanceData(instanceName, sensorData);

        percentageChartView.textColor(Color.BLACK)
                .textSize(50)
                .typeface(Typeface.SANS_SERIF)
                .textShadow(Color.WHITE, 2f, 2f, 2f)
                .backgroundColor(Color.BLACK)
                .apply();

        AdaptiveColorProvider colorProvider = new AdaptiveColorProvider() {
            @Override
            public int provideProgressColor(float progress) {
                if (progress <= 20)
                    return Color.parseColor("#EA0C0C");
                else if (progress <= 40)
                    return Color.parseColor("#DA2626");
                else if (progress <= 50)
                    return Color.parseColor("#CCBD3C");
                else if (progress <= 60)
                    return Color.parseColor("#EAD313");
                else if (progress <= 80)
                    return Color.parseColor("#65C869");
                else return Color.parseColor("#1BB822");
            }

            @Override
            public int provideBackgroundColor(float progress) {
                return ColorUtils.blendARGB(provideProgressColor(progress), Color.BLACK, .8f);
            }

            @Override
            public int provideTextColor(float progress) {
                return provideProgressColor(progress);
            }

            @Override
            public int provideBackgroundBarColor(float progress) {
                return ColorUtils.blendARGB(provideProgressColor(progress), Color.BLACK, .5f);
            }
        };
        percentageChartView.setAdaptiveColorProvider(colorProvider);

        String[] cropNames = getResources().getStringArray(R.array.cropNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(CropHealthActivity.this, R.layout.support_simple_spinner_dropdown_item, cropNames);
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

                MLInputObject mlInputObject = new MLInputObject(sensorData.getAirSensorData(), sensorData.getSoilSensorData()
                        , sensorData.getLDRSensorData(), sensorData.getTemperatureSensorData(), sensorData.getHumiditySensorData()
                        , cropNames[position].toLowerCase());
                predictAndInitCropHealth(mlInputObject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void predictAndInitCropHealth(MLInputObject mlInputObject) {

        Call<PredictionResult> call = MLRetrofitClient.getInstance().getMyApi().sendDataForPrediction(mlInputObject);
        call.enqueue(new Callback<PredictionResult>() {
            @Override
            public void onResponse(Call<PredictionResult> call, Response<PredictionResult> response) {

                PredictionResult prediction = response.body();
                float healthPercentage = Float.parseFloat(prediction.getHealth().substring(0, 5))*10;
                percentageChartView.setProgress(healthPercentage, true);

                if(healthPercentage <= 40.0){
                    currCropStatus.setText("Your Plant Health is Critical    :(");
                    cropStatusImgView.setImageResource(R.drawable.ic_baseline_mood_bad_24);
                    linearLayoutCropHealth.setBackgroundResource(R.drawable.shape_crop_bad_health);
                }else if(healthPercentage <= 60.0){
                    currCropStatus.setText("Your Plant Health is Neutral    :|");
                    cropStatusImgView.setImageResource(R.drawable.ic_baseline_mood_24);
                    linearLayoutCropHealth.setBackgroundResource(R.drawable.shape_crop_neutral_health);
                }else{
                    currCropStatus.setText("Your Plant is Healthy    :)");
                    cropStatusImgView.setImageResource(R.drawable.ic_baseline_mood_24);
                    linearLayoutCropHealth.setBackgroundResource(R.drawable.shape_crop_good_health);
                }
            }

            @Override
            public void onFailure(Call<PredictionResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured. "+t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
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
//                Toast.makeText(CropHealthActivity.this, "Air: "+instanceResults.get(0).getSensorData().getAirSensorData() + " Soil: " + instanceResults.get(0).getSensorData().getSoilSensorData()
//                        + " LDR: " + instanceResults.get(0).getSensorData().getLDRSensorData() + " Temp: " + instanceResults.get(0).getSensorData().getTemperatureSensorData()+ " Humidity: " + instanceResults.get(0).getSensorData().getHumiditySensorData(), Toast.LENGTH_LONG).show();
                MLInputObject mlInputObject = new MLInputObject(sensorDataResult.getAirSensorData(), sensorDataResult.getSoilSensorData()
                        , sensorDataResult.getLDRSensorData(), sensorDataResult.getTemperatureSensorData(), sensorDataResult.getHumiditySensorData()
                        , "apple");
                predictAndInitCropHealth(mlInputObject);
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
                SensorDataResult sensorResults = response.body();
            }

            @Override
            public void onFailure(Call<SensorDataResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

}
