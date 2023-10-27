package com.example.medixdiagnostic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medixdiagnostic.Api.ApiClient;
import com.example.medixdiagnostic.Api.ApiService;
import com.example.medixdiagnostic.Common.Common;
import com.example.medixdiagnostic.Model.Category;
import com.example.medixdiagnostic.Model.Result;
import com.example.medixdiagnostic.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpdateTestActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private TextInputEditText editTextName, editTextShortName, editTextShortDesc, editTextLongDesc, editTextPrice;
    private TextInputEditText editTextItemDiscountPrice;
    private TextView textViewStartDate, textViewEndDate;
    private Button btn_UpdateItem;
    private LinearLayout linearLayDiscount, linearLayExpand;
    private RadioGroup radioStatusType;


    private List<Category> categoryList;
    private String cat_id, test_id, status;
    String accDate, cYear, cMonth;

    private String dis_start_date, dis_end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Test Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // getting views
        spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        linearLayDiscount = (LinearLayout) findViewById(R.id.linearLayDiscount);
        linearLayExpand = (LinearLayout) findViewById(R.id.linearLayExpand);
        editTextName = (TextInputEditText) findViewById(R.id.editTextItemName);
        editTextShortName = (TextInputEditText) findViewById(R.id.editTextItemShortName);
        editTextShortDesc = (TextInputEditText) findViewById(R.id.editTextItemShortDesc);
        editTextLongDesc = (TextInputEditText) findViewById(R.id.editTextItemLongDesc);
        editTextPrice = (TextInputEditText) findViewById(R.id.editTextItemPrice);
        radioStatusType = (RadioGroup) findViewById(R.id.radioStatusType);
        editTextItemDiscountPrice = (TextInputEditText) findViewById(R.id.editTextItemDiscountPrice);
        textViewStartDate = (TextView) findViewById(R.id.textViewStartDate);
        textViewEndDate = (TextView) findViewById(R.id.textViewEndDate); 
        btn_UpdateItem = (Button) findViewById(R.id.btn_UpdateItem);

        btn_UpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextName.getText().toString()) ||
                        TextUtils.isEmpty(editTextShortName.getText().toString()) ||
                        TextUtils.isEmpty(editTextShortDesc.getText().toString()) ||
                        TextUtils.isEmpty(editTextLongDesc.getText().toString()) ||
                        TextUtils.isEmpty(editTextPrice.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Some of fields is empty", Toast.LENGTH_LONG).show();
                } else {
                    updateTestItem();
                }
            }
        });

        linearLayDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linearLayExpand.getVisibility() == View.VISIBLE)
                {
                    linearLayExpand.setVisibility(View.GONE);
                }
                else
                {
                    linearLayExpand.setVisibility(View.VISIBLE);
                }
            }
        });

        textViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(UpdateTestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date, day = null, month = null;

                        if(dayOfMonth < 10)
                        {
                            day = "0" + String.valueOf(dayOfMonth);
                        }
                        else
                        {
                            day = String.valueOf(dayOfMonth);
                        }
                        if(monthOfYear+1 < 10)
                        {
                            month = "0" + String.valueOf(monthOfYear+1);
                        }
                        else
                        {
                            month = String.valueOf(monthOfYear+1);
                        }

                        cYear = String.valueOf(year);
                        cMonth = String .valueOf(month);

                        date =  String.valueOf(year) + "-" + month + "-" + day;

                        dis_end_date = date;

                        textViewEndDate.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });

        textViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(UpdateTestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date, day = null, month = null;

                        if(dayOfMonth < 10)
                        {
                            day = "0" + String.valueOf(dayOfMonth);
                        }
                        else
                        {
                            day = String.valueOf(dayOfMonth);
                        }
                        if(monthOfYear+1 < 10)
                        {
                            month = "0" + String.valueOf(monthOfYear+1);
                        }
                        else
                        {
                            month = String.valueOf(monthOfYear+1);
                        }

                        cYear = String.valueOf(year);
                        cMonth = String .valueOf(month);

                        date =  String.valueOf(year) + "-" + month + "-" + day;

                        dis_start_date = date;

                        textViewStartDate.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });

        radioStatusType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton)findViewById(checkedId);
                int index = radioStatusType.indexOfChild(rb);
                switch (index) {
                    case 0: // first button - Active
                        status = "1";
                        break;
                    case 1: // second button - In active
                        status = "0";
                        break;

                }
            }
        });

        // load test data
        loadTestData();

        // load Categories
        loadCategorySpinner();
    }

    private void loadTestData() {

        if(Common.currentTestItem.getStatus().equalsIgnoreCase("1"))
        {
            radioStatusType.check(radioStatusType.getChildAt(0).getId());
        }
        else
        {
            radioStatusType.check(radioStatusType.getChildAt(01).getId());
        }

        editTextName.setText(Common.currentTestItem.getName());
        editTextShortName.setText(Common.currentTestItem.getShort_name());

        editTextLongDesc.setText(Common.currentTestItem.getLong_desc());
        editTextShortDesc.setText(Common.currentTestItem.getShort_desc());

        editTextPrice.setText(Common.currentTestItem.getPrice());
        editTextItemDiscountPrice.setText(Common.currentTestItem.getDiscount_price());


        if(Common.currentTestItem.getDis_start_date().equalsIgnoreCase("0000-00-00 00:00:00") &&
                Common.currentTestItem.getDis_end_date().equalsIgnoreCase("0000-00-00 00:00:00"))
        {
            // get current date and time
            //getting current date and time using Date class
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateobj = new Date();
            textViewStartDate.setText(df.format(dateobj));
            textViewEndDate.setText(df.format(dateobj));
            dis_start_date = df.format(dateobj);
            dis_end_date = df.format(dateobj);

        }
        else
        {
            textViewStartDate.setText(Common.currentTestItem.getDis_start_date());
            textViewEndDate.setText(Common.currentTestItem.getDis_end_date());
            dis_start_date = Common.currentTestItem.getDis_start_date();
            dis_end_date = Common.currentTestItem.getDis_end_date();
        }

    }

    private void updateTestItem() {

        final android.app.AlertDialog waitingDialog = new SpotsDialog(UpdateTestActivity.this);
        waitingDialog.show();
        waitingDialog.setMessage("Please wait ...");

        ApiService service = ApiClient.getClientMedix().create(ApiService.class);
        Call<Result> call = service.updateTestItem(Common.TEST_ID,
                cat_id, editTextName.getText().toString(),
                editTextShortName.getText().toString(),
                editTextLongDesc.getText().toString(),
                editTextShortDesc.getText().toString(),
                status);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                waitingDialog.dismiss();
                updateTestItemPrice();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                waitingDialog.dismiss();
            }
        });
    }

    private void updateTestItemPrice() {

        ApiService service = ApiClient.getClientMedix().create(ApiService.class);
        Call<Result> call = service.updateTestItemPrice(Common.TEST_ID,
                editTextPrice.getText().toString(),
                editTextItemDiscountPrice.getText().toString(),
                dis_start_date,
                dis_end_date);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategorySpinner() {

        final android.app.AlertDialog waitingDialog = new SpotsDialog(UpdateTestActivity.this);
        waitingDialog.show();
        waitingDialog.setMessage("Please wait ...");

        ApiService service = ApiClient.getClientMedix().create(ApiService.class);
        Call<Result> call = service.getAllCategories();

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                categoryList = response.body().getAllCategories();
                waitingDialog.dismiss();

                showListInSpinner();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                waitingDialog.dismiss();
            }
        });
    }

    private void showListInSpinner() {

        String[] items = new String[categoryList.size()];
        //Traversing through the whole list to get all the names
        for (int i = 0; i < categoryList.size(); i++) {
            //Storing names to string array
            items[i] = categoryList.get(i).getName();
        }

        List<String> strList = new ArrayList<String>(Arrays.asList(items));

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(UpdateTestActivity.this, R.layout.spinner_list_item, items);
        //setting adapter to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerCategory.setAdapter(adapter);


        spinnerCategory.setSelection(strList.indexOf(Common.currentTestItem.getCat_name()));

        /*if(menu_name != null)
        {
            spinnerMenuItem.setSelection(adapter.getPosition(menu_name));
        }*/
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat_id = categoryList.get(position).getCat_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // load Categories
        loadCategorySpinner();
    }
}
