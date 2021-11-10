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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.tensorflow.lite.Interpreter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphActivity extends AppCompatActivity {

    List<SensorDataResult> sensorDataArray = new ArrayList<>();
    LineChart chart;
    String instanceName;
    Spinner cropNameSpinner;
    List<Entry> entries;
    LineDataSet dataSet;
    Description description;
    String currSensorAttribute = "Temperature";
    int sensorDataArraySize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        chart = (LineChart) findViewById(R.id.chart);

        chart.animateY(3000, Easing.EaseInOutCirc);
        description = chart.getDescription();
        chart.setNoDataText("Insufficient  Data");
        description.setText("Temperature --> Sample Number");
        cropNameSpinner = findViewById(R.id.sensorAttrSpinner);
        instanceName = getIntent().getStringExtra("instName");
        fetchInstanceData(instanceName, sensorDataArray);

        entries = new ArrayList<Entry>();

        String[] sensorAttrNames = getResources().getStringArray(R.array.sensorNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(GraphActivity.this, R.layout.support_simple_spinner_dropdown_item, sensorAttrNames);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cropNameSpinner.setAdapter(adapter);
        cropNameSpinner.setSelection(0);

        cropNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currSensorAttribute = sensorAttrNames[position];
                entries.clear();

                if(sensorDataArray.size() >= 15) {
                    for (int i = 0; i < 15; i++) {
                        if (currSensorAttribute.equals("Air")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getAirSensorData()));
                        } else if (currSensorAttribute.equals("Humidity")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getHumiditySensorData()));
                        } else if (currSensorAttribute.equals("Soil")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getSoilSensorData()));
                        } else if (currSensorAttribute.equals("Temperature")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getTemperatureSensorData()));
                        } else if (currSensorAttribute.equals("Light")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getLDRSensorData()));
                        }
                    }
                }else if(sensorDataArray.size() >= 10){
                    for (int i = 0; i < 10; i++) {
                        if (currSensorAttribute.equals("Air")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getAirSensorData()));
                        } else if (currSensorAttribute.equals("Humidity")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getHumiditySensorData()));
                        } else if (currSensorAttribute.equals("Soil")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getSoilSensorData()));
                        } else if (currSensorAttribute.equals("Temperature")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getTemperatureSensorData()));
                        } else if (currSensorAttribute.equals("Light")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getLDRSensorData()));
                        }
                    }
                }else{
                    Toast.makeText(GraphActivity.this, "No enough data...", Toast.LENGTH_LONG).show();
                }

                dataSet = new LineDataSet(entries, currSensorAttribute);
                LineData lineData = new LineData(dataSet);
                description.setText(currSensorAttribute + " --> Sample Number");
                chart.setData(lineData);
                chart.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void fetchInstanceData(String instName, List<SensorDataResult> sensorDataArray) {
        Call<List<InstanceResult>> call = RetrofitClient.getInstance().getMyApi().getInstanceData(instName);
        call.enqueue(new Callback<List<InstanceResult>>() {
            @Override
            public void onResponse(Call<List<InstanceResult>> call, Response<List<InstanceResult>> response) {

                List<InstanceResult> instanceResults = response.body();

                if (instanceResults != null) {
                    sensorDataArray.addAll(instanceResults.get(0).getSensorDataArray());
                }
                sensorDataArraySize = sensorDataArray.size();

                if(sensorDataArray.size() >= 15) {
                    for (int i = 0; i < 15; i++) {
                        if (currSensorAttribute.equals("Temperature")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getTemperatureSensorData()));
                        }
                    }
                }else if(sensorDataArray.size() >= 10){
                    for (int i = 0; i < 10; i++) {
                        if (currSensorAttribute.equals("Temperature")) {
                            entries.add(new Entry((i + 1), sensorDataArray.get(sensorDataArraySize - i - 1).getTemperatureSensorData()));
                        }
                    }
                }else{
                    Toast.makeText(GraphActivity.this, "No enough data...", Toast.LENGTH_LONG).show();
                }

                LineDataSet dataSet = new LineDataSet(entries, currSensorAttribute);
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);
                chart.invalidate();
            }

            @Override
            public void onFailure(Call<List<InstanceResult>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

}
