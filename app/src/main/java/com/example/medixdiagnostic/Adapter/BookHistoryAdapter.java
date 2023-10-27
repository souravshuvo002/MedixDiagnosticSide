package com.example.medixdiagnostic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medixdiagnostic.Activity.BookHistoryActivity;
import com.example.medixdiagnostic.Activity.ShowTestDetailsActivity;
import com.example.medixdiagnostic.Activity.SingleBookStatusActivity;
import com.example.medixdiagnostic.Activity.UpdateTestActivity;
import com.example.medixdiagnostic.Common.Common;
import com.example.medixdiagnostic.Model.Book;
import com.example.medixdiagnostic.Model.Test;
import com.example.medixdiagnostic.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookHistoryAdapter extends RecyclerView.Adapter<BookHistoryAdapter.ViewHolder> {

    private List<Book> bookList;
    private Context context;
    private int lastPosition = -1;


    public BookHistoryAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @Override
    public BookHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_all_book_history_items, parent, false);
        return new BookHistoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final BookHistoryAdapter.ViewHolder holder, final int position) {
        final Book book = bookList.get(position);

        /**
         *  Animation Part
         */
        /*Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        holder.itemView.startAnimation(animation);
        lastPosition = position;*/

        holder.textViewOrderID.setText("Book ID: " + book.getTest_book_id());

        String strCurrentDate = book.getOrder_date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        String date = format.format(newDate);



        holder.textViewOrderDate.setText(date);

        holder.textViewItemPrice.setText(context.getResources().getString(R.string.currency_sign)+ book.getTotal_price());
        holder.textViewOrderStatus.setText(Common.convertCodeToStatus(book.getBook_status_id()));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                String CENTER_ID = sharedPreferences.getString("DIAGNOSTIC_ID", null);

                Intent intent = new Intent(context, SingleBookStatusActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("BOOK_ID", book.getTest_book_id());
                Common.CENTER_ID = CENTER_ID;
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewOrderID, textViewOrderDate, textViewItemPrice, textViewOrderStatus;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewOrderID = (TextView) itemView.findViewById(R.id.textViewOrderID);
            textViewOrderDate = (TextView) itemView.findViewById(R.id.textViewOrderDate);
            textViewItemPrice = (TextView) itemView.findViewById(R.id.textViewItemPrice);
            textViewOrderStatus = (TextView) itemView.findViewById(R.id.textViewOrderStatus);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLay);
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

}
