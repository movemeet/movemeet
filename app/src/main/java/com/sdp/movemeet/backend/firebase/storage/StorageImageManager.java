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
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.models.Image;
import com.squareup.picasso.Picasso;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;

public class StorageImageManager implements BackendManager<Image> {

    private final StorageReference storageReference;

    public StorageImageManager() {
        this.storageReference = BackendInstanceProvider.getStorageInstance().getReference();
    }

    @Override
    public StorageTask add(Image image, String path) {
        //Stores image in db, and retrieves corresponding view
//        return storageReference.child(path).putFile(image.getImageUri()).addOnSuccessListener(
//                taskSnapshot -> storageReference.child(path).getDownloadUrl().addOnSuccessListener(
//                        uri -> Picasso.get().load(uri).into(image.getImageView()))); // Picasso helps us link the URI to the ImageView
        return storageReference.child(path).putFile(image.getImageUri());
    }

    @Override
    public Task<?> set(Image object, String path) {
        return null;
    }

    @Override
    public Task<?> updt(String value, String path, String field) {
        return null;
    }

    @Override
    public Task<?> delete(String path) {
        return null;
    }

    @Override
    public Task<Uri> get(String path) {
        return storageReference.child(path).getDownloadUrl();
    }

    @Override
    public Task<?> search(String field, Object value) {
        return null;
    }
}
