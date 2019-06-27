package com.example.safehouse.configs;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissoes
{
    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode)
    {
        if (Build.VERSION.SDK_INT >=23)
        {
            List<String> listaPermissoes = new ArrayList<>();

            for (String permissao : permissoes)
            {
                Boolean tempermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!tempermissao)
                {
                    listaPermissoes.add(permissao);
                }
            }

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            if (listaPermissoes.isEmpty())
            {
                return true;
            }
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }

        return true;
    }
}
