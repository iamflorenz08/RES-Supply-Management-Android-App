package com.dreambig.supplymanagementapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.dreambig.supplymanagementapp.Models.ItemModel;
import com.dreambig.supplymanagementapp.databinding.AddedItemBinding;

import java.util.ArrayList;
import java.util.List;

public class AddedItemsAdapter extends RecyclerView.Adapter<AddedItemsAdapter.ViewHolder> {
    private List<ItemModel> items = new ArrayList<>();
    private ItemListener itemListener;
    @NonNull
    @Override
    public AddedItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddedItemBinding binding = AddedItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedItemsAdapter.ViewHolder holder, int position) {
        Glide.with(holder.binding.getRoot()).load(items.get(position).getItem_image()).transform(new FitCenter()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.binding.ivStock);
        holder.binding.tvStockName.setText(items.get(position).getItem_name());
        holder.binding.tvItemType.setText(items.get(position).getItem_type());
        holder.binding.tvMeasurement.setText(items.get(position).getUnit_measurement());
        holder.binding.tvItemCost.setText("₱ " +  String.format("%.2f", items.get(position).getTotal_cost()));
        holder.binding.etQuantity.setText(String.valueOf(items.get(position).getQuantity()));

        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.deleteItem(items.get(position));
            }
        });

        holder.binding.btnQuantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curr_val  = Integer.parseInt(holder.binding.etQuantity.getText().toString());
                Double unit_cost = items.get(position).getTotal_cost() / Double.parseDouble(String.valueOf(curr_val));
                int quantity = curr_val + 1;
                Double total_cost = unit_cost * quantity;

                holder.binding.etQuantity.setText(String.valueOf(quantity));
                items.get(position).setQuantity(quantity);
                items.get(position).setTotal_cost(total_cost);
                itemListener.insertItem(items.get(position));
            }
        });

        holder.binding.btnQuantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curr_val  = Integer.parseInt(holder.binding.etQuantity.getText().toString());
                Double unit_cost = items.get(position).getTotal_cost() / Double.parseDouble(String.valueOf(curr_val));
                int quantity = curr_val-1;
                Double total_cost = unit_cost * quantity;

                if(curr_val <= 1) return;
                holder.binding.etQuantity.setText(String.valueOf(quantity));
                items.get(position).setQuantity(quantity);
                items.get(position).setTotal_cost(total_cost);
                itemListener.insertItem(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AddedItemBinding binding;
        public ViewHolder(@NonNull AddedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemListener {
        void insertItem(ItemModel itemModel);
        void deleteItem(ItemModel itemModel);
    }
}
