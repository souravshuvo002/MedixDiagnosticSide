package com.example.medixdiagnostic.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medixdiagnostic.Activity.ShowTestDetailsActivity;
import com.example.medixdiagnostic.Activity.UpdateTestActivity;
import com.example.medixdiagnostic.Common.Common;
import com.example.medixdiagnostic.Model.Test;
import com.example.medixdiagnostic.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DiagnosticTestAdapter extends RecyclerView.Adapter<DiagnosticTestAdapter.ViewHolder> {

    private List<Test> diagnosticTestLists;
    private Context context;
    private RecyclerView recyclerView = null;
    int previousExpandedPosition = -1;
    int mExpandedPosition = -1;

    public DiagnosticTestAdapter(List<Test> diagnosticTestLists, Context context) {
        this.diagnosticTestLists = diagnosticTestLists;
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public DiagnosticTestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_diagnostic_test_items_2, parent, false);
        return new DiagnosticTestAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DiagnosticTestAdapter.ViewHolder holder, final int position) {
        final Test test = diagnosticTestLists.get(position);

        /**
         *  Animation Part
         */
        setFadeAnimation(holder.itemView);
        //holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

        final boolean isExpanded = position == mExpandedPosition;
        holder.linearLayBook.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        if (isExpanded) {
            previousExpandedPosition = position;
        }

        holder.textViewType.setText(test.getCat_name());
        holder.textViewName.setText(test.getName());
        holder.textViewShortName.setText(test.getShort_name());

        if (test.getDiscount_price().equals("0.0000")) {
            DecimalFormat df2 = new DecimalFormat("####0.00");
            double price = Double.parseDouble(test.getPrice());
            holder.textViewPrice.setText(new StringBuilder(context.getResources().getString(R.string.currency_sign)).append(df2.format(price)));
        } else {
            DecimalFormat df2 = new DecimalFormat("####0.00");
            double price = Double.parseDouble(test.getDiscount_price());
            holder.textViewPrice.setText(new StringBuilder(context.getResources().getString(R.string.currency_sign)).append(df2.format(price)));
        }
        Picasso.with(context)
                .load(R.drawable.diag)
                .error(R.drawable.diag)
                .into(holder.imageView);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);

                final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int distance;
                    View first = recyclerView.getChildAt(0);
                    int height = first.getHeight();
                    int current = recyclerView.getChildAdapterPosition(first);
                    int p = Math.abs(position - current);
                    if (p > 5) distance = (p - (p - 5)) * height;
                    else distance = p * height;
                    manager.scrollToPositionWithOffset(position, distance);
                }
            }
        });


        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.currentTestItem = test;
                Common.DIAGNOSTIC_ID = test.getCenter_id();
                Common.TEST_ID = test.getTest_id();
                context.startActivity(new Intent(context, UpdateTestActivity.class));
            }
        });

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentTestItem = test;
                Common.DIAGNOSTIC_ID = test.getCenter_id();
                Common.TEST_ID = test.getTest_id();
                context.startActivity(new Intent(context, ShowTestDetailsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return diagnosticTestLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewType, textViewName, textViewShortName, textViewPrice;
        public ImageView imageView;
        public Button btnUpdate, btnDetails;
        public LinearLayout linearLayout, linearLayBook;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewType = (TextView) itemView.findViewById(R.id.item_name_type);
            textViewName = (TextView) itemView.findViewById(R.id.item_name);
            textViewShortName = (TextView) itemView.findViewById(R.id.item_short_name);
            textViewPrice = (TextView) itemView.findViewById(R.id.item_price);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            btnUpdate = (Button) itemView.findViewById(R.id.btnUpdate);
            btnDetails = (Button) itemView.findViewById(R.id.btnDetails);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLay);
            linearLayBook = (LinearLayout) itemView.findViewById(R.id.linearLayBook);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateList(List<Test> list) {
        this.diagnosticTestLists = list;
        notifyDataSetChanged();
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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }
}