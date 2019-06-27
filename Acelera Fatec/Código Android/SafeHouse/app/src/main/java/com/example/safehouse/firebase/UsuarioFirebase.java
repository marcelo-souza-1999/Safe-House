package com.example.safehouse.firebase;

import android.arch.core.internal.FastSafeIterableMap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase
{
    public static FirebaseUser getUsuarioAtual()
    {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarFotoUsuario(Uri url)
    {
        FirebaseUser user = getUsuarioAtual();
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setPhotoUri(url)
                .build();

       try
       {
           user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>()
           {
               @Override
               public void onComplete(@NonNull Task<Void> task)
               {
                   if(!task.isSuccessful())
                   {
                       Log.d("Perfil", "Erro ao atualizar foto de perfil.");
                   }
               }
           });
           return true;
       }
       catch(Exception e)
       {
            e.printStackTrace();
            return false;
       }

    }

    public static boolean atualizarNomeUsuario(String nomeRecuperado)
    {
        FirebaseUser user = getUsuarioAtual();
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setDisplayName(nomeRecuperado)
                .build();

        try
        {
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(!task.isSuccessful())
                    {
                        Log.d("Perfil", "Erro ao atualizar nome do perfil.");
                    }
                }
            });
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualizarEmailUsuario(String emailRecuperado)
    {
        FirebaseUser emailRec = FirebaseAuth.getInstance().getCurrentUser();
        try
        {
            if(emailRec != null)
            {
                emailRecuperado = emailRec.getEmail();
            }
            else
            {
                Log.d("Perfil", "Erro ao recuperar email do usuário.");
            }
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

}
