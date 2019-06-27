package com.example.safehouse.activitys;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.safehouse.models.BottomSheetAlterarSenhaAlarme;
import com.example.safehouse.models.BottomSheetDialogAtivarAlarme;
import com.example.safehouse.services.ConectarWebService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SenhaAlarmeActivity extends AppCompatActivity
{
    private EditText txtSenhaAlarme;
    private Button btnAlterarSenha;
    private FirebaseUser usuarioAtual;
    private String getNomeLogado = "", getTipoUser = "";
    private static ConectarWebService conectarWebService;
    private static String host = conectarWebService.getEndServer(), emailADM = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha_alarme);

        usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null)
        {
            getNomeLogado = usuarioAtual.getDisplayName();
        }

        txtSenhaAlarme = findViewById(R.id.editTextSenhaAlarme);
        btnAlterarSenha = findViewById(R.id.buttonAlterarSenhaAlarme);

        txtSenhaAlarme.setEnabled(false);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        verificarSenha();
    }

    public void verificarSenha()
    {
        String url = "http://"+host+"/webService-Fatec/pegarSenha.php";
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
                        JSONObject senhaJsonObj = jsonArray.getJSONObject(i);
                        String senhaAlarme = senhaJsonObj.getString("senha");

                        if(senhaAlarme!= null)
                        {
                            String getSenha = senhaAlarme;

                            txtSenhaAlarme.setText(getSenha);
                            txtSenhaAlarme.setEnabled(false);
                            btnAlterarSenha.setVisibility(View.VISIBLE);
                        }

                    }
                }
                catch (JSONException e)
                {
                    avisoSenha();
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(getApplicationContext(), "Erro no JSON: "+error, Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void avisoSenha()
    {
        String getNomeLogado = usuarioAtual.getDisplayName();

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Aviso");
        dialog.setMessage(Html.fromHtml("<medium><font color =\"#01C7D2\">Caro "+getNomeLogado+
                "<small><p align=left><center>O alarme da sua casa encontra-se sem senha.<br>" +
                "Defina uma senha para poder ativá-lo<br>" +
                " e manter sua casa em segurança.</center></small></p>"));
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                txtSenhaAlarme.setEnabled(true);
            }
        });
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }

    public void salvarSenha (View view)
    {
        if(txtSenhaAlarme.getText().length() <6)
        {
            Toast.makeText(getApplicationContext(), "A senha precisa conter 6 dígitos",Toast.LENGTH_LONG).show();
        }
        else
        {
           insertDadosSQL("http://"+host+"/webService-Fatec/insertSenha.php");
        }
    }

    public void updateSenha()
    {
        String url = "http://"+host+"/webService-Fatec/pegarSenha.php";
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
                        JSONObject senhaJsonObj = jsonArray.getJSONObject(i);
                        String senhaAlarme = senhaJsonObj.getString("senha");

                        if(senhaAlarme != null)
                        {
                            txtSenhaAlarme.setText("");
                            BottomSheetAlterarSenhaAlarme bottomSheetAlterarSenhaAlarme = new BottomSheetAlterarSenhaAlarme();
                            bottomSheetAlterarSenhaAlarme.show(getSupportFragmentManager(), "Alterar Alarme");


                        }
                    }
                }
                catch (JSONException e)
                {
                    Log.d("Error-Alarme", "Erro ao recuperar dados do JSON: "+e);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("senha_Android", txtSenhaAlarme.getText().toString());

                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void alterarSenha (View view)
    {
        verificarDadosSQL();
    }

    public void verificarDadosSQL()
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
                        JSONObject tipoJsonObj = jsonArray.getJSONObject(i);
                        String tipoUsuario = tipoJsonObj.getString("tipo");
                        getTipoUser = tipoUsuario;

                        if(getTipoUser.equals("comum"))
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(SenhaAlarmeActivity.this);

                            dialog.setTitle("Permissão Negada");
                            dialog.setMessage(Html.fromHtml("<small><p align=left><center><br>Você não tem permissão para alterar a senha<br>" +
                                    "do alarme, solicite uma permissão ao administrador da residência." +
                                    "</center></small></p>"));
                            dialog.setPositiveButton("Solicitar", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    String texto = "Solicitação enviada com sucesso, quando ela for aceita você será " +
                                            "notificado no seu email.";
                                    Spannable centralizarToast = new SpannableString(texto);
                                    centralizarToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                            0, texto.length() - 1,
                                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                                    Toast.makeText(getApplication(),centralizarToast, Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.setNegativeButton("Cancelar", null);

                            dialog.setCancelable(false);
                            dialog.create();
                            dialog.show();
                        }
                        else if(getTipoUser.equals("adm"))
                        {
                            txtSenhaAlarme.setEnabled(true);
                            txtSenhaAlarme.setFocusable(true);
                            updateSenha();
                        }
                    }
                }
                catch (JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "Erro ao recuperar tipo do usuário do JSON: "+e, Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(), "Erro no JSON: "+error, Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> dado = new HashMap<>();
                dado.put("nome", getNomeLogado);

                return dado;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void updateSenhaAlarme(String URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("getUpdateDados", "Atualizou Senha do alarme");
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("getErrorSenhaAlarme", error.getMessage());
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
                    parametros.put("senha_Android", txtSenhaAlarme.getText().toString());
                }
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void insertDadosSQL (String URL)
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
                    parametros.put("senha_Android", txtSenhaAlarme.getText().toString());
                    parametros.put("estado_Android", "Desativado");

                    Log.d("valor", txtSenhaAlarme.getText().toString());
                }
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        abrirFragment();
    }

    public void abrirFragment()
    {
        Toast.makeText(getApplicationContext(), "Senha criada com sucesso.", Toast.LENGTH_LONG).show();
        finish();
    }
}
