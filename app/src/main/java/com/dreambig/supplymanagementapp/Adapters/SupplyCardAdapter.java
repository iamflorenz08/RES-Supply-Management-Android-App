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
import com.dreambig.supplymanagementapp.databinding.ItemCardViewBinding;
import java.util.ArrayList;
import java.util.List;

public class SupplyCardAdapter extends RecyclerView.Adapter<SupplyCardAdapter.ViewHolder> {
    private List<SupplyModel> items = new ArrayList<>();
    private ItemListener itemListener;

    @NonNull
    @Override
    public SupplyCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardViewBinding binding = ItemCardViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplyCardAdapter.ViewHolder holder, int position) {
        holder.binding.tvProductName.setText(items.get(position).getItem_name());
        holder.binding.tvSupplyCount.setText("Available: " + items.get(position).getCurrent_supply());
        Glide.with(holder.binding.getRoot())
                .load(items.get(position).getPhoto_url())
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
        private ItemCardViewBinding binding;

        public ViewHolder(@NonNull ItemCardViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    public interface ItemListener{
        void itemOnClick(SupplyModel supplyItem);
    }

}
