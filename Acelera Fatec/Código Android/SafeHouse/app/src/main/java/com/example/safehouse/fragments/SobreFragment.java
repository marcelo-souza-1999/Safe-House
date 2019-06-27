package com.example.safehouse.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.safehouse.R;

public class SobreFragment extends Fragment
{

    private TextView txtEmailFabiano,txtEmailLucas,txtEmailMarcelo,txtEmailWhalesson;

    public SobreFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view =  inflater.inflate(R.layout.fragment_sobre, container, false);

        txtEmailFabiano = view.findViewById(R.id.textViewEmailDevFabiano);
        txtEmailLucas = view.findViewById(R.id.textViewEmailDevLucas);
        txtEmailMarcelo = view.findViewById(R.id.textViewEmailDevMarcelo);
        txtEmailWhalesson = view.findViewById(R.id.textViewEmailDevWhalesson);

        txtEmailFabiano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailFabiano();
            }
        });

        txtEmailLucas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailLucas();
            }
        });

        txtEmailMarcelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailMarcelo();
            }
        });

        txtEmailWhalesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailWhalesson();
            }
        });

        return view;
    }

    public void sendEmailFabiano()
    {
        Intent email = new Intent( Intent.ACTION_SEND );
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"fabianoluizj@gmail.com" } );
        email.putExtra(Intent.EXTRA_SUBJECT, "Designer do Aplicativo" );
        email.putExtra(Intent.EXTRA_TEXT, "Envie-nos uma sugestão ou critica referente ao designer da Safe House" );

        email.setType("message/rfc822");
        startActivity( Intent.createChooser(email, "Escolha o App de e-mail:" ) );
    }

    public void sendEmailMarcelo()
    {
        Intent email = new Intent( Intent.ACTION_SEND );
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"marcelo@object1ve.com" } );
        email.putExtra(Intent.EXTRA_SUBJECT, "Funcionamento do Aplicativo" );
        email.putExtra(Intent.EXTRA_TEXT, "Envie-nos uma sugestão ou critica referente a funcionalidades da Safe House");

        email.setType("message/rfc822");
        startActivity( Intent.createChooser(email, "Escolha o App de e-mail:" ) );
    }

    public void sendEmailLucas()
    {
        Intent email = new Intent( Intent.ACTION_SEND );
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"lmartins3007@gmail.com" } );
        email.putExtra(Intent.EXTRA_SUBJECT, "Funcionamento do Aplicativo" );
        email.putExtra(Intent.EXTRA_TEXT, "Envie-nos uma sugestão ou critica referente a funcionalidades da Safe House");

        email.setType("message/rfc822");
        startActivity( Intent.createChooser(email, "Escolha o App de e-mail:" ) );
    }

    public void sendEmailWhalesson()
    {
        Intent email = new Intent( Intent.ACTION_SEND );
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"whalesson345@gmail.com" } );
        email.putExtra(Intent.EXTRA_SUBJECT, "Funcionamento do Aplicativo" );
        email.putExtra(Intent.EXTRA_TEXT, "Envie-nos uma sugestão ou critica referente a funcionalidades da Safe House");

        email.setType("message/rfc822");
        startActivity( Intent.createChooser(email, "Escolha o App de e-mail:" ) );
    }
}
