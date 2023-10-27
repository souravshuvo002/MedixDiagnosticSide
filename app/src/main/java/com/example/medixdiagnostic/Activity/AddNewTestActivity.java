package com.example.medixdiagnostic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medixdiagnostic.Api.ApiClient;
import com.example.medixdiagnostic.Api.ApiService;
import com.example.medixdiagnostic.Common.Common;
import com.example.medixdiagnostic.Model.Category;
import com.example.medixdiagnostic.Model.Result;
import com.example.medixdiagnostic.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AddNewTestActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private TextInputEditText editTextName, editTextShortName, editTextShortDesc, editTextLongDesc, editTextPrice;
    private Button buttonAdd;

    private List<Category> categoryList;
    private String cat_id, test_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Test Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // getting views
        spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        editTextName = (TextInputEditText) findViewById(R.id.editTextItemName);
        editTextShortName = (TextInputEditText) findViewById(R.id.editTextItemShortName);
        editTextShortDesc = (TextInputEditText) findViewById(R.id.editTextItemShortDesc);
        editTextLongDesc = (TextInputEditText) findViewById(R.id.editTextItemLongDesc);
        editTextPrice = (TextInputEditText) findViewById(R.id.editTextItemPrice);
        buttonAdd = (Button) findViewById(R.id.btn_addItem);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextName.getText().toString()) ||
                        TextUtils.isEmpty(editTextShortName.getText().toString()) ||
                        TextUtils.isEmpty(editTextShortDesc.getText().toString()) ||
                        TextUtils.isEmpty(editTextLongDesc.getText().toString()) ||
                        TextUtils.isEmpty(editTextPrice.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Some of fields is empty", Toast.LENGTH_LONG).show();
                } else {
                    addNewTestItem();
                }
            }
        });

        // load Categories
        loadCategorySpinner();
    }



    private void addNewTestItem() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddNewTestActivity.this);
        String CENTER_ID = sharedPreferences.getString("DIAGNOSTIC_ID", null);
        Log.d("ID: ", CENTER_ID);

        final android.app.AlertDialog waitingDialog = new SpotsDialog(AddNewTestActivity.this);
        waitingDialog.show();
        waitingDialog.setMessage("Please wait ...");

        ApiService service = ApiClient.getClientMedix().create(ApiService.class);
        Call<Result> call = service.addTestItem(CENTER_ID,
                cat_id, editTextName.getText().toString(),
                editTextShortName.getText().toString(),
                editTextLongDesc.getText().toString(),
                editTextShortDesc.getText().toString());

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                waitingDialog.dismiss();
                getLastInsertedTestID(CENTER_ID);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                waitingDialog.dismiss();
            }
        });
    }

    private void getLastInsertedTestID(String center_id) {
        ApiService service = ApiClient.getClientMedix().create(ApiService.class);
        Call<Result> call = service.getLastInsertedTestID(center_id);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                test_id = response.body().getLastInsertTestID().getTest_id();
                addTestItemPrice(center_id, test_id);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTestItemPrice(String center_id, String test_id) {
        ApiService service = ApiClient.getClientMedix().create(ApiService.class);
        Call<Result> call = service.addTestPrice(center_id, test_id, editTextPrice.getText().toString());

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.body().getError())
                {
                    Toast.makeText(getApplicationContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategorySpinner() {

        final android.app.AlertDialog waitingDialog = new SpotsDialog(AddNewTestActivity.this);
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
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(AddNewTestActivity.this, R.layout.spinner_list_item, items);
        //setting adapter to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerCategory.setAdapter(adapter);
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
