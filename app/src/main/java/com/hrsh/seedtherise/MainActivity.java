package com.hrsh.seedtherise;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    InstListAdapter instListAdapter;
//    Switch switchTheme;
//    boolean isDarkMode = false;
    private List<InstanceListByNameModel> instanceListByNameModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        UiModeManager uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);

//        if (uiManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
////            uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
//            isDarkMode = true;
//        } else {
////            uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
//            isDarkMode = false;
//        }

//        switchTheme = findViewById(R.id.switchTheme);
        recyclerView = findViewById(R.id.recyclerView);

//        switchTheme.setChecked(isDarkMode);
//        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            isDarkMode = isChecked;
//
//            if (isDarkMode) {
//                System.out.println("dark mode ON checked");
////                uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            } else {
//                System.out.println("dark mode OFF un-checked");
////                uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            }
//        });

        instanceListByNameModelArrayList = new ArrayList<>();

        instListAdapter = new InstListAdapter(this, instanceListByNameModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(instListAdapter);

        fetchInstanceNameListData(instanceListByNameModelArrayList, instListAdapter);
    }

    private void fetchInstanceNameListData(List<InstanceListByNameModel> instNameListModelList, InstListAdapter instListAdapter) {
        Call<List<InstanceNameResult>> call = RetrofitClient.getInstance().getMyApi().getInstanceNameData();
        call.enqueue(new Callback<List<InstanceNameResult>>() {
            @Override
            public void onResponse(Call<List<InstanceNameResult>> call, Response<List<InstanceNameResult>> response) {

                List<InstanceNameResult> instanceListResults = response.body();

                for (int i = 0; i < instanceListResults.size(); i++) {
                    instNameListModelList.add(new InstanceListByNameModel(
                            instanceListResults.get(i).getInstName(), instanceListResults.get(i).getInstName().substring(0, 1).toUpperCase()
                    ));
                }
                instListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<InstanceNameResult>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

}
