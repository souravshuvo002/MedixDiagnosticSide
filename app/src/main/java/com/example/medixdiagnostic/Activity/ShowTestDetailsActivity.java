package com.example.medixdiagnostic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.medixdiagnostic.Common.Common;
import com.example.medixdiagnostic.R;

public class ShowTestDetailsActivity extends AppCompatActivity {

    private TextView textViewEdit, textViewTestCategory, textViewTestName, textViewTestShortName, textViewShortDesc,
            textViewLongDesc,  textViewPrice, textViewDiscountPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_test_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Test Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // getting views
        textViewEdit = (TextView) findViewById(R.id.textViewEdit);
        textViewTestCategory = (TextView) findViewById(R.id.textViewTestCategory);
        textViewTestName = (TextView) findViewById(R.id.textViewTestName);
        textViewTestShortName = (TextView) findViewById(R.id.textViewTestShortName);
        textViewShortDesc = (TextView) findViewById(R.id.textViewShortDesc);
        textViewLongDesc = (TextView) findViewById(R.id.textViewLongDesc);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        textViewDiscountPrice = (TextView) findViewById(R.id.textViewDiscountPrice);


        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowTestDetailsActivity.this, UpdateTestActivity.class));
                finish();
            }
        });

        // load data
        loadData();
    }

    private void loadData() {
        textViewTestCategory.setText(Common.currentTestItem.getCat_name());
        textViewTestName.setText(Common.currentTestItem.getName());
        textViewTestShortName.setText(Common.currentTestItem.getShort_name());
        textViewShortDesc.setText(Common.currentTestItem.getShort_desc());
        textViewLongDesc.setText(Common.currentTestItem.getLong_desc());
        textViewPrice.setText(Common.currentTestItem.getPrice());
        textViewDiscountPrice.setText(Common.currentTestItem.getDiscount_price());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
