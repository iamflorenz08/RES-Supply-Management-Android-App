package com.dreambig.supplymanagementapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.dreambig.supplymanagementapp.Models.ItemModel;
import com.dreambig.supplymanagementapp.Models.RequisitionModel;
import com.dreambig.supplymanagementapp.databinding.ItemDetailsRequisitionBinding;

import java.util.ArrayList;
import java.util.List;

public class RequisitionDetailAdapter extends RecyclerView.Adapter<RequisitionDetailAdapter.ViewHolder> {
    private List<ItemModel> items = new ArrayList<>();

    @NonNull
    @Override
    public RequisitionDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDetailsRequisitionBinding binding = ItemDetailsRequisitionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequisitionDetailAdapter.ViewHolder holder, int position) {
        ItemModel item = items.get(position);

        Glide.with(holder.binding.getRoot())
                .load(item.getItem_image())
                .transform(new FitCenter())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.binding.ivStock);

        holder.binding.tvStockName.setText(item.getItem_name());
        holder.binding.tvItemType.setText(item.getItem_type());
        holder.binding.tvMeasurement.setText(item.getUnit_measurement());
        holder.binding.tvItemCost.setText("₱ " + String.format("%.2f", item.getTotal_cost()));
        holder.binding.tvQuantity.setText("Qty: " + item.getQuantity());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDetailsRequisitionBinding binding;
        public ViewHolder(@NonNull ItemDetailsRequisitionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
