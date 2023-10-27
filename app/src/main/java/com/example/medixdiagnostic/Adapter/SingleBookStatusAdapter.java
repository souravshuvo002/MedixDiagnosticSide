package com.example.medixdiagnostic.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.medixdiagnostic.Activity.DiagnosticTestActivity;
import com.example.medixdiagnostic.Activity.MainActivity;
import com.example.medixdiagnostic.Activity.SingleBookStatusActivity;
import com.example.medixdiagnostic.Api.ApiService;
import com.example.medixdiagnostic.Api.ApiURL;
import com.example.medixdiagnostic.Api.IFCMService;
import com.example.medixdiagnostic.Common.Common;
import com.example.medixdiagnostic.Model.Book;
import com.example.medixdiagnostic.Model.DataMessage;
import com.example.medixdiagnostic.Model.MyResponse;
import com.example.medixdiagnostic.Model.Result;
import com.example.medixdiagnostic.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleBookStatusAdapter extends RecyclerView.Adapter<SingleBookStatusAdapter.ViewHolder> {

    private List<Book> bookList;
    private Context context;
    private String item;
    private int lastPosition = -1;

    public SingleBookStatusAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;

    }

    @Override
    public SingleBookStatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_book_status_item, parent, false);
        return new SingleBookStatusAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleBookStatusAdapter.ViewHolder holder, final int position) {
        final Book book = bookList.get(position);

        /**
         *  Animation Part
         */
        setFadeAnimation(holder.itemView);

        Picasso.with(context)
                .load(R.drawable.diag)
                .error(R.drawable.diag)
                .into(holder.imageViewItem);


        holder.textViewItemShortName.setText("Abbreviation: " + book.getTest_short_name());
        holder.textViewItemName.setText(book.getTest_name());
        holder.textViewItemPrice.setText("Price: " + context.getResources().getString(R.string.currency_sign) + book.getPrice());
        holder.textViewItemQuantity.setText("Quantity: " + book.getQuantity());

        holder.textViewItemBookingDate.setText("Booking date: " + book.getBooking_date());

        holder.textViewItemOrderStatus.setText("Status: " + Common.convertCodeToStatus(book.getBook_status_id()));


        holder.buttonUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Update status For " + book.getTest_name());
                alertDialog.setMessage("Please Choose a status");

                View myView = LayoutInflater.from(context).inflate(R.layout.update_status_layout, null);

                String[] arraySpinner = new String[]{
                        "Pending", "Accept", "Reject", "Complete"};
                final Spinner spinner = (Spinner) myView.findViewById(R.id.spinner_orderStatus);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spinner.setAdapter(adapter);

                alertDialog.setView(myView);
                alertDialog.setCancelable(false);

                if (book.getBook_status_id().equalsIgnoreCase("1")) {
                    spinner.setSelection(0);
                } else if (book.getBook_status_id().equalsIgnoreCase("2")) {
                    spinner.setSelection(1);
                } else if (book.getBook_status_id().equalsIgnoreCase("3")) {
                    spinner.setSelection(2);
                } else if (book.getBook_status_id().equalsIgnoreCase("4")) {
                    spinner.setSelection(3);
                }

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        item = String.valueOf(spinner.getSelectedItem());

                        //Update Order Status
                        //building retrofit object
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ApiURL.MEDIX_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        //Defining retrofit api service
                        ApiService service = retrofit.create(ApiService.class);
                        Call<Result> call = service.updateBookStatus(String.valueOf(book.getTest_book_id()), book.getCenter_id(), book.getTest_id(), book.getCustomer_id(), String.valueOf(spinner.getSelectedItemPosition() + 1));

                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                String CENTER_NAME = sharedPreferences.getString("USERNAME", null);

                                String status = "";
                                if (spinner.getSelectedItemPosition() == 0) {
                                    status = "Pending";
                                } else if (spinner.getSelectedItemPosition() == 1) {
                                    status = "Accepted";
                                } else if (spinner.getSelectedItemPosition() == 2) {
                                    status = "Rejected";
                                } else if (spinner.getSelectedItemPosition() == 3) {
                                    status = "Completed";
                                }
                                sendOrderUpdateNotification(book.getTest_name(), String.valueOf(book.getTest_book_id()), CENTER_NAME, book.getCustomer_id(), status);

                                //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                ((SingleBookStatusActivity) context).loadOrderItems(book.getTest_book_id());

                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {

                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final android.app.AlertDialog dialog = alertDialog.create();
                alertDialog.show();
                Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (b != null) {
                    b.setTextColor(Color.parseColor("#FF8A65"));
                }

            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DiagnosticTestActivity.class);
                intent.putExtra("NAME", book.getDiagnostic_center_name());
                intent.putExtra("CENTER_ID", book.getCenter_id());
                Common.CENTER_ID = book.getCenter_id();

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewItem;
        public TextView textViewItemShortName, textViewItemName, textViewItemPrice, textViewItemQuantity, textViewItemOrderStatus,
                textViewItemBookingDate;
        public Button buttonUpdateStatus;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            imageViewItem = (ImageView) itemView.findViewById(R.id.image_item);
            textViewItemShortName = (TextView) itemView.findViewById(R.id.textViewItemShortName);
            textViewItemName = (TextView) itemView.findViewById(R.id.textViewItemName);
            textViewItemPrice = (TextView) itemView.findViewById(R.id.textViewItemPrice);
            textViewItemQuantity = (TextView) itemView.findViewById(R.id.textViewItemQuantity);
            textViewItemBookingDate = (TextView) itemView.findViewById(R.id.textViewItemBookingDate);
            textViewItemOrderStatus = (TextView) itemView.findViewById(R.id.textViewItemStatus);

            buttonUpdateStatus = (Button) itemView.findViewById(R.id.btnUpdateStatus);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLay);
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    private void sendOrderUpdateNotification(final String test_name, final String test_book_id, String center_name, String customer_id, final String status) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiURL.MEDIX_SHOP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiService service = retrofit.create(ApiService.class);

        //defining the call
        Call<Result> call = service.getCustomerToken(customer_id);
        //calling the api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                Map<String, String> datasend = new HashMap<>();
                datasend.put("title", "Your booked order #" + test_book_id + " has been " + status);
                datasend.put("message", "Test Name: " + test_name + ".Center Name: " + center_name);

                DataMessage dataMessage = new DataMessage();
                if (response.body().getCustomerToken().getToken() != null) {
                    dataMessage.setTo(response.body().getCustomerToken().getToken());
                }
                dataMessage.setData(datasend);
                IFCMService ifcmService = Common.getFCMService();
                ifcmService.sendNotification(dataMessage)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                if (response.code() == 200) {
                                    if (response.body().success == 1) {
                                        Toast.makeText(context, "Order Updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Send Notification failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("Msg: ", "CheckOut");
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

}