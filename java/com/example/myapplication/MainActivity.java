package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView list_city;
    private Button button;
    private Button btn_add;
    private Button btn_search;
    private AutoCompleteTextView autoCompleteTextView;

    private CityOperator cityOperator;
    private List<City> cityList;
    private String cityName;
    CityAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        draw();
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (resultCode) {
//            case 1:
//                String string_city = data.getStringExtra("cityName");
//                if(nameExistInList(string_city)){
//                    break;
//                }
//                else if(!nameExistInList(string_city)){
//                    updateCurrentCity(string_city);
//                    draw();
//                }
//                break;
//            default:
//                break;
//        }
//
//    }

    private void bindView(){
        cityOperator = new CityOperator(this);
        list_city = findViewById(R.id.list_city);



        this.autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        Resources resources = getResources();
        String[] country = resources.getStringArray(R.array.city_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1,
                country
        );
        this.autoCompleteTextView.setAdapter(adapter);
        this.autoCompleteTextView.setOnClickListener(this);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        btn_search=findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

    }

    private void draw(){
        cityList = cityOperator.getItemCity();
        adapter = new CityAdapter(cityList,this);
        list_city.setAdapter(adapter);
    }

    private boolean nameExistInList(String name){
        for(int i = 1; i < cityList.size(); i ++){
            if(cityList.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }


    private void updateCurrentCity(String string_city){
        if(cityOperator.getIsExist(string_city) == 1){
            City city1 = cityOperator.getIsSelectCity();
            cityOperator.update(city1);
            City city2 = new City(string_city,"否");
            cityOperator.update(city2);
        }
        else if(cityOperator.getIsExist(string_city) == 0){
            City city1 = cityOperator.getIsSelectCity();
            cityOperator.update(city1);
            City city2 = new City(string_city, "是");
            cityOperator.add(city2);
        }
    }


    public void onClick(View v) {
        String string_city = autoCompleteTextView.getText().toString();
        switch (v.getId()){
            case R.id.btn_search:
                Resources resources = this.getResources();
                String[] country = resources.getStringArray(R.array.city_array);
                Log.d("TAG",country[0]);
                if(Arrays.asList(country).contains(string_city)){
                    Intent intent =new Intent(this,weather.class);
                    intent.putExtra("city_name",string_city);
                    startActivity(intent);
                    break;
                }
                else {
                    Toast toast = Toast.makeText(this,"找不到该城市",Toast.LENGTH_SHORT);
                    toast.show();}


                break;
            case R.id.btn_add:
                Log.d("TAG", string_city);;
                if(!string_city.equals(""))updateCurrentCity(string_city);
                draw();
                break;

            default:
        }
    }
}