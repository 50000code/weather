package com.example.weather_report;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CitySelect extends AppCompatActivity implements AdapterView.OnItemClickListener{
    TextView nowCity;
    String city;
    String[] citys = {"北京","上海","深圳","杭州"};
    private List<Animal> mData = null;
    private Context mContext;
    private Adapter mAdapter = null;
    private ListView list_animal;
    private LinearLayout ly_content;
//    private int[] imgIds = new int[]{R.mipmap.head_icon1, R.mipmap.head_icon2, R.mipmap.head_icon3};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_select);
        nowCity = findViewById(R.id.now_city);
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        nowCity.setText(city);


        mContext = CitySelect.this;
        list_animal = (ListView) findViewById(R.id.list_test);
        //动态加载顶部View和底部View
        final LayoutInflater inflater = LayoutInflater.from(this);
        View headView = inflater.inflate(R.layout.view_header, null, false);
        View footView = inflater.inflate(R.layout.view_foot, null, false);

        mData = new LinkedList<Animal>();
        for (int i = 0; i < citys.length; i++){
            mData.add(new Animal(citys[i], "" ));
        }

        mAdapter = new Adapter((LinkedList<Animal>) mData, mContext);
        //添加表头和表尾需要写在setAdapter方法调用之前！！！
//        list_animal.addHeaderView(headView);
//        list_animal.addFooterView(footView);

        list_animal.setAdapter(mAdapter);
        list_animal.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext,"你点击了第" + position + "项",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CitySelect.this, MainActivity.class);
        intent.putExtra("city", citys[position]);
        startActivity(intent);
    }


//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(mContext,"你点击了第" + position + "项",Toast.LENGTH_SHORT).show();
//    }
}
