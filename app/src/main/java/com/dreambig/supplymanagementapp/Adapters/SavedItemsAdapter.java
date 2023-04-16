package com.dreambig.supplymanagementapp.Adapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dreambig.supplymanagementapp.Models.SavedItemModel;
import com.dreambig.supplymanagementapp.Models.SupplyModel;
import com.dreambig.supplymanagementapp.databinding.LayoutSavedItemsBinding;

import java.util.ArrayList;

public class SavedItemsAdapter extends RecyclerView.Adapter<SavedItemsAdapter.ViewHolder> {

    private ArrayList<SavedItemModel> savedItems;
    private ArrayList<SavedItemModel> deleteItems;
    private Boolean editMode;
    private SavedItemsListener savedItemsListener;

    public SavedItemsAdapter(SavedItemsListener savedItemsListener) {
        this.savedItems = new ArrayList<>();
        this.deleteItems = new ArrayList<>();
        this.editMode = false;
        this.savedItemsListener = savedItemsListener;
    }

    @NonNull
    @Override
    public SavedItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSavedItemsBinding binding = LayoutSavedItemsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedItemsAdapter.ViewHolder holder, int position) {
        LayoutSavedItemsBinding binding = holder.binding;
        SavedItemModel savedItem = savedItems.get(position);

        Glide.with(binding.ivItemImage).load(savedItem.getItem().getPhoto_url()).fitCenter().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(binding.ivItemImage);
        binding.tvItemName.setText(savedItem.getItem().getItem_name());
        binding.tvItemType.setText(savedItem.getItem().getItem_type());
        binding.tvAvailability.setText("Available: " +savedItem.getItem().getCurrent_supply());
        binding.cbDelete.setVisibility(View.INVISIBLE);
        binding.cbDelete.setChecked(false);

        if(editMode){
            binding.cbDelete.setVisibility(View.VISIBLE);
        }

        binding.mvSavedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode){
                    binding.cbDelete.setChecked(!binding.cbDelete.isChecked());
                    return;
                }

                savedItemsListener.OnItemClick(savedItem);
            }
        });

        binding.cbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    deleteItems.add(savedItem);
                }
                else{
                    deleteItems.remove(savedItem);
                }
                savedItemsListener.OnEditItemClick(deleteItems);
            }
        });

        binding.mvSavedItems.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savedItemsListener.OnItemLongClick();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedItems.size();
    }

    public void setSavedItems(ArrayList<SavedItemModel> savedItems) {
        this.savedItems = savedItems;
        notifyDataSetChanged();
    }

    public void setEditMode(Boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private LayoutSavedItemsBinding binding;
        public ViewHolder(@NonNull LayoutSavedItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface SavedItemsListener{
        void OnItemLongClick();
        void OnItemClick(SavedItemModel savedItemModel);
        void OnEditItemClick(ArrayList<SavedItemModel> deleteItems);
    }

}
