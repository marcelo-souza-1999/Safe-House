package com.example.safehouse.activitsCarregando;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.safehouse.R;
import com.example.safehouse.activitys.CasaActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class CadastroGoogleActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{

    private FirebaseAuth autenticacao;
    private GoogleApiClient googleApiClient;
    private FrameLayout frameCarregando;
    private ProgressBar barraProgresso;
    private TextView textAviso;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas_google);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        frameCarregando = findViewById(R.id.frameCarregando);
        barraProgresso = findViewById(R.id.progressBarCarregandoContas);
        textAviso = findViewById(R.id.textViewAviso);
        autenticacao = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        sigIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseLogin(account);


            }
        }
    }


    private void sigIn()
    {
        MyRunnable runnable = new MyRunnable();
        new Thread(runnable).start();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                barraProgresso.setVisibility(View.INVISIBLE);
                textAviso.setVisibility(View.INVISIBLE);


            }
        }, 3000);
    }

    public class MyRunnable implements Runnable
    {

        @Override
        public void run()
        {

            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, 1);
            Log.d("ErroGoogle", "Aqui");
        }
    }

    private void signOut()
    {
        autenticacao.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>()
        {
            @Override
            public void onResult(@NonNull Status status)
            {
                Toast.makeText(CadastroGoogleActivity.this, "Conta da Google desconectada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Toast.makeText(CadastroGoogleActivity.this, "Falha na autenticação com a Google", Toast.LENGTH_SHORT).show();
    }

    private void firebaseLogin(GoogleSignInAccount account)
    {
        textAviso.setText("Finalizando seu cadastro...");
        textAviso.setTextSize(18);
        textAviso.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        barraProgresso.setVisibility(View.VISIBLE);
        textAviso.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            { Log.d("ErroGoogle", "Aqui");
                barraProgresso.setVisibility(View.INVISIBLE);
                textAviso.setVisibility(View.INVISIBLE);
            }
        }, 4000);

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        autenticacao.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
         {

             @Override
              public void onComplete(Task<AuthResult> task)
              {
        if (task.isSuccessful())
        {
            startActivity(new Intent(CadastroGoogleActivity.this, CasaActivity.class));
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Falha na autenticação com a sua conta Google", Toast.LENGTH_SHORT).show();
        }
        }});
    }
}
