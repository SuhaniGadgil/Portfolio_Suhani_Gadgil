package com.fit2081.suhani_fit2081_a3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
// Adapted from Chief Examiner’s A2 code provided in the A3 Starter Project
public class ListCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, FragmentListCategory.newInstance("d", "3s"))
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbarCommon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Categories");
    }

}