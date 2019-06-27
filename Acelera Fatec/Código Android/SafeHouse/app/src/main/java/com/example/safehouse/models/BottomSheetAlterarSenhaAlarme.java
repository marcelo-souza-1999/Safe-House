package com.example.safehouse.models;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.safehouse.R;
import com.example.safehouse.activitys.SenhaAlarmeActivity;
import com.example.safehouse.fragments.ConfiguracoesFragment;
import com.example.safehouse.fragments.PrincipalFragment;
import com.example.safehouse.services.ConectarWebService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;


public class BottomSheetAlterarSenhaAlarme extends BottomSheetDialogFragment
{
    private EditText edtInsereSenha;
    private TextView txtAlterarSenha;
    private String getCampoSenha = "", estadoAlarme = "";
    private static ConectarWebService conectarWebService;
    private static String host = conectarWebService.getEndServer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_alterar_senha_alarme, container, false);

        edtInsereSenha = view.findViewById(R.id.editTextAlterarAlarme);
        txtAlterarSenha = view.findViewById(R.id.textViewAlterarAlarme);

        txtAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSenha();
            }
        });
        return view;
    }

    public void updateSenha()
    {
        if(edtInsereSenha.getText().length() < 6)
        {
            Toast.makeText(getContext(), "A senha precisa conter 6 dígitos", Toast.LENGTH_LONG).show();
        }
        else
        {
            String URL =  "http://"+host+"/webService-Fatec/updateSenhaAlarme.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    Log.d("getUpdateDados", "Atualizou estado do alarme");
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Log.e("getErrorUpdateAlarme", error.getMessage());
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String, String> parametros = new HashMap<String, String>();
                    FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
                    if(usuarioAtual != null)
                    {
                        parametros.put("senha_Android", edtInsereSenha.getText().toString());
                    }
                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);

            dismiss();
            restartActivity();
        }
    }

    private void restartActivity()
    {
        Toast.makeText(getContext(),"Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
       startActivity(new Intent(getContext(), SenhaAlarmeActivity.class));
    }
}
