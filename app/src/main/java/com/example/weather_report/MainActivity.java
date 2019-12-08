package com.example.weather_report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView temp,city,condition,wind,tempRange,date,f_d0,f_c0,f_t0,f_d1,f_c1,f_t1,f_d2,f_c2,f_t2;
    ImageView mainWeatherPic,f_p0,f_p1,f_p2;
    Button buttonCity;
    private String path;
    private String path_forecast;
    String image_url1 = "https://cdn.heweather.com/cond_icon/";
    String image_url2 = ".png";
    String image_url = "";
    String url1 = "https://free-api.heweather.net/s6/weather/now?location=";
    String url2 = "&key=20ad913688514668890a60a663f7e48e";
    String url3 = "https://free-api.heweather.net/s6/weather/forecast?location=";
    String detail = "";
    String detial_forecase = "";
    String location = "大兴";
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_city_weather);
        temp = findViewById(R.id.frag_city_temp);
        city = findViewById(R.id.frag_city_location);
        condition = findViewById(R.id.frag_city_weather);
        date = findViewById(R.id.frag_city_weekday);
        wind = findViewById(R.id.frag_city_wind);
        tempRange = findViewById(R.id.frag_temp_range);
        mainWeatherPic = findViewById(R.id.frag_weatherpic);
        f_c0 = findViewById(R.id.item_condition0);
        f_c1 = findViewById(R.id.item_condition1);
        f_c2 = findViewById(R.id.item_condition2);
        f_t0 = findViewById(R.id.item_range0);
        f_t1 = findViewById(R.id.item_range1);
        f_t2 = findViewById(R.id.item_range2);
        f_d0 = findViewById(R.id.item_date0);
        f_d1 = findViewById(R.id.item_date1);
        f_d2 = findViewById(R.id.item_date2);
        f_p0 = findViewById(R.id.item_pic0);
        f_p1 = findViewById(R.id.item_pic1);
        f_p2 = findViewById(R.id.item_pic2);
        buttonCity = findViewById(R.id.button_city);
        Intent intent = getIntent();
