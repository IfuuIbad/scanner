package com.ifuuibad.scanners;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{

    private TextView title, result;
    private Button btnStart;
    private ProgressBar progressBar;
    private CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // init view scan

        title = findViewById(R.id.txt_title);
        result = findViewById(R.id.txt_result);
        btnStart = findViewById(R.id.btn_start);
        progressBar = findViewById(R.id.progress_bar);
        cardView = findViewById(R.id.card_result);

        if ( getIntent().getExtras() != null ){
            Bundle bundle = getIntent().getExtras();
            if (bundle.getString("_result") != null){
                fan(BuildConfig.POST_URL, bundle.getString("_result"));
            }
        }
    }

    public void startScan(View view){
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        finish();
    }

    // Network
    public void fan(String url, String resultBarcode) {
        btnStart.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", resultBarcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            result.setText("status: " +response.getString("status") +"\n"
                                    +"message: " +response.getString("message"));
                            cardView.setVisibility(View.VISIBLE);
                            btnStart.setText("Scan Again");
                            btnStart.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("_error", error.getMessage());

                        builder.setTitle("error");
                        builder.setMessage("response empty");
                        AlertDialog alert1 = builder.create();
                        alert1.show();
                    }
                });
    }

}
