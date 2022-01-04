package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.preference.PreferenceManager;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Request.HourlyWeatherForecast;
import com.example.myapplication.Request.LifestyleForecast;
import com.example.myapplication.Request.WeeklyWeatherForecast;

import com.google.gson.Gson;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class weather<PointValue> extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences pref;//定义一个SharedPreferences对象
    private SharedPreferences.Editor editor;

    private TextView day0, day1,day2,day3,day4,day5,day6;
    private TextView date0, date1,date2,date3,date4,date5,date6;
    private ImageView img_d0, img_d1,img_d2,img_d3,img_d4,img_d5,img_d6;
    private TextView cond_txt_d0, cond_txt_d1,cond_txt_d2,cond_txt_d3,cond_txt_d4,cond_txt_d5,cond_txt_d6;
    private ImageView img_n0, img_n1,img_n2,img_n3,img_n4,img_n5,img_n6;
    private TextView cond_txt_n0, cond_txt_n1,cond_txt_n2,cond_txt_n3,cond_txt_n4,cond_txt_n5,cond_txt_n6;
    private TextView wind_dir01, wind_dir11,wind_dir21,wind_dir31,wind_dir41,wind_dir51,wind_dir61;
    private TextView tmp_max0, tmp_max1,tmp_max2,tmp_max3,tmp_max4,tmp_max5,tmp_max6;
    private TextView tmp_min0, tmp_min1,tmp_min2,tmp_min3,tmp_min4,tmp_min5,tmp_min6;
    private TextView wind_sc01, wind_sc11,wind_sc21,wind_sc31,wind_sc41,wind_sc51,wind_sc61;
    private WeeklyWeatherForecast weeklyWeatherForecast;
    private volatile String string_weather_forcast;
    private TextView tv_updata;
    private float[] y1;
    private float[] y2;
    private float[] x;
    private List<PointValue> value1;
    private List<PointValue> value2;
    String string_city;
    Button delete;


    private TextView time0,time1,time2,time3,time4,time5,time6,time7;
    private TextView tmp0,tmp1,tmp2,tmp3,tmp4,tmp5,tmp6,tmp7;
    private TextView cond_txt0,cond_txt1,cond_txt2,cond_txt3,cond_txt4,cond_txt5,cond_txt6,cond_txt7;
    private TextView wind_dir0,wind_dir1,wind_dir2,wind_dir3,wind_dir4,wind_dir5,wind_dir6,wind_dir7;
    private TextView wind_sc0,wind_sc1,wind_sc2,wind_sc3,wind_sc4,wind_sc5,wind_sc6,wind_sc7;
    private TextView comf_type,comf_txt;
    private TextView drsg_type,drsg_txt;
    private TextView flu_type,flu_txt;
    private TextView sport_type,sport_txt;
    private TextView trav_type,trav_txt;
    private TextView uv_type,uv_txt;
    private TextView cw_type,cw_txt;
    private TextView air_type,air_txt;
    private TextView tv_update;
    private Button refresh;

    private HourlyWeatherForecast hourlyWeatherForecast;
    private LifestyleForecast lifestyleForecast;
    private volatile String string_hourly_weather_forcast;
    private volatile String string_Lifestyle_forcast;



    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    parseData();
                    break;
                default:
                    break;
            }
        };
    };



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);
        Intent intent=getIntent();
        string_city=intent.getStringExtra("city_name");
        bindView();
        pref= getSharedPreferences(string_city,MODE_PRIVATE);
        //pref= PreferenceManager.getDefaultSharedPreferences(this);
        String string_Lifestyle_forcast=pref.getString("string_Lifestyle_forcast","");
        String string_hourly_weather_forcast=pref.getString("string_hourly_weather_forcast","");
        String string_weather_forcast=pref.getString("string_weather_forcast","");
        editor=pref.edit();
        if(string_weather_forcast.equals("")||string_hourly_weather_forcast.equals("")||string_Lifestyle_forcast.equals("")){
            getData();
            editor.putString("string_Lifestyle_forcast",string_Lifestyle_forcast);
            editor.putString("string_hourly_weather_forcast",string_hourly_weather_forcast);
            editor.putString("string_weather_forcast",string_weather_forcast);
        }
        else{
            parseData();
            editor.clear();
        }
        editor.apply();

    }



    void bindView(){
        refresh=findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        tv_updata = findViewById(R.id.tv_update1);
        day0 = findViewById(R.id.day0);
        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        day6 = findViewById(R.id.day6);
        date0 = findViewById(R.id.date0);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        date3 = findViewById(R.id.date3);
        date4 = findViewById(R.id.date4);
        date5 = findViewById(R.id.date5);
        date6 = findViewById(R.id.date6);
        img_d0 = findViewById(R.id.img_d0);
        img_d1 = findViewById(R.id.img_d1);
        img_d2 = findViewById(R.id.img_d2);
        img_d3 = findViewById(R.id.img_d3);
        img_d4 = findViewById(R.id.img_d4);
        img_d5 = findViewById(R.id.img_d5);
        img_d6 = findViewById(R.id.img_d6);
        cond_txt_d0 = findViewById(R.id.cond_txt_d0);
        cond_txt_d1 = findViewById(R.id.cond_txt_d1);
        cond_txt_d2 = findViewById(R.id.cond_txt_d2);
        cond_txt_d3 = findViewById(R.id.cond_txt_d3);
        cond_txt_d4 = findViewById(R.id.cond_txt_d4);
        cond_txt_d5 = findViewById(R.id.cond_txt_d5);
        cond_txt_d6 = findViewById(R.id.cond_txt_d6);
        img_n0 = findViewById(R.id.img_n0);
        img_n1 = findViewById(R.id.img_n1);
        img_n2 = findViewById(R.id.img_n2);
        img_n3 = findViewById(R.id.img_n3);
        img_n4 = findViewById(R.id.img_n4);
        img_n5 = findViewById(R.id.img_n5);
        img_n6 = findViewById(R.id.img_n6);
        cond_txt_n0 = findViewById(R.id.cond_txt_n0);
        cond_txt_n1 = findViewById(R.id.cond_txt_n1);
        cond_txt_n2 = findViewById(R.id.cond_txt_n2);
        cond_txt_n3 = findViewById(R.id.cond_txt_n3);
        cond_txt_n4 = findViewById(R.id.cond_txt_n4);
        cond_txt_n5 = findViewById(R.id.cond_txt_n5);
        cond_txt_n6 = findViewById(R.id.cond_txt_n6);
        wind_dir01 = findViewById(R.id.wind_dir01);
        wind_dir11 = findViewById(R.id.wind_dir11);
        wind_dir21 = findViewById(R.id.wind_dir21);
        wind_dir31 = findViewById(R.id.wind_dir31);
        wind_dir41 = findViewById(R.id.wind_dir41);
        wind_dir51= findViewById(R.id.wind_dir51);
        wind_dir61 = findViewById(R.id.wind_dir61);
        wind_sc01 = findViewById(R.id.wind_sc01);
        wind_sc11 = findViewById(R.id.wind_sc11);
        wind_sc21 = findViewById(R.id.wind_sc21);
        wind_sc31 = findViewById(R.id.wind_sc31);
        wind_sc41 = findViewById(R.id.wind_sc41);
        wind_sc51 = findViewById(R.id.wind_sc51);
        wind_sc61 = findViewById(R.id.wind_sc61);
        tmp_max0 = findViewById(R.id.tmp_max0);
        tmp_max1 = findViewById(R.id.tmp_max1);
        tmp_max2 = findViewById(R.id.tmp_max2);
        tmp_max3 = findViewById(R.id.tmp_max3);
        tmp_max4 = findViewById(R.id.tmp_max4);
        tmp_max5 = findViewById(R.id.tmp_max5);
        tmp_max6 = findViewById(R.id.tmp_max6);
        tmp_min0 = findViewById(R.id.tmp_min0);
        tmp_min1 = findViewById(R.id.tmp_min1);
        tmp_min2 = findViewById(R.id.tmp_min2);
        tmp_min3 = findViewById(R.id.tmp_min3);
        tmp_min4 = findViewById(R.id.tmp_min4);
        tmp_min5 = findViewById(R.id.tmp_min5);
        tmp_min6 = findViewById(R.id.tmp_min6);


//        tv_update = findViewById(R.id.tv_update);
        time0 = findViewById(R.id.time0);
        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);
        time4 = findViewById(R.id.time4);
        time5 = findViewById(R.id.time5);
        time6 = findViewById(R.id.time6);
        time7 = findViewById(R.id.time7);

        tmp0 = findViewById(R.id.tmp0);
        tmp1 = findViewById(R.id.tmp1);
        tmp2 = findViewById(R.id.tmp2);
        tmp3 = findViewById(R.id.tmp3);
        tmp4 = findViewById(R.id.tmp4);
        tmp5 = findViewById(R.id.tmp5);
        tmp6 = findViewById(R.id.tmp6);
        tmp7 = findViewById(R.id.tmp7);

        cond_txt0 = findViewById(R.id.cond_txt0);
        cond_txt1 = findViewById(R.id.cond_txt1);
        cond_txt2 = findViewById(R.id.cond_txt2);
        cond_txt3 = findViewById(R.id.cond_txt3);
        cond_txt4 = findViewById(R.id.cond_txt4);
        cond_txt5 = findViewById(R.id.cond_txt5);
        cond_txt6 = findViewById(R.id.cond_txt6);
        cond_txt7 = findViewById(R.id.cond_txt7);

        wind_dir0 = findViewById(R.id.wind_dir0);
        wind_dir1 = findViewById(R.id.wind_dir1);
        wind_dir2 = findViewById(R.id.wind_dir2);
        wind_dir3 = findViewById(R.id.wind_dir3);
        wind_dir4 = findViewById(R.id.wind_dir4);
        wind_dir5 = findViewById(R.id.wind_dir5);
        wind_dir6 = findViewById(R.id.wind_dir6);
        wind_dir7 = findViewById(R.id.wind_dir7);

        wind_sc0 = findViewById(R.id.wind_sc0);
        wind_sc1 = findViewById(R.id.wind_sc1);
        wind_sc2 = findViewById(R.id.wind_sc2);
        wind_sc3 = findViewById(R.id.wind_sc3);
        wind_sc4 = findViewById(R.id.wind_sc4);
        wind_sc5 = findViewById(R.id.wind_sc5);
        wind_sc6 = findViewById(R.id.wind_sc6);
        wind_sc7 = findViewById(R.id.wind_sc7);

    }
    public void deleteCache(File[] files){

        boolean flag;
        for(File itemFile : files){
            flag = itemFile.delete();
            if (flag == false) {
                deleteCache(itemFile.listFiles());
            }
        }
        Toast.makeText(this, "清除缓存成功", Toast.LENGTH_SHORT).show();
    }

    void draw(){

        if(hourlyWeatherForecast == null || lifestyleForecast == null||weeklyWeatherForecast==null) return ;
        try{
            SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sd2 = new SimpleDateFormat("HH:mm");
            Date upd = sd1.parse(weeklyWeatherForecast.getHeWeather6().get(0).getUpdate().getLoc());
            tv_updata.setText(sd1.format(upd)+" 发布");

            SimpleDateFormat std1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat std2 = new SimpleDateFormat("EEEE");
            SimpleDateFormat std3 = new SimpleDateFormat("MM/dd");
            Date d;
            String string_date0 = weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(0).getDate();
            d = std1.parse(string_date0);
            date0.setText(std3.format(d));
            day0.setText("今天");
            String string_date1 = weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(1).getDate();
            d = std1.parse(string_date1);
            date1.setText(std3.format(d));
            day1.setText("明天");
            String string_date2 = weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(2).getDate();
            d = std1.parse(string_date2);
            date2.setText(std3.format(d));
            day2.setText("后天");
            String string_date3 = weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(3).getDate();
            d = std1.parse(string_date3);
            date3.setText(std3.format(d));
            day3.setText(std2.format(d));
            String string_date4 = weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(4).getDate();
            d = std1.parse(string_date4);
            date4.setText(std3.format(d));
            day4.setText(std2.format(d));
            String string_date5 = weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(5).getDate();
            d = std1.parse(string_date5);
            date5.setText(std3.format(d));
            day5.setText(std2.format(d));
            String string_date6 = weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(6).getDate();
            d = std1.parse(string_date6);
            date6.setText(std3.format(d));
            day6.setText(std2.format(d));

//            SimpleDateFormat sd11 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            SimpleDateFormat sd21 = new SimpleDateFormat("HH:mm");
//            Date upd1 = sd11.parse(hourlyWeatherForecast.getHeWeather6().get(0).getUpdate().getLoc());
//            tv_update.setText(sd21.format(upd1)+" 发布");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setImgView(img_d0,0,0);
        setImgView(img_d1,1,0);
        setImgView(img_d2,2,0);
        setImgView(img_d3,3,0);
        setImgView(img_d4,4,0);
        setImgView(img_d5,5,0);
        setImgView(img_d6,6,0);


        setImgView(img_n0,0,1);
        setImgView(img_n1,1,1);
        setImgView(img_n2,2,1);
        setImgView(img_n3,3,1);
        setImgView(img_n4,4,1);
        setImgView(img_n5,5,1);
        setImgView(img_n6,6,1);


        cond_txt_d0.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(0).getCond_txt_d());
        cond_txt_d1.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(1).getCond_txt_d());
        cond_txt_d2.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(2).getCond_txt_d());
        cond_txt_d3.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(3).getCond_txt_d());
        cond_txt_d4.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(4).getCond_txt_d());
        cond_txt_d5.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(5).getCond_txt_d());
        cond_txt_d6.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(6).getCond_txt_d());

        cond_txt_n0.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(0).getCond_txt_n());
        cond_txt_n1.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(1).getCond_txt_n());
        cond_txt_n2.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(2).getCond_txt_n());
        cond_txt_n3.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(3).getCond_txt_n());
        cond_txt_n4.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(4).getCond_txt_n());
        cond_txt_n5.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(5).getCond_txt_n());
        cond_txt_n6.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(6).getCond_txt_n());

        wind_dir01.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(0).getWind_dir());
        wind_dir11.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(1).getWind_dir());
        wind_dir21.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(2).getWind_dir());
        wind_dir31.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(3).getWind_dir());
        wind_dir41.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(4).getWind_dir());
        wind_dir51.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(5).getWind_dir());
        wind_dir61.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(6).getWind_dir());

        wind_sc01.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(0).getWind_sc()+"级");
        wind_sc11.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(1).getWind_sc()+"级");
        wind_sc21.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(2).getWind_sc()+"级");
        wind_sc31.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(3).getWind_sc()+"级");
        wind_sc41.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(4).getWind_sc()+"级");
        wind_sc51.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(5).getWind_sc()+"级");
        wind_sc61.setText(weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(6).getWind_sc()+"级");


        String tm;
        String[] s;
        tm = hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(0).getTime();
        s = tm.split(" ");
        time0.setText(s[1]);
        tm = hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(1).getTime();
        s = tm.split(" ");
        time1.setText(s[1]);
        tm = hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(2).getTime();
        s = tm.split(" ");
        time2.setText(s[1]);
        tm = hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(3).getTime();
        s = tm.split(" ");
        time3.setText(s[1]);
        tm = hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(4).getTime();
        s = tm.split(" ");
        time4.setText(s[1]);
        tm = hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(5).getTime();
        s = tm.split(" ");
        time5.setText(s[1]);
        tm = hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(6).getTime();
        s = tm.split(" ");
        time6.setText(s[1]);
        tm = hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(7).getTime();
        s = tm.split(" ");
        time7.setText(s[1]);

        String degree = "℃";

        tmp0.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(0).getTmp()+degree);
        tmp1.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(1).getTmp()+degree);
        tmp2.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(2).getTmp()+degree);
        tmp3.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(3).getTmp()+degree);
        tmp4.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(4).getTmp()+degree);
        tmp5.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(5).getTmp()+degree);
        tmp6.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(6).getTmp()+degree);
        tmp7.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(7).getTmp()+degree);

        cond_txt0.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(0).getCond_txt());
        cond_txt1.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(1).getCond_txt());
        cond_txt2.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(2).getCond_txt());
        cond_txt3.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(3).getCond_txt());
        cond_txt4.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(4).getCond_txt());
        cond_txt5.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(5).getCond_txt());
        cond_txt6.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(6).getCond_txt());
        cond_txt7.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(7).getCond_txt());

        wind_dir0.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(0).getWind_dir());
        wind_dir1.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(1).getWind_dir());
        wind_dir2.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(2).getWind_dir());
        wind_dir3.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(3).getWind_dir());
        wind_dir4.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(4).getWind_dir());
        wind_dir5.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(5).getWind_dir());
        wind_dir6.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(6).getWind_dir());
        wind_dir7.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(7).getWind_dir());

        wind_sc0.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(0).getWind_sc()+"级");
        wind_sc1.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(1).getWind_sc()+"级");
        wind_sc2.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(2).getWind_sc()+"级");
        wind_sc3.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(3).getWind_sc()+"级");
        wind_sc4.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(4).getWind_sc()+"级");
        wind_sc5.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(5).getWind_sc()+"级");
        wind_sc6.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(6).getWind_sc()+"级");
        wind_sc7.setText(hourlyWeatherForecast.getHeWeather6().get(0).getHourly().get(7).getWind_sc()+"级");

