package com.indiaawayday.salonivithalani.indiaawayday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.indiaawayday.salonivithalani.indiaawayday.scanner.ScannerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanCode = (Button) findViewById(R.id.scan_code);
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivityForResult(intent, ScannerActivity.SCAN_RESULT_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ScannerActivity.SCAN_RESULT_CODE) {
            int scanStatus = data.getIntExtra(ScannerActivity.SCAN_STATUS, 0);
            if (scanStatus == ScannerActivity.SCAN_OK) {
                String scannedText = data.getStringExtra(ScannerActivity.SCAN_RESULT);
                Toast.makeText(MainActivity.this, scannedText, Toast.LENGTH_LONG).show();
            } else {
                String errorMsg = data.getStringExtra(ScannerActivity.ERROR_INFO);
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }

        }
    }
}
