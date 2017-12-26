package com.example.administrator.jsonweather10;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonWeatherActivity extends AppCompatActivity implements Runnable{
    private String cityname="广州";
    JsonWeatherActivity activity;
    private String url1="http://wthrcdn.etouch.cn/weather_mini?city="+cityname;
    private EditText mCityname;
    private Button mSearch;
    private LinearLayout mShowTV;
    public JsonWeatherActivity(JsonWeatherActivity activity,String url){
        this.activity = activity;
        this.url1 = url;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_weather);
        mCityname = (EditText) findViewById(R.id.cityname);
        mSearch = (Button) findViewById(R.id.search);
        mShowTV = (LinearLayout) findViewById(R.id.show_weather);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowTV.removeAllViews();
                cityname = mCityname.getText().toString();
                Toast.makeText(JsonWeatherActivity.this,"正在查询天气...",Toast.LENGTH_LONG).show();
                Thread th =new Thread();
                th.start();

            }
        });
    }
    @Override
    public void run() {
        showWeather();
    }
    public void showWeather(){
        try{
            URL url = new URL(url1);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream din = httpURLConnection.getInputStream();
            InputStreamReader ins = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader buffer = new BufferedReader(ins);
            String inputLine = null;
            StringBuffer JsonData = new StringBuffer();
            while((inputLine = buffer.readLine()) != null){
                JsonData.append(inputLine);
            }
            String sJsonData = JsonData.toString();
            JSONObject jsonObject = new JSONObject(sJsonData);
            JSONObject cityweather = jsonObject.getJSONObject("data");
            StringBuffer weatherInfo = new StringBuffer();
            weatherInfo.append("温度："+cityweather.getString("high"));
            weatherInfo.append("天气提示："+cityweather.getString("ganmao"));
            Message message = new Message();
            message.what = 1;
            message.obj = weatherInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
