package com.example.oi156f.bakeboss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
