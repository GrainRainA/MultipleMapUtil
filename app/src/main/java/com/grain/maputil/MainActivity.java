package com.grain.maputil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.grain.map.MapView;
import com.grain.utils.InitUtilsModule;
import com.grain.utils.hint.L;
import com.grain.utils.hint.toast;
import com.grain.utils.utils.Permission.ApplyPermission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitUtilsModule.init(this);
        new ApplyPermission(this);

        MapView mapView = findViewById(R.id.mapView);
        new MapShowUtil(this, mapView);

    }
}
