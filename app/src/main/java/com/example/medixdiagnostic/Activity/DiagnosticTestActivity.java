package com.example.medixdiagnostic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medixdiagnostic.Adapter.DiagnosticTestAdapter;
import com.example.medixdiagnostic.Api.ApiClient;
import com.example.medixdiagnostic.Api.ApiService;
import com.example.medixdiagnostic.Model.Result;
import com.example.medixdiagnostic.Model.Test;
import com.example.medixdiagnostic.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticTestActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerViewTest;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private List<Test> testList;
    private DiagnosticTestAdapter adapter;
    private EditText searchInput;
    private CharSequence search = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Test List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // getting views
        searchInput = findViewById(R.id.search_input);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().
                getColor(android.R.color.holo_blue_dark), getResources().
                getColor(android.R.color.holo_red_dark), getResources().
                getColor(android.R.color.holo_green_light), getResources().
                getColor(android.R.color.holo_orange_dark));
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabAddItem);
        recyclerViewTest = (RecyclerView) findViewById(R.id.recycler_view_test);
        recyclerViewTest.setHasFixedSize(false);
        recyclerViewTest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewTest.setItemAnimator(new DefaultItemAnimator());

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiagnosticTestActivity.this, AddNewTestActivity.class));
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });


        // load all test by center_id
        loadTestData();

    }

    private void loadTestData() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DiagnosticTestActivity.this);
        String CENTER_ID = sharedPreferences.getString("DIAGNOSTIC_ID", null);
        Log.d("ID: ", CENTER_ID);

        swipeRefreshLayout.setRefreshing(false);

        final android.app.AlertDialog waitingDialog = new SpotsDialog(DiagnosticTestActivity.this);
        waitingDialog.show();
        waitingDialog.setMessage("Please wait ...");
        // Removed to Wish List
        //Defining retrofit api service
        ApiService service = ApiClient.getClientMedix().create(ApiService.class);
        Call<Result> resultCall = service.getTestByCenterID(CENTER_ID);

        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                testList = response.body().getAllTestByDiagnostic();
                if (testList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "Empty Data", Toast.LENGTH_SHORT).show();
                    waitingDialog.dismiss();
                } else {
                    adapter = new DiagnosticTestAdapter(testList, DiagnosticTestActivity.this);
                    recyclerViewTest.setAdapter(adapter);
                    waitingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                waitingDialog.dismiss();
            }
        });

    }

    void filter(String text) {
        List<Test> temp = new ArrayList();
        for (Test d : testList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getDiagnostic_center_name().toLowerCase().contains(text.toLowerCase())
                    || d.getName().toLowerCase().contains(text.toLowerCase())
                    || d.getShort_name().toLowerCase().contains(text.toLowerCase())
                    || d.getPrice().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }

    @Override
    public void onRefresh() {
        /*searchInput.setText("");
        search = "";*/
        loadTestData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // load all test data
        loadTestData();
    }
}
