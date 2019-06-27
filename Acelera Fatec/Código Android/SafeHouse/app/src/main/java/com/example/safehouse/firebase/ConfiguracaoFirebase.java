package com.example.safehouse.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase
{
    private static StorageReference storage;
    private static  FirebaseAuth auth;

    public static FirebaseAuth getFirebaseAutenticacao()
    {
        if (auth == null)
        {
            auth= FirebaseAuth.getInstance();
        }
        return auth;
    }


    public static StorageReference getFirebaseStorage()
    {
        if (storage == null)
        {
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }
}

