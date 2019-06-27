package com.example.safehouse.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.safehouse.R;
import com.example.safehouse.activitys.PrincipalActivity;
import com.example.safehouse.firebase.UsuarioFirebase;
import com.example.safehouse.services.ConectarWebService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {
    private CircleImageView imgPerfil;
    private EditText txtNome, txtEmail, txtNomeCasa;
    private Button btnAlterarSenha;
    private String emailUser;
    private FirebaseUser userFirebase;
    private static ConectarWebService conectarWebService;
    private static String host = conectarWebService.getEndServer();
    private FirebaseUser usuarioAtual;
    private FirebaseAuth autenticacao;

    public PerfilFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();

        carregarDados();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        imgPerfil = view.findViewById(R.id.imagem_minha_conta);
        txtNome = view.findViewById(R.id.editTextNomePerfil);
        txtEmail = view.findViewById(R.id.editTextEmailPerfil);
        txtNomeCasa = view.findViewById(R.id.editTextCasaPerfil);
        btnAlterarSenha = view.findViewById(R.id.buttonAlterarSenha);



        txtNome.setEnabled(false);
        txtEmail.setEnabled(false);
        txtNomeCasa.setEnabled(false);

        btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }



    public void carregarDados() {
        recuperarFoto();
        recuperarNomeEmail();
        recuperarNomeCasa();
    }

    public void recuperarNomeCasa() {
        buscarDadosSQL();
    }

    public void recuperarNomeEmail() {
        userFirebase = UsuarioFirebase.getUsuarioAtual();

        txtNome.setText(userFirebase.getDisplayName());
        txtEmail.setText(userFirebase.getEmail());
    }

    public void recuperarFoto() {
        userFirebase = UsuarioFirebase.getUsuarioAtual();
        Uri url = userFirebase.getPhotoUrl();

        if (url != null) {
            Glide.with(PerfilFragment.this)
                    .load(url)
                    .into(imgPerfil);
        } else {
            imgPerfil.setImageResource(R.drawable.foto_perfil);
        }
    }

    public void buscarDadosSQL() {
        String url = "http://" + host + "/webService-Fatec/pegarDados.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("dados");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject alarmeJsonObj = jsonArray.getJSONObject(i);
                        String alarmeNomeCasa = alarmeJsonObj.getString("alarmeNomeCasa");
                        txtNomeCasa.setText(alarmeNomeCasa);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Erro ao recuperar dados do JSON: " + e, Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Erro no JSON: " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> dado = new HashMap<>();
                dado.put("nome", userFirebase.getDisplayName());

                return dado;
            }
        };
        requestQueue.add(stringRequest);
    }
}
