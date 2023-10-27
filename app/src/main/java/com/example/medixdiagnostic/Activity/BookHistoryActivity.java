package com.example.medixdiagnostic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medixdiagnostic.Adapter.BookHistoryAdapter;
import com.example.medixdiagnostic.Api.ApiClient;
import com.example.medixdiagnostic.Api.ApiService;
import com.example.medixdiagnostic.Model.Result;
import com.example.medixdiagnostic.R;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookHistoryActivity extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener {

    public ImageView imageViewBackButton;
    public RecyclerView recycler_view_BookStatus;
    public BookHistoryAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_history);


        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        // getting views
        imageViewBackButton = (ImageView) findViewById(R.id.imageViewBack);
        recycler_view_BookStatus = (RecyclerView) findViewById(R.id.recycler_view_BookStatus);
        recycler_view_BookStatus.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler_view_BookStatus.setHasFixedSize(true);
        recycler_view_BookStatus.setItemAnimator(new DefaultItemAnimator());
        recycler_view_BookStatus.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(BookHistoryActivity.this);
        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadOrderStatusItems();
                    }
                }
        );

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Load Cart Status Items from Database
        loadOrderStatusItems();
    }

    private void loadOrderStatusItems() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BookHistoryActivity.this);
        String CENTER_ID = sharedPreferences.getString("DIAGNOSTIC_ID", null);
        Log.d("ID: ", CENTER_ID);

        swipeRefreshLayout.setRefreshing(true);
        final android.app.AlertDialog waitingDialog = new SpotsDialog(BookHistoryActivity.this);
        waitingDialog.show();
        waitingDialog.setMessage("Please wait ...");

        //Defining retrofit api service
        ApiService service = ApiClient.getClientMedix().create(ApiService.class);
        Call<Result> call = service.getAllBookingForDiagnostic(CENTER_ID);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (!response.body().getError()) {
                    waitingDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "No Book list", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    waitingDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                    adapter = new BookHistoryAdapter(response.body().getAllBookList(), getApplicationContext());
                    recycler_view_BookStatus.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onRefresh() {
        loadOrderStatusItems();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
