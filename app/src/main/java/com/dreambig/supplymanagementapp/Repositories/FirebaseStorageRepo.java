package com.dreambig.supplymanagementapp.Repositories;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageRepo {
    private StorageReference storageRef;
    private MutableLiveData<String> mProfileUri;

    public FirebaseStorageRepo(){
        storageRef = FirebaseStorage.getInstance().getReference("profile_photos");
        mProfileUri = new MutableLiveData<>();
    }

    public void uploadProfilePhoto(Uri uri){
        StorageReference imageRef = storageRef.child(System.currentTimeMillis()+"_"+uri.getLastPathSegment());
        Task<Uri> uriTask = imageRef.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    mProfileUri.postValue(downloadUri.toString());
                } else {
                    mProfileUri.postValue(null);
                }
            }
        });

    }


    public MutableLiveData<String> getmProfileUri() {
        return mProfileUri;
    }

    public void deletePhoto(String fileName) {
        Log.d("MY_DEV", fileName);
        StorageReference imageRef = storageRef.child(fileName);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("MY_DEV", "SUCCESS");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d("MY_DEV", exception.toString());
            }
        });
    }
}
