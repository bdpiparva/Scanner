//package com.indiaawayday.salonivithalani.indiaawayday;
//
//import android.content.Intent;
//import android.graphics.Camera;
//import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.Result;
//
//import java.util.Arrays;
//
//import me.dm7.barcodescanner.zxing.ZXingScannerView;
//
//public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
//
//    private ZXingScannerView mScannerView;
//    public static final int SCAN_RESULT_CODE = 1;
//    public static final String SCAN_RESULT = "result";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mScannerView = new ZXingScannerView(this);
//        setContentView(mScannerView);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                scanQRCode();
//            }
//        }, 2000);
//    }
//
//    @Override
//    public void handleResult(Result result) {
//        Intent intent = new Intent();
//        intent.putExtra(SCAN_RESULT, result.getText());
//        setResult(SCAN_RESULT_CODE, intent);
//        mScannerView.stopCamera();
//        finish();
//    }
//
//    public void scanQRCode() {
//        mScannerView.setFormats(Arrays.asList(BarcodeFormat.QR_CODE));
//        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mScannerView.stopCamera();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mScannerView.clearAnimation();
//        mScannerView.setResultHandler(null);
//        mScannerView.stopCamera();
//        mScannerView = null;
//    }
//}
