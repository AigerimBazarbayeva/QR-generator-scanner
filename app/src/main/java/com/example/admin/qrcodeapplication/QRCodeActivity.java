package com.example.admin.qrcodeapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeActivity extends AppCompatActivity {
    private EditText inputField;
    private Button qrButtonGenerate;
    private Button dataMatrixButtonGenerate;
    private Button qrButtonScan;
    private Button dataMatrixButtonScan;
    private ImageView codeImageView;

    final Activity activity = this;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        inputField = findViewById(R.id.inputField);
        qrButtonGenerate = findViewById(R.id.qrButtonGenerate);
        dataMatrixButtonGenerate = findViewById(R.id.dataMatrixButtonGenerate);
        qrButtonScan = findViewById(R.id.qrButtonScan);
        dataMatrixButtonScan = findViewById(R.id.dataMatrixButtonScan);
        codeImageView = findViewById(R.id.codeImageView);

        qrButtonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qrInput = inputField.getText().toString().trim();

                if (qrInput.length() > 0){
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try{
                        BitMatrix bitMatrix = multiFormatWriter.encode(qrInput,
                                BarcodeFormat.QR_CODE, 500, 500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        codeImageView.setImageBitmap(bitmap);
                    }catch (WriterException e){
                        e.printStackTrace();
                    }
                }else{
                    makeToast("Please enter text");
                }
            }
        });

        dataMatrixButtonGenerate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String dataMatrixInput = inputField.getText().toString().trim();
                if (dataMatrixInput.length() > 0){
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try{
                        BitMatrix bitMatrix = multiFormatWriter.encode(dataMatrixInput,
                                BarcodeFormat.DATA_MATRIX, 500, 500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        codeImageView.setImageBitmap(
                                Bitmap.createScaledBitmap(
                                        bitmap, 500, 500, false));
                    }catch (WriterException e){
                        e.printStackTrace();
                    }
                }else{
                    makeToast("Please enter text");
                }
            }
        });

        qrButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("Scanning QR code");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });

        dataMatrixButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.DATA_MATRIX_TYPES);
                intentIntegrator.setPrompt("Scanning Data Matrix code");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });
    }

    protected  void makeToast(String toastText){
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){

            if (result.getContents() == null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
