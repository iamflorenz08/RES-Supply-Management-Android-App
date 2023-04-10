package com.dreambig.supplymanagementapp.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.R;
import com.dreambig.supplymanagementapp.databinding.RequisitionsBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequisitionAdapter extends RecyclerView.Adapter<RequisitionAdapter.ViewHolder> {
    private List<RequisitionModel> requisitions = new ArrayList<>();
    private ItemListener itemListener;
    @NonNull
    @Override
    public RequisitionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequisitionsBinding binding = RequisitionsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequisitionAdapter.ViewHolder holder, int position) {
        RequisitionModel requisition = requisitions.get(position);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            OffsetDateTime odt = OffsetDateTime.parse(requisition.getCreatedAt());
            holder.binding.tvDate.setText(odt.getMonthValue() + "/" + odt.getDayOfMonth() + "/" + odt.getYear());
        }

        Glide.with(holder.binding.getRoot())
                .load(requisition.getItems().get(0).getItem_image())
                .transform(new FitCenter(), new RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.binding.ivStockImage);

        holder.binding.tvApproval.setText(requisition.getStatus());
        if(requisition.getStatus().equals("To be approved") ){
            holder.binding.tvApproval.setTextColor(Color.parseColor("#1A297A"));
        }
        else if(requisition.getStatus().equals("On going") || requisition.getStatus().equals("Completed")){
            holder.binding.tvApproval.setTextColor(Color.parseColor("#32A05F"));
        }
        else if(requisition.getStatus().equals("Rejected")){
            holder.binding.tvApproval.setTextColor(Color.parseColor("#FF1A03"));
        }

        holder.binding.tvItemName.setText(requisition.getItems().get(0).getItem_name());
        holder.binding.tvItemType.setText(requisition.getItems().get(0).getItem_type());
        holder.binding.tvMeasurement.setText(requisition.getItems().get(0).getUnit_measurement());
        holder.binding.tvOtherItems.setText("+ " + requisition.getItems().size() + " Other items");
        holder.binding.tvTotalCost.setText("₱ " + String.format("%.2f", requisition.getItems().get(0).getTotal_cost()));
        holder.binding.tvQuantity.setText("Qty: " + requisition.getItems().get(0).getQuantity());

        holder.binding.btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.requestDetails(requisition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requisitions.size();
    }

    public void setRequisitions(List<RequisitionModel> requisitions) {
        this.requisitions = requisitions;
        notifyDataSetChanged();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RequisitionsBinding binding;
        public ViewHolder(@NonNull RequisitionsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemListener{
        void requestDetails(RequisitionModel requisitionModel);
    }
}
