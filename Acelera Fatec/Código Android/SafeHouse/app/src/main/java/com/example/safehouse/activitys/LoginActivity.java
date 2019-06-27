package com.example.safehouse.activitys;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.safehouse.R;
import com.example.safehouse.firebase.ConfiguracaoFirebase;
import com.example.safehouse.models.Usuario;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    private EditText campoEmail, campoSenha;
    private Button botaoEntrar, botaoGoogle;
    private String emailUser;
    private CircleImageView imgUser;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private GoogleApiClient googleApiClient;
    private static  int SELECAO_GOOGLE = 1;
    private FirebaseUser usuarioAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

       inicializarObjetos();
    }

    private void inicializarObjetos()
    {
        botaoEntrar = findViewById(R.id.buttonEntrar);
        botaoGoogle = findViewById(R.id.botaoGoogleLogin);
        campoEmail = findViewById(R.id.editTextEmailLogin);
        campoSenha = findViewById(R.id.editTextSenhaLogin);
        imgUser = findViewById(R.id.image_userLogin);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECAO_GOOGLE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                loginGoogle(account);
            }
        }
    }

    public void btnLoginGoogle(View view)
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, SELECAO_GOOGLE);
    }

    public void loginGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {

                    @Override
                    public void onComplete(Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(LoginActivity.this, CasaActivity.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Falha na autenticação com a sua conta Google", Toast.LENGTH_SHORT).show();
                        }
                    }});
    }

    public void verificarCampos()
    {
         campoEmail.requestFocus();
         campoSenha.requestFocus();

          if (campoEmail.getText().toString().equals("") && campoSenha.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Insira seu Email e Senha!", Toast.LENGTH_LONG ).show();
                    campoEmail.requestFocus();
                }
                else if (campoEmail.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Insira seu email!", Toast.LENGTH_LONG );
                    campoEmail.requestFocus();
                }
                else if (campoSenha.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Insira sua senha", Toast.LENGTH_LONG ).show();
                    campoSenha.requestFocus();
                }
                else
                {
                    usuario = new Usuario();
                    usuario.setEmail(campoEmail.getText().toString());
                    usuario.setSenha(campoSenha.getText().toString());
                    logarUsuario();
                }
    }

    public void logarUsuario()
    {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword
                (
                        usuario.getEmail(),
                        usuario.getSenha()
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    if(autenticacao.getCurrentUser().isEmailVerified())
                    {
                        abrirTelaPrincipal();
                    }
                    else
                    {
                        String texto = "Confirme o link enviado no seu email \ne faça login novamente";
                        Spannable centralizarToast = new SpannableString(texto);
                        centralizarToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                0, texto.length() - 1,
                                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                        Toast toast = Toast.makeText(LoginActivity.this, centralizarToast, Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else
                {
                    String excecao = "";
                    try
                    {
                        throw task.getException();
                    }
                    catch(FirebaseAuthInvalidUserException e)
                    {
                        excecao = "Usuário não encontrado!\n Verifique seus dados e tente novamente";
                    }
                    catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        excecao = "E-mail ou Senha não encontrados";
                    }
                    catch (Exception e)
                    {
                        excecao = "Erro ao realizar Login: " +e.getMessage();
                        e.printStackTrace();
                    }

                    Spannable centralizarToast = new SpannableString(excecao);
                    centralizarToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                            0, excecao.length() - 1,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    Toast.makeText(LoginActivity.this, centralizarToast, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void acessarConta(View view)
    {
        verificarCampos();
    }

    private void abrirTelaPrincipal()
    {
        Intent intent = new Intent(LoginActivity.this, CasaActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult)
    {
        Toast.makeText(LoginActivity.this, "Login com o Google falhou!", Toast.LENGTH_SHORT).show();
    }

    public void criarConta(View view)
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void trocarSenha(View view)
    {
        if(campoEmail.getText().toString().equals(""))
        {
            String texto = "Preencha o campo de email, para podermos redefinir sua senha";
            Spannable centralizarToast = new SpannableString(texto);
            centralizarToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0, texto.length() - 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            Toast.makeText(getApplicationContext(),centralizarToast , Toast.LENGTH_LONG).show();

        }
        else
        {
            emailUser = campoEmail.getText().toString();

            autenticacao.sendPasswordResetEmail(emailUser).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        String texto = "Um link para criar uma nova senha foi enviado para o email "+emailUser+ "\n siga as intruções contidas " +
                                "no email para criar uma nova senha para você.";
                        Spannable centralizarToast = new SpannableString(texto);
                        centralizarToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                0, texto.length() - 1,
                                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                        Toast.makeText(getApplicationContext(),centralizarToast , Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Erro ao redefinir senha "+task, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}


