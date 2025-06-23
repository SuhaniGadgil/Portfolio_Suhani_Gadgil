package com.fit2081.suhani_fit2081_a3;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
// Adapted from Chief Examiner’s A2 code provided in the A3 Starter Project
public class ListEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, FragmentListEvent.newInstance("", ""))
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbarCommon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Events page");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}