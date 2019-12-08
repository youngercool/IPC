package com.younger.younger.myhttpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("=====", Thread.currentThread().getName() + Thread.currentThread().getId());
        Log.e("=====", "===" + (Looper.myLooper() == Looper.getMainLooper()));
        tv_test = findViewById(R.id.tv_test);

        tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
        volleyM();
        OkHttpUtils.get().url("http://www.baidu.com").build().execute(new com.zhy.http.okhttp.callback.Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Request.Builder builder = new Request.Builder().url("https://square.github.io/okhttp/upgrading_to_okhttp_4/").method("GET", null);
        Request request = builder.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                tv_test.setText("fgdgfdgd");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("=====", "===" + (Looper.myLooper() == Looper.getMainLooper()));

            }
        });

    }

    private void volleyM() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.baidu.com";
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("==v===", "===" + (Looper.myLooper() == Looper.getMainLooper()));
                Log.e("==v===", Thread.currentThread().getName() + Thread.currentThread().getId());

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("==v===", "===" + (Looper.myLooper() == Looper.getMainLooper()));
                Log.e("==v===", Thread.currentThread().getName() + Thread.currentThread().getId());

            }
        });
        queue.add(stringRequest);
    }
}
