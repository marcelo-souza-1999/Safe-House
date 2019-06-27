package com.example.safehouse.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.safehouse.R;
import com.example.safehouse.activitsCarregando.CadastroGoogleActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null && usuarioAtual.isEmailVerified())
        {
            abrirTelaPrincipal();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnLoginGoogle(View view)
    {
        Intent intent = new Intent(getApplicationContext(), CadastroGoogleActivity.class);
        startActivity(intent);
    }

    public void btnCadastrar(View view)
    {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void btnEntrar(View view)
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void abrirTelaPrincipal()
    {
        Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }
}
