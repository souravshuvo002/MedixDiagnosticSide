package com.example.medixdiagnostic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.medixdiagnostic.Api.ApiClient;
import com.example.medixdiagnostic.Api.ApiService;
import com.example.medixdiagnostic.Api.ApiURL;
import com.example.medixdiagnostic.Common.Common;
import com.example.medixdiagnostic.Model.Result;
import com.example.medixdiagnostic.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class RegistrationActivity extends AppCompatActivity {


    private TextInputEditText editTextName, editTextTagline, editTextEmail, editTextPhone,
            editTextPassword, editTextConPassword, editTextAddress, editTextDiscount;
    private CheckBox checkboxTermAgreement;
    private LinearLayout laySignIn;
    private Button btn_reg;

    private String strName, strTagline, strEmail, strPhone, strAddress, strPass, strConPass, strDiscount;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
        editor = sharedPreferences.edit();

        // getting views
        editTextName = (TextInputEditText) findViewById(R.id.editTextName);
        editTextEmail = (TextInputEditText) findViewById(R.id.editTextEmail);
        editTextPhone = (TextInputEditText) findViewById(R.id.editTextPhone);
        editTextAddress = (TextInputEditText) findViewById(R.id.editTextAddress);
        editTextPassword = (TextInputEditText) findViewById(R.id.editTextPassword);
        editTextConPassword = (TextInputEditText) findViewById(R.id.editTextConPassword);
        editTextTagline = (TextInputEditText) findViewById(R.id.editTextTagline);
        editTextDiscount = (TextInputEditText) findViewById(R.id.editTextDiscount);
        checkboxTermAgreement = (CheckBox) findViewById(R.id.checkboxTermAgreement);
        laySignIn = (LinearLayout) findViewById(R.id.laySignIn);
        btn_reg = (Button) findViewById(R.id.btn_reg);

        // setting phone number
        editTextPhone.setText(Common.Diagnostic_phone);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


        laySignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerUser() {
        if(checkboxTermAgreement.isChecked()){

            strName = editTextName.getText().toString().trim();
            strTagline = editTextTagline.getText().toString().trim();
            strEmail = editTextEmail.getText().toString().trim();
            strPhone = editTextPhone.getText().toString().trim();
            strAddress = editTextAddress.getText().toString().trim();
            strPass = editTextPassword.getText().toString().trim();
            strConPass = editTextConPassword.getText().toString().trim();
            strDiscount = editTextDiscount.getText().toString().trim();

            if(!strPass.equals(strConPass))
            {
                Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_LONG).show();
                return;
            }
            else
            {
                final android.app.AlertDialog waitingDialog = new SpotsDialog(RegistrationActivity.this);
                waitingDialog.show();
                waitingDialog.setMessage("Please wait ...");

                //Defining retrofit api service
                ApiService service = ApiClient.getClientMedix().create(ApiService.class);
                Call<Result> call = service.registerDiagnostic(strName, strAddress, strPhone, strEmail, strPass, strTagline, strDiscount);

                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        waitingDialog.dismiss();

                        if(!response.body().getError())
                        {
                            editor.putString("USERNAME", response.body().getDiagnostic().getName());
                            editor.putString("PHONE", editTextPhone.getText().toString());
                            editor.putString("PASSWORD", editTextPassword.getText().toString());
                            editor.putString("DIAGNOSTIC_ID", response.body().getDiagnostic().getDiagnostic_center_id());
                            editor.apply();

                            Common.Diagnostic_phone = strPhone;
                            Common.DIAGNOSTIC_ID = response.body().getDiagnostic().getDiagnostic_center_id();

                            updateTokenToServer();

                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(), "You must accept privacy policy and term & condition in order to sign up for Dhacai.", Toast.LENGTH_LONG).show();
        }
    }

    private void updateTokenToServer() {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String DIAGNOSTIC_ID = sharedPreferences.getString("DIAGNOSTIC_ID", null);

        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {

                        //building retrofit object
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ApiURL.MEDIX_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        //Defining retrofit api service
                        ApiService service = retrofit.create(ApiService.class);

                        //defining the call
                        Call<Result> call = service.updateDiagnosticToken(DIAGNOSTIC_ID, instanceIdResult.getToken());
                        //calling the api
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {
                                Log.e("MA_Debug: ", response.body().getMessage());
                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                Log.e("MA_Debug: ", t.getMessage());
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }



    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}