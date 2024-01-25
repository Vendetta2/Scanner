package com.skillet.scanner;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FOR_PERM = 21364789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView cumButt = findViewById(R.id.cam_butt);
        Button sendButt = findViewById(R.id.send_butt);
        TextInputLayout decodedTV = findViewById(R.id.decoded_text_tv);

        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};



        cumButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermissions(permission)){
                    scanCode();
                } else {
                    permissionLauncherMultiple.launch(permission);
                }

            }
        });


    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CamActivity.class);
        barLauncher.launch(options);
    }


    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
       if(result.getContents() != null){
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Result");
           builder.setMessage(result.getContents());
           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int which) {
                   dialogInterface.dismiss();
               }
           }).show();
       }
    });


    private boolean checkPermissions(String[] permission){
        for (String s : permission) {
            if (checkSelfPermission(s) != PackageManager.PERMISSION_GRANTED) {
                Log.d("checkPerms", "checkPermissions: " + checkSelfPermission(s) + " denied");
                return false;
            }
            Log.d("checkPerms", "checkPermissions: " + checkSelfPermission(s) + " granted");
        }
        return true;

    }


    private ActivityResultLauncher<String[]> permissionLauncherMultiple = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> o) {

                }
            }
    );


}