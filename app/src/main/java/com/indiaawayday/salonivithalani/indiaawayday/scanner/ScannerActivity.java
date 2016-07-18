package com.indiaawayday.salonivithalani.indiaawayday.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by bhupendrakumar on 7/17/16.
 */
public class ScannerActivity extends AppCompatActivity {

    private static final String TAG = "ScannerActivity";
    public static final int SCAN_RESULT_CODE = 1;
    private ScannerView mScannerView;

    public static final String SCAN_RESULT = "SCAN_RESULT";
    public static final String ERROR_INFO = "ERROR_INFO";
    public static final String SCAN_STATUS = "SCAN_STATUS";
    public static final int SCAN_OK = 1;
    public static final int SCAN_FAILED = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mScannerView = new ScannerView(this) {
            @Override
            public void onCameraNotAvailable() {
                cancelRequest();
            }

            @Override
            public void handleResult(Result result) {
                Intent intent = new Intent();
                intent.putExtra(SCAN_RESULT, result.getText());
                intent.putExtra(SCAN_STATUS,SCAN_OK);
                setResult(SCAN_RESULT_CODE, intent);
                mScannerView.stopCamera();
                finish();
            }
        };

        setContentView(mScannerView.getContentView());
    }


    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.pauseCamera();
    }

    public void cancelRequest() {
        Intent intent = new Intent();
        intent.putExtra(ERROR_INFO, "Camera unavailable");
        intent.putExtra(SCAN_STATUS, SCAN_FAILED);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
