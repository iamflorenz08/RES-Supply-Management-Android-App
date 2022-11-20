package com.dreambig.supplymanagementapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.databinding.ItemListViewBinding;

import java.util.ArrayList;
import java.util.List;

public class SupplyListAdapter extends RecyclerView.Adapter<SupplyListAdapter.ViewHolder> {
    private List<SupplyModel> items = new ArrayList<>();
    private ItemListener itemListener;

    @NonNull
    @Override
    public SupplyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListViewBinding binding = ItemListViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SupplyListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplyListAdapter.ViewHolder holder, int position) {
        holder.binding.tvProductName.setText(items.get(position).getItem_name());
        holder.binding.tvSupplyCount.setText("Available: " + items.get(position).getAvailable());
        Glide.with(holder.binding.getRoot())
                .load(items.get(position).getItem_image())
                .transform(new FitCenter(), new RoundedCorners(20))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.binding.ivSupply);

        holder.binding.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.itemOnClick(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<SupplyModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private ItemListViewBinding binding;

        public ViewHolder(@NonNull ItemListViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    public interface ItemListener{
        void itemOnClick(SupplyModel supplyItem);
    }
}
