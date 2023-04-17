package com.dreambig.supplymanagementapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.databinding.LayoutRecommendedItemBinding;

import java.util.ArrayList;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    private ArrayList<SupplyModel> recommend_items;
    private RecommendAdapterListener recommendAdapterListener;

    public RecommendAdapter(){
        this.recommend_items = new ArrayList<>();
    }
    @NonNull
    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutRecommendedItemBinding binding = LayoutRecommendedItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendAdapter.ViewHolder holder, int position) {
        LayoutRecommendedItemBinding binding = holder.binding;
        SupplyModel recommend_item = recommend_items.get(position);
        Glide.with(binding.ivItemImage).load(recommend_item.getPhoto_url()).fitCenter().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(binding.ivItemImage);
        binding.tvItemName.setText(recommend_item.getItem_name());
        binding.mvRecommendedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recommendAdapterListener.OnRecommendItemClick(recommend_item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommend_items.size();
    }

    public void setRecommend_items(ArrayList<SupplyModel> recommend_items) {
        this.recommend_items = recommend_items;
        notifyDataSetChanged();
    }

    public void setRecommendAdapterListener(RecommendAdapterListener recommendAdapterListener) {
        this.recommendAdapterListener = recommendAdapterListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LayoutRecommendedItemBinding binding;
        public ViewHolder(@NonNull LayoutRecommendedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface RecommendAdapterListener{
        void OnRecommendItemClick(SupplyModel recommend_item);
    }
}
