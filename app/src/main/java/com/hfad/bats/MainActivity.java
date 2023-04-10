package com.hfad.bats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    boolean signalVal = true;

    //XML wale keede idhar
    public void gordon(View view) {
        ImageButton bats = findViewById(R.id.signal);
        TextView signalStatus = findViewById(R.id.SignalStatus);

        String url = "https://clever-battledress-dove.cyclic.app/patchasignalvalue/6357e3e3d837d737e22d7ee4";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        signalStatus.setText("Signal Status: Updating..." );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myApp", "Error: " + error);
                    }
                });

        getRequestQueue().add(request);
    }

    //looping api requests

    private static RequestQueue mRequestQueue;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        ImageButton bats = findViewById(R.id.signal);
        bats.setImageResource(R.drawable.bmoff);
        TextView signalStatus = findViewById(R.id.SignalStatus);

        // Initialize the request queue
        mRequestQueue = Volley.newRequestQueue(this);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://clever-battledress-dove.cyclic.app/SignalValue", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray stuffArray = response.getJSONArray("stuff");
                            JSONObject stuffObject = stuffArray.getJSONObject(0);
                            boolean signalValue = stuffObject.getBoolean("signalValue");
                            signalVal = signalValue;
                            Log.d("myApp", "Signal Value: " + signalVal);
                            if (signalVal == true) {
                                bats.setImageResource(R.drawable.bmon);
                                signalStatus.setText("Signal Status: ON" );

                            } else if (signalVal == false) {
                                bats.setImageResource(R.drawable.bmoff);
                                signalStatus.setText("Signal Status: OFF" );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myApp", "onErrorResponse: " + error);
                    }
                });

                // Add the request to the queue
                mRequestQueue.add(jsonObjectRequest);
            }
        }, 0, 10000);

    }


    public static RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

}

/**
 *
 *

 public Runnable fetchData() {
 RequestQueue requestQueue = Volley.newRequestQueue(this);
 JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
 "https://clever-battledress-dove.cyclic.app/SignalValue",
 null,
 new Response.Listener<JSONObject>() {
@Override
public void onResponse(JSONObject response) {
try {
Log.d("myApp", "Valid Response Habibi! \n" + response.getString("stuff"));
} catch (JSONException e) {
e.printStackTrace();
}
}
}, new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
Log.d("myApp", "onErrorResponse: " + error);
}
});
 requestQueue.add(jsonObjectRequest);
 return null;
 }

 */