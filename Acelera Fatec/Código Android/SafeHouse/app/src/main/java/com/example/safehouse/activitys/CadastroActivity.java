package com.example.safehouse.activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.safehouse.R;
import com.example.safehouse.configs.Permissoes;
import com.example.safehouse.firebase.ConfiguracaoFirebase;
import com.example.safehouse.firebase.UsuarioFirebase;
import com.example.safehouse.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroActivity extends AppCompatActivity
{
    private EditText campoNome, campoEmail, campoSenha, campoConfirmarSenha;
    private TextView textoAviso, inserirFoto;
    private Button botaoCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private StorageReference storageReference;
    private ProgressBar carregando;
    private Bitmap imagem;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private CircleImageView imagemPerfil;
    private CircleMenu menuCircular;
    private StorageReference imagemRef;
    private String[] permissoesNecessarias = new String[]
    {
         Manifest.permission.READ_EXTERNAL_STORAGE,
         Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().setTitle("Cadastro");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        autenticacao = FirebaseAuth.getInstance();
        campoNome = findViewById(R.id.editTextNomeCasa);
        campoEmail = findViewById(R.id.editTextEmailCadastro);
        campoSenha = findViewById(R.id.editTextSenhaCadastro);
        campoConfirmarSenha = findViewById(R.id.editTextConfirmarSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);
        carregando = findViewById(R.id.progressBarCarregando);
        textoAviso = findViewById(R.id.textViewCarregar);
        imagemPerfil = findViewById(R.id.imagem_logo_app);
        carregando.setVisibility(View.INVISIBLE);
        textoAviso.setVisibility(View.INVISIBLE);
        inserirFoto = findViewById(R.id.textViewInserirFoto);
        menuCircular = findViewById(R.id.opcoes);

        menuCircular.setVisibility(View.INVISIBLE);

        menuCircular.setMainMenu(Color.parseColor("#01C7D2"),R.drawable.ic_camera_preto24dp, R.drawable.ic_remover_24dp)
                .addSubMenu(Color.parseColor("#258CFF"), R.drawable.ic_camera_branco_24dp)
                .addSubMenu(Color.parseColor("#03a9f4"), R.drawable.ic_galeria_branco)

                .setOnMenuSelectedListener(new OnMenuSelectedListener()
                {
                    @Override
                    public void onMenuSelected(int index)
                    {
                        if(index == 0)
                        {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(Intent.createChooser(takePictureIntent,"Foto de Perfil"), SELECAO_CAMERA);
                            menuCircular.setVisibility(View.INVISIBLE);
                            inserirFoto.setVisibility(View.VISIBLE);
                            menuCircular.closeMenu();
                        }
                        if(index == 1)
                        {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(Intent.createChooser(intent, "Foto de perfil"), SELECAO_GALERIA);
                            menuCircular.closeMenu();
                            menuCircular.setVisibility(View.INVISIBLE);
                            inserirFoto.setVisibility(View.VISIBLE);
                        }

                    }
                });

        botaoCadastrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    validarCadastro();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void alertaUsuario()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Permissões Negadas");
        dialog.setMessage("Autorize as permissões solicitadas pelo aplicativo para que sua foto seja inserida no aplicativo.");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                menuCircular.setVisibility(View.INVISIBLE);
                menuCircular.closeMenu();
            }
        });

        dialog.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults)
        {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED)
            {
                alertaUsuario();
                return;
            }
        }
    }

    public void inserirFoto(View view)
    {
        Permissoes.validarPermissoes(permissoesNecessarias, CadastroActivity.this, 1);

        if(imagem == null)
        {
            menuCircular.setVisibility(View.VISIBLE);
            menuCircular.openMenu();
        }
        else if(imagem != null)
        {
            imagem = null;
            imagemPerfil.setImageResource(R.drawable.foto_perfil);
            inserirFoto.setVisibility(View.VISIBLE);
            inserirFoto.setText("Inserir foto");
        }
    }

    public void validarCadastro() throws ClassNotFoundException
    {
        if (campoNome.getText().toString().equals(""))
        {
            campoNome.setError("Campo Nome obrigatório!");
            campoNome.requestFocus();
        }
        else if (campoEmail.getText().toString().equals(""))
        {
            campoEmail.setError("Campo Email obrigatório!");
            campoEmail.requestFocus();
        }
        else if (campoSenha.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Preencha os campos de senha!", Toast.LENGTH_LONG).show();
        }
        else if (!campoSenha.getText().toString().equals(campoConfirmarSenha.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "As senhas não conferem!", Toast.LENGTH_LONG).show();
        }
        else
        {
            usuario = new Usuario();
            usuario.setNome(campoNome.getText().toString());
            usuario.setEmail(campoEmail.getText().toString());
            usuario.setSenha(campoConfirmarSenha.getText().toString());
            cadEmail();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK )
        {
            try
            {
                if(requestCode == SELECAO_CAMERA)
                {
                    Bundle extras = data.getExtras();
                    imagem = (Bitmap) extras.get("data");
                    imagemPerfil.setImageBitmap( imagem );
                    inserirFoto.setText("Remover foto");
                    Toast.makeText(getApplicationContext(), "Imagem de perfil atualizada!",Toast.LENGTH_LONG).show();
                }
                else if(requestCode == SELECAO_GALERIA)
                {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    imagem = BitmapFactory.decodeStream(inputStream);
                    imagemPerfil.setImageBitmap( imagem );
                    inserirFoto.setText("Remover foto");
                    Toast.makeText(getApplicationContext(), "Imagem de perfil atualizada!",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void salvarImagemFirebase()
    {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.PNG, 80, baos);
            byte[] dadosImagem = baos.toByteArray();

            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            String identificador_user = autenticacao.getCurrentUser().getEmail();

            //Estrutura das fotos no Firebase
            imagemRef = storageReference
                    .child("Imagens de Perfil")
                    .child(identificador_user)
                    .child("usuário.png");

            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
            uploadTask.addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Erro ao inserir imagem de Perfil: " + e, Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                           atualizarFotoUsuario(uri);
                        }
                    });
                }
            });
    }

    public void atualizarFotoUsuario(Uri url)
    {
        UsuarioFirebase.atualizarFotoUsuario(url);
    }

    public void cadEmail()
    {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword
        (
            usuario.getEmail(), usuario.getSenha()

         ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(Task<AuthResult> task)
            {

                if (task.isSuccessful())
                {
                    textoAviso.requestFocus();
                    carregando.setVisibility(View.VISIBLE);
                    textoAviso.setVisibility(View.VISIBLE);

                    autenticacao.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                if(imagem != null)
                                {
                                    salvarImagemFirebase();
                                }

                                String texto = "Cadastro efetuado com Sucesso!\nConfirme sua conta no link enviado ao seu email ";
                                Spannable centralizarToast = new SpannableString(texto);
                                centralizarToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                        0, texto.length() - 1,
                                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                                Toast.makeText(getApplicationContext(), centralizarToast, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                                startActivity(intent);

                                UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                                UsuarioFirebase.atualizarEmailUsuario(usuario.getEmail());
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {

                    String excecao = "";
                    try
                    {
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e)
                    {
                        excecao = "Insira uma senha contendo letras e números";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        excecao = "Por favor, insira um e-mail válido!";
                    }
                    catch (FirebaseAuthUserCollisionException e)
                    {
                        excecao = "Esse email já foi cadastrado \nfavor inserir outro e-mail!";
                    }
                    catch (Exception e)
                    {
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                    }

                    Spannable centralizarToast = new SpannableString(excecao);
                    centralizarToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                            0, excecao.length() - 1,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                     Toast.makeText(CadastroActivity.this, centralizarToast, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