//        comf_type.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(0).getBrf());
//        comf_txt.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(0).getTxt());
//        drsg_type.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(1).getBrf());
//        drsg_txt.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(1).getTxt());
//        flu_type.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(2).getBrf());
//        flu_txt.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(2).getTxt());
//        sport_type.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(3).getBrf());
//        sport_txt.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(3).getTxt());
//        trav_type.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(4).getBrf());
//        trav_txt.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(4).getTxt());
//        uv_type.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(5).getBrf());
//        uv_txt.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(5).getTxt());
//        cw_type.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(6).getBrf());
//        cw_txt.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(6).getTxt());
//        air_type.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(7).getBrf());
//        air_txt.setText(lifestyleForecast.getHeWeather6().get(0).getLifestyle().get(7).getTxt());
    }

    void getData(){
        new Thread() {
            public void run() {
                try {
                    CityOperator cityOperator = new CityOperator(this);
                    Log.d("TAG", string_city);
                    String string_city1 = cityOperator.ChangeInfomat(string_city).toString2();

                    string_weather_forcast = GetData.getJson("https://free-api.heweather.com/s6/weather/forecast?location="
                            + string_city1
                            + "&key=2d7b37b322a04de1ab17fca5f2e0f0ea");
                    string_hourly_weather_forcast = GetData.getJson("https://free-api.heweather.com/s6/weather/hourly?location=" + string_city1 + "&key=2d7b37b322a04de1ab17fca5f2e0f0ea");
                    string_Lifestyle_forcast = GetData.getJson("https://free-api.heweather.com/s6/weather/lifestyle?location=" + string_city1 + "&key=2d7b37b322a04de1ab17fca5f2e0f0ea");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            };
        }.start();
    }

    void parseData(){
        Gson gson = new Gson();
        weeklyWeatherForecast = gson.fromJson(string_weather_forcast,WeeklyWeatherForecast.class);
        //Log.d("TAG1", weeklyWeatherForecast.toString());
        hourlyWeatherForecast = gson.fromJson(string_hourly_weather_forcast,HourlyWeatherForecast.class);
        lifestyleForecast = gson.fromJson(string_Lifestyle_forcast,LifestyleForecast.class);
        //Log.d("TAG2", string_hourly_weather_forcast);
        if(weeklyWeatherForecast != null)
            draw();
//
//        Log.d("TAG2", string_hourly_weather_forcast);
    }
    public int getResource(String imageName){
        Context ctx= Objects.requireNonNull(this).getBaseContext();
        return getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
    }
    private void setImgView(ImageView ig,int i, int flag){
        if(flag == 0){
            String img1 =  "x" + weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(i).getCond_code_d();
            ig.setImageResource(getResource(img1));
        }
        else{
            String img1 =  "x" + weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(i).getCond_code_n() + "n";
            String img2 =  "x" + weeklyWeatherForecast.getHeWeather6().get(0).getDaily_forecast().get(i).getCond_code_n();
            if(getResource(img1) == 0){
                ig.setImageResource(getResource(img2));
            }
            else{
                ig.setImageResource(getResource(img1));
            }
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                Log.d("TAG", "刷新 ");
                getData();
                break;
            default:
        }
    }
}