//            location = intent.getStringExtra("city");
        if (intent.getStringExtra("city") == null){

        }else{
            Log.d("mine",intent.getStringExtra("city"));
            location = intent.getStringExtra("city");
        }



        path = url1 + location + url2;
        path_forecast = url3 + location + url2;

        ThreadTime threadTime = new ThreadTime();
        threadTime.start();
        ThreadData threadData = new ThreadData();
        threadData.start();
        buttonCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"请选择城市",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, CitySelect.class);
                intent.putExtra("city", location);
                startActivity(intent);
            }
        });


    }



    public void loadImage (final String s, final ImageView v){
        new Thread(){
            public void run(){
                try {
                    byte[] data = GetData.getImage(s);
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    v.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();

    }

    private void ShowForecast (String s) {

        ForecastBean forecastBean = new Gson().fromJson(s,ForecastBean.class);
        ForecastBean.HeWeather6Bean heWeather6Bean = forecastBean.getHeWeather6().get(0);
        ForecastBean.HeWeather6Bean.DailyForecastBean daily_forecast0 = heWeather6Bean.getDaily_forecast().get(0);
        ForecastBean.HeWeather6Bean.DailyForecastBean daily_forecast1 = heWeather6Bean.getDaily_forecast().get(1);
        ForecastBean.HeWeather6Bean.DailyForecastBean daily_forecast2 = heWeather6Bean.getDaily_forecast().get(2);

        Log.d("mine", daily_forecast0.getTmp_max());
        image_url = image_url1 + daily_forecast0.getCond_code_d() + image_url2;
        loadImage(image_url,f_p0);
        image_url = image_url1 + daily_forecast1.getCond_code_d() + image_url2;
        loadImage(image_url,f_p1);
        image_url = image_url1 + daily_forecast2.getCond_code_d() + image_url2;
        loadImage(image_url,f_p2);

        tempRange.setText(daily_forecast0.getTmp_min() + "℃~" + daily_forecast0.getTmp_max() + "℃");
        f_t0.setText(daily_forecast0.getTmp_min() + "℃~" + daily_forecast0.getTmp_max() + "℃");
        f_t1.setText(daily_forecast1.getTmp_min() + "℃~" + daily_forecast1.getTmp_max() + "℃");
        f_t2.setText(daily_forecast2.getTmp_min() + "℃~" + daily_forecast2.getTmp_max() + "℃");
        if (daily_forecast0.getCond_code_d().equals(daily_forecast0.getCond_code_n())){
            f_c0.setText(daily_forecast0.getCond_txt_d());
        }else{
            f_c0.setText(daily_forecast0.getCond_txt_d() + "转" + daily_forecast0.getCond_txt_n());
        };
        if (daily_forecast1.getCond_code_d().equals(daily_forecast1.getCond_code_n())){
            f_c1.setText(daily_forecast1.getCond_txt_d());
        }else{
            f_c1.setText(daily_forecast1.getCond_txt_d() + "转" + daily_forecast1.getCond_txt_n());
        };
        Log.d("mine",daily_forecast2.getCond_code_d() + daily_forecast2.getCond_code_n());
        if (daily_forecast2.getCond_code_d().equals(daily_forecast2.getCond_code_n())){
            f_c2.setText(daily_forecast2.getCond_txt_d());
        }else{
            f_c2.setText(daily_forecast2.getCond_txt_d() + "转" + daily_forecast2.getCond_txt_n());
        };

    }

    public class ThreadTime extends Thread {
        public void run() {
            super.run();
            while (true) {
                mHandlertime.sendEmptyMessage(0x001);
                refreshMSGtime();//刷新UI
            }
        }
    }
    public class ThreadData extends Thread {
        public void run() {
            super.run();
            while (true) {
                try {
                    detail = GetData.getHtml(path);
                    detial_forecase = GetData.getHtml(path_forecast);
                    Log.d("mine", path);
                    Log.d("mine", detail);
                    Log.d("mine", detial_forecase);
                    mHandlertime.sendEmptyMessage(0x002);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refreshMSG();//刷新UI
            }
        }
    }

    //将i的值传给Handler，通知Handler刷新UI
    public void refreshMSG() {
        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {

        }


    }


    //将i的值传给Handler，通知Handler刷新UI
    public void refreshMSGtime() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }


    }

    //新建Handler对象。
    Handler mHandlertime = new Handler(){

        //handleMessage为处理消息的方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x001:{
                    Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
                    int year = calendar.get(Calendar.YEAR);
//月
                    int month = calendar.get(Calendar.MONTH)+1;
//日
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                hour = hour + 8;
//                if (hour > 24){
//                    hour = hour -24;
//                }
//分钟
                    int minute = calendar.get(Calendar.MINUTE);
//秒
                    int second = calendar.get(Calendar.SECOND);
                    date.setText(year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);
                    f_d0.setText(month+"月"+ day +"日");
                    f_d1.setText(month+"月"+ (day + 1) +"日");
                    f_d2.setText(month+"月"+ (day + 2) +"日");
                    break;
                }
                case 0x002:{
                    Log.d("mine", detail + "123");
                    parseShowData(detail);
                    ShowForecast(detial_forecase);
                    break;
                }
                default:
                    break;


            }
        }
    };


    private void parseShowData(String s) {
        WeatherBean weatherBean = new Gson().fromJson(s,WeatherBean.class);
        WeatherBean.HeWeather6Bean heWeather6Bean = weatherBean.getHeWeather6().get(0);
        WeatherBean.HeWeather6Bean.BasicBean basicBean = heWeather6Bean.getBasic();
        WeatherBean.HeWeather6Bean.NowBean nowBean= heWeather6Bean.getNow();
        WeatherBean.HeWeather6Bean.UpdateBean updateBean= heWeather6Bean.getUpdate();
        image_url = image_url1 + nowBean.getCond_code() + image_url2;
        loadImage(image_url,mainWeatherPic);
        temp.setText(nowBean.getTmp());
        city.setText(basicBean.getAdmin_area() + basicBean.getLocation());
        condition.setText(nowBean.getCond_txt());
        wind.setText(nowBean.getWind_dir()+nowBean.getWind_sc()+"级");
        Log.d("temp", nowBean.getWind_dir());

        }
    };

