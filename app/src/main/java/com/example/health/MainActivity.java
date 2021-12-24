package com.example.health;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText m1;
    EditText m2;
    RequestQueue reqQueue;
    String ans = "";
    String url = "http://abashin.ru/cgi-bin/ru/tests/burnout";
    Integer len;
    String high = "соответствуют высокому уровню переутомления";
    String low = "соответствуют отсутствию";
    String normal = "соответствуют небольшому переутомлению";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m1 = (EditText) findViewById(R.id.ent1);
        m2 = (EditText) findViewById(R.id.ent2);
        reqQueue = Volley.newRequestQueue(this);

    }

    public void Click(View view) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url, response -> {ans = response.toString();
        }, error -> {ans = error.toString();}) {
            Integer len = 40 + m2.length() + m1.length();
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("day", "15");
                parameters.put("month", "12");
                parameters.put("year", "1990");
                parameters.put("sex", "1");
                parameters.put("m1", m1.getText().toString());
                parameters.put("m2", m2.getText().toString());

                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("Host", "abashin.ru");
                parameters.put("Connection", "close");
                parameters.put("Cache-Control", "max-age=0");
                parameters.put("DNT", "1");
                parameters.put("Upgrade-Insecure-Requests", "1");
                parameters.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                parameters.put("Accept-Encoding", "deflate");
                parameters.put("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
                parameters.put("Content-Type", "application/x-www-form-urlencoded");
                parameters.put("Content-Length", len.toString());

                return parameters;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String connect = null;
                try {
                    connect = new String(response.data, "UTF-8");
                    return Response.success(connect, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException ex) {
                    return Response.error(new ParseError(ex));
                }
            }
        };

        reqQueue.add(strReq);
        Toast.makeText(MainActivity.this, "ОТВЕТ:"+ans, Toast.LENGTH_LONG).show();

        if (ans.contains(high)) {
            Intent intent = new Intent(MainActivity.this, high_activity.class);
            startActivity(intent);
        }
        else if (ans.contains(normal)) {
            Intent intent = new Intent(MainActivity.this, normal_activity.class);
            startActivity(intent);
        }
        else if (ans.contains(low)) {
            Intent intent = new Intent(MainActivity.this, low_activity.class);
            startActivity(intent);
        }
        else if (ans.contains("Error")) {
            Toast.makeText(MainActivity.this, "ПРОИЗОШЛА ОШИБКА", Toast.LENGTH_LONG).show();
        }



    }

}