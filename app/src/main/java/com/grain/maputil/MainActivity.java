package com.grain.maputil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.grain.map.MapView;
import com.grain.utils.MQTTManager;
import com.grain.utils.hint.L;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView mapView = findViewById(R.id.mapView);
        new MapShowUtil(this, mapView);

    }
}
