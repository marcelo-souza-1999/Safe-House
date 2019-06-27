package com.example.safehouse.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safehouse.R;
import com.example.safehouse.activitys.LoginActivity;
import com.example.safehouse.activitys.SenhaAlarmeActivity;
import com.example.safehouse.firebase.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfiguracoesFragment extends Fragment
{
    private TextView textViewTitulo;
    private Button btnApagar, btnCriarSenha;
    private FirebaseUser usuarioAtual;
    private FirebaseAuth autenticacao;

    public ConfiguracoesFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_configuracoes, container, false);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        textViewTitulo = view.findViewById(R.id.textViewTituloLogin);

        btnApagar = view.findViewById(R.id.buttonExcluirConta);

        btnCriarSenha = view.findViewById(R.id.buttonCriarSenhaAlarme);

        textViewTitulo.setText(Html.fromHtml("<u>GERENCIAMENTO DA SUA<br><center>RESIDÊNCIA</u></center>"));

        btnCriarSenha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                abrirActivitySenha();
            }
        });

        btnApagar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                apagarConta();
            }
        });

        return view;
    }

    public void abrirActivitySenha()
    {
        Intent intent = new Intent(getContext(), SenhaAlarmeActivity.class);
        startActivity(intent);
    }

    public void apagarConta()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        dialog.setTitle("Apagar conta");
        dialog.setMessage("Tem certeza que deseja apagar sua conta?");

        dialog.setCancelable(true);

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
                if(usuarioAtual != null)
                {
                    usuarioAtual.delete().addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                    if(task.isSuccessful())
                    {
                        autenticacao.signOut();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getContext(), "Conta apagada com sucesso!", Toast.LENGTH_LONG).show();
                        getActivity().getFragmentManager().popBackStack();
                    }
                    else
                    {
                         Toast.makeText(getContext(), "Ocorreu um erro ao apagar sua conta: "+task, Toast.LENGTH_LONG).show();
                    }
                        }
                    });
                }
            }
        });

        dialog.setNegativeButton("Não", null);

        dialog.create();
        dialog.show();
    }
}
