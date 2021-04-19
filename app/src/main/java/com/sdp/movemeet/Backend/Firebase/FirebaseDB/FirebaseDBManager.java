package com.sdp.movemeet.Backend.Firebase.FirebaseDB;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.sdp.movemeet.Backend.BackendStorage;
import com.sdp.movemeet.Backend.Firebase.FirebaseObject;
import com.sdp.movemeet.Backend.Serialization.BackendSerializer;

import java.util.Map;

abstract class FirebaseDBManager<T extends FirebaseObject> implements BackendStorage<T> {

    private final static String PATH_SEPARATOR = "/";

    private final BackendSerializer<T> serializer;
    private final FirebaseDatabase db;

    public <T> FirebaseDBManager(FirebaseDatabase db, BackendSerializer serializer) {
        if (db == null || serializer == null) throw new IllegalArgumentException();

        this.db = db;
        this.serializer = serializer;
    }

    @Override
    public Task<?> add(T object, String path) {
        if (object == null || path == null || path.isEmpty()) throw new IllegalArgumentException();

        Map<String, Object> data = serializer.serialize(object);
        DatabaseReference newChild = db.getReference(path).push();
        return newChild.setValue(data);
    }

    @Override
    public Task<?> delete(String path) {
        if (path == null || path.isEmpty()) throw new IllegalArgumentException();

        DatabaseReference toDelete = db.getReference(path);
        return toDelete.removeValue();
    }

    @Override
    public Task<?> get(String path) {
        if (path == null || path.isEmpty()) throw new IllegalArgumentException();

        DatabaseReference ref = db.getReference(path);
        return ref.get();
    }

    @Override
    public Task<?> search(String field, Object value) {
        throw new UnsupportedOperationException();
    }
}
