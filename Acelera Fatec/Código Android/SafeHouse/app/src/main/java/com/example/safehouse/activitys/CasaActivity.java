package com.example.safehouse.activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.safehouse.R;
import com.example.safehouse.firebase.UsuarioFirebase;
import com.example.safehouse.services.ConectarWebService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CasaActivity extends AppCompatActivity
{
    private EditText nomeCasa;
    private FirebaseUser usuarioAtual;
    private String nomeUser = "";
    private static ConectarWebService conectarWebService;
    private static String host = conectarWebService.getEndServer();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casa);

        nomeCasa = findViewById(R.id.editTextNomeCasa);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null)
        {
            nomeUser = usuarioAtual.getDisplayName();
        }

        verificarBancoDeDados();
    }

    public void verificarBancoDeDados()
    {
        String url = "http://"+host+"/webService-Fatec/pegarDados.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("dados");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject alarmeJsonObj = jsonArray.getJSONObject(i);
                        String alarmeNomeCasa = alarmeJsonObj.getString("alarmeNomeCasa");
                        if(alarmeNomeCasa != null)
                        {
                            nomeCasa.setText(alarmeNomeCasa);

                            nomeCasa.setEnabled(false);

                            insertDadosSQL("http://"+host+"/webService-Fatec/insertDados.php");

                            abrirTelaPrincipal();
                        }

                    }
                }
                catch (JSONException e)
                {
                    Log.d("Error-Casa", "Erro ao recuperar dados do JSON: "+e);
                    //Toast.makeText(getApplicationContext(), "Erro ao recuperar dados do JSON: "+e, Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("Error-Casa", "Erro no JSON: "+error);
                //Toast.makeText(getApplicationContext(), "Erro no JSON: "+error, Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> dado = new HashMap<>();
                dado.put("nome", nomeUser);

                return dado;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void verificarCasa(View view)
    {
        verificarCampo();
    }

    public void verificarCampo()
    {
        if(nomeCasa.getText().toString().equals(""))
        {
            nomeCasa.setError("Insira um nome para identificar sua  casa");
        }
        else
        {
            insertDadosSQL("http://"+host+"/webService-Fatec/insertDados.php");
        }
    }

    public String getDataAtual()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();

        return dateFormat.format(data);
    }

    public String getHoraAtual()
    {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date hora = new Date();

        return dateFormat.format(hora);
    }

    public String getPathFoto()
    {
        String caminho = "";

        FirebaseUser userFirebase = UsuarioFirebase.getUsuarioAtual();
        if(userFirebase != null)
        {
            Uri url = userFirebase.getPhotoUrl();
            caminho = url.toString();
        }

        return caminho;
    }

    public void insertDadosSQL(String URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("getWebService", "Conectou no web service");
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("getErrorWebService", error.getMessage());
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
                    parametros.put("nome_Android", usuarioAtual.getDisplayName());
                    parametros.put("caminho_Android", getPathFoto());
                    parametros.put("email_Android", usuarioAtual.getEmail());
                    parametros.put("dataCad_Android", getDataAtual());
                    parametros.put("horaCad_Android", getHoraAtual());
                    parametros.put("casa_Android", nomeCasa.getText().toString());
                }
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        abrirTelaPrincipal();
    }

    public void abrirTelaPrincipal()
    {
        Intent intent = new Intent(CasaActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }
}
