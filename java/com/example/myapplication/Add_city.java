package com.example.myapplication;
import android.content.Intent;
import android.content.res.Resources;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Add_city extends AppCompatActivity implements View.OnClickListener{
    private Button btn_add;
    private Button btn_back;
    private AutoCompleteTextView autoCompleteTextView;
    private CityOperator cityOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_city);
        bindView();
    }

    private void bindView(){
        cityOperator = new CityOperator(this);
        this.autoCompleteTextView = this.findViewById(R.id.autoCompleteTextView);
        Resources resources = this.getResources();
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
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
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
}
