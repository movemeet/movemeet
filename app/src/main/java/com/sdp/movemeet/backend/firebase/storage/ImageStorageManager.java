package com.sdp.movemeet.backend.firebase.storage;

import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.models.Image;
import com.squareup.picasso.Picasso;

public class ImageStorageManager extends StorageManager<Image> {

    private StorageReference imageRef;

    public ImageStorageManager() {
        this.imageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public StorageTask add(Image image, String path) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(path);
        return fileRef.putFile(image.getImageUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(image.getImageView()); // Picasso helps us link the URI to the ImageView
                    }
                });
            }
        });
    }


    @Override
    public Task<Uri> get(String path) {
        return imageRef.getDownloadUrl();
    }

}