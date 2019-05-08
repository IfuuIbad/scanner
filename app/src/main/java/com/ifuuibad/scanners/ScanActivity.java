package com.ifuuibad.scanners;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQ_CAMERA = 125;
    private static final int TXT_CAMERA = 1;

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkPermission(TXT_CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermission(TXT_CAMERA);
        }

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView); // init view scan


        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Scaning");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private int checkPermission(int permission){

        int status = PackageManager.PERMISSION_DENIED;

        switch (permission){

            case TXT_CAMERA:
                status = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                break;

        }

        return status;
    }

    private void requestPermission(int permission){

        switch (permission){

            case TXT_CAMERA:
                ActivityCompat.requestPermissions(ScanActivity.this,
                        new String[]{ Manifest.permission.CAMERA }, REQ_CAMERA);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("_result", rawResult.getText());
        startActivity(intent);
        finish();

    }

}
