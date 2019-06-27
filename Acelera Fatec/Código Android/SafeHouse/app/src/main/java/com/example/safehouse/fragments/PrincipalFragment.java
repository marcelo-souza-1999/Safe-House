package com.example.safehouse.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.safehouse.models.BottomSheetDialogAtivarAlarme;
import com.example.safehouse.models.BottomSheetDialogDesativarAlarme;
import com.example.safehouse.services.ConectarWebService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PrincipalFragment extends Fragment
{
    private TextView sessaoUser, resultado, nomeCasa, textTitulo;
    private String estadoPortao = "";
    private Button btnAtivarAlarme, btnAbrirGaragem;
    private FirebaseUser usuarioAtual;
    private SharedPreferences sPreferences = null;
    private static ConectarWebService conectarWebService;
    private static String host = conectarWebService.getEndServer();

    public PrincipalFragment()
    {

    }

    @Override
    public void onStart()
    {
        super.onStart();

        verificarBD();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (sPreferences.getBoolean("firstRun", true))
        {
            sPreferences.edit().putBoolean("firstRun", false).apply();
            verificarUser();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        sessaoUser = view.findViewById(R.id.textViewUsuario);

        resultado = view.findViewById(R.id.textViewAtivado);

        nomeCasa = view.findViewById(R.id.textViewNomeCasa);

        btnAtivarAlarme = view.findViewById(R.id.buttonAtivarAlarme);

        btnAbrirGaragem = view.findViewById(R.id.buttonAbrirGaragem);

        textTitulo = view.findViewById(R.id.textViewTituloLogin);

        usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null)
        {
            sessaoUser.setText("  Usuário logado: "+usuarioAtual.getDisplayName());
        }

        buscarDadosSQL();

        sPreferences = this.getActivity().getSharedPreferences("first run",MODE_PRIVATE);

        textTitulo.setText(Html.fromHtml("<u>SAFE HOUSE<br><center>SUA CASA EM SEGURANÇA</u></center>"));

        btnAtivarAlarme.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               ativarAlarme();
            }
        });

        btnAbrirGaragem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(btnAbrirGaragem.getText().equals("Abrir Garagem"))
                {
                    estadoPortao = "Aberto";
                    salvarAtivarPortao("http://"+host+"/webService-Fatec/estadoPortao.php");
                }
                else if(btnAbrirGaragem.getText().equals("FECHAR GARAGEM"))
                {
                    salvarDesativarPortao("http://"+host+"/webService-Fatec/estadoPortao.php");
                }
            }
        });

        return view;
    }

    public void verificarBD()
    {
        String url = "http://"+host+"/webService-Fatec/pegarEstadoAlarme.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                        JSONObject estadoJsonObj = jsonArray.getJSONObject(i);
                        String estadoAlarme = estadoJsonObj.getString("estado");

                        String getEstado = estadoAlarme;

                        if(getEstado.equals("Desativado"))
                        {
                            resultado.setText("Desativado");
                            resultado.setTextColor(Color.RED);
                            btnAtivarAlarme.setText("ATIVAR ALARME");
                        }
                        else if(getEstado.equals("Ativado"))
                        {
                            resultado.setText("Ativado");
                            resultado.setTextColor(Color.GREEN);
                            btnAtivarAlarme.setText("DESATIVAR ALARME");
                        }
                    }
                }
                catch (JSONException e)
                {
                   // Toast.makeText(getContext(), "Erro ao recuperar estado do Alarme: "+e, Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(), "Erro no JSON: "+error, Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> dado = new HashMap<>();
                dado.put("nome", sessaoUser.getText().toString());

                return dado;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void buscarDadosSQL()
    {

        String url = "http://"+host+"/webService-Fatec/pegarDados.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                        nomeCasa.setText("  Casa da familia: "+alarmeNomeCasa);
                    }
                }
                catch (JSONException e)
                {
                    Toast.makeText(getContext(), "Erro ao recuperar dados do JSON: "+e, Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(), "Erro no JSON: "+error, Toast.LENGTH_LONG).show();
                Log.d("erroFragment", " " +error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> dado = new HashMap<>();
                dado.put("nome", sessaoUser.getText().toString());

                return dado;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void salvarDesativarPortao(String URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("getEstadoPortao", "Salvou estado do portao no MySQL");
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("getErrorEstadoPortao", error.getMessage());
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
                    parametros.put("estado_Android", "Fechado");
                    parametros.put("hora_Android", getHoraAtual());
                    parametros.put("data_Android", getDataAtual());
                    parametros.put("dia_Android", getDiaSemana());
                }
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


        fecharPortao("http://"+host+"/webService-Fatec/recebe.php?codigo="+3);
    }

    public void salvarAtivarPortao(String URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("getEstadoPortao", "Salvou estado do portao no MySQL");
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("getErrorEstadoPortao", error.getMessage());
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
                    parametros.put("estado_Android", estadoPortao);
                    parametros.put("hora_Android", getHoraAtual());
                    parametros.put("data_Android", getDataAtual());
                    parametros.put("dia_Android", getDiaSemana());
                }
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        abrirPortao("http://"+host+"/webService-Fatec/recebe.php?codigo="+2);
    }

    public void abrirPortao(String URL)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(getContext(),"Abrindo Portão.", Toast.LENGTH_SHORT).show();
                btnAbrirGaragem.setText("FECHAR GARAGEM");
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(), "Erro no JSON: "+error, Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void fecharPortao(String URL)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(getContext(),"Fechando Portão.", Toast.LENGTH_SHORT).show();
                btnAbrirGaragem.setText("ABRIR GARAGEM");
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(), "Erro no JSON: "+error, Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
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

    public String getDiaSemana()
    {
        Date d = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        String diaSemana = "";
        int dia = c.get(c.DAY_OF_WEEK);
        switch(dia)
        {
            case Calendar.SUNDAY: diaSemana = "Domingo";break;
            case Calendar.MONDAY: diaSemana = "Segunda";break;
            case Calendar.TUESDAY: diaSemana = "Terca";break;
            case Calendar.WEDNESDAY: diaSemana = "Quarta";break;
            case Calendar.THURSDAY: diaSemana = "Quinta";break;
            case Calendar.FRIDAY: diaSemana = "Sexta";break;
            case Calendar.SATURDAY: diaSemana = "Sabado";break;
        }

        return diaSemana;
    }

    public void ativarAlarme()
    {
        if(btnAtivarAlarme.getText().equals("ATIVAR ALARME"))
        {
            BottomSheetDialogAtivarAlarme bottomSheetDialogAtivarAlarme = new BottomSheetDialogAtivarAlarme();
            bottomSheetDialogAtivarAlarme.show(getFragmentManager(), "Ativar Alarme");
        }
        else if(btnAtivarAlarme.getText().equals("DESATIVAR ALARME"))
        {
           BottomSheetDialogDesativarAlarme bottomSheetDialogDesativarAlarme = new BottomSheetDialogDesativarAlarme();
           bottomSheetDialogDesativarAlarme.show(getFragmentManager(), "Desativar Alarme");
        }
    }

    public void verificarUser()
    {
        String getNomeLogado = usuarioAtual.getDisplayName();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        dialog.setMessage(Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<medium><font color =\"#01C7D2\">Olá "+getNomeLogado+
                ", seja bem vindo!</font>" +
                "<small><p align=left><center>Você cadastrou sua residência no aplicativo, agora você é o administrador." +
                " Primeiro você deverá criar uma senha para o alarme e poderá alterá-la caso desejar."+
                " Além disso, ainda poderá adicionar outros usuários ou torná-los administradores.<br><br>" +
                "Agradecemos por usar nosso aplicativo.</center></small></p>"));
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(getContext(), SenhaAlarmeActivity.class);
                startActivity(intent);
            }
        });
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }

}
