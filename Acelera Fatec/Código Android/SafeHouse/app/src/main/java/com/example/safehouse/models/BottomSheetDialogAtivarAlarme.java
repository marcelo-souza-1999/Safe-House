package com.example.safehouse.models;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.safehouse.fragments.PrincipalFragment;
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

public class BottomSheetDialogAtivarAlarme extends BottomSheetDialogFragment
{
    private TextView confirmarSenha;
    private Button btnAtivarAlarme;
    private EditText insereSenha;
    private String getCampoSenha = "", estadoAlarme = "";
    private static ConectarWebService conectarWebService;
    private static String host = conectarWebService.getEndServer();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_ativar_alarme, container, false);

        insereSenha = view.findViewById(R.id.editTextAtivarAlarme);

        btnAtivarAlarme = view.findViewById(R.id.buttonAtivarAlarme);

        confirmarSenha = view.findViewById(R.id.textViewConfirmarSenha);

        confirmarSenha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                verificarCampo();
            }
        });

        getDiaSemana();

        return view;
    }

    public void verificarCampo()
    {
        if(insereSenha.getText().length() < 6)
        {
            Toast.makeText(getContext(), "A senha precisa conter 6 dígitos", Toast.LENGTH_LONG).show();
        }
        else
        {
            validarSenha();
        }
    }

    public void validarSenha()
    {
        getCampoSenha = insereSenha.getText().toString();

        String url = "http://"+host+"/webService-Fatec/pegarSenha.php";
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
                        JSONObject senhaJsonObj = jsonArray.getJSONObject(i);
                        String senhaAlarme = senhaJsonObj.getString("senha");
                        String getJson = senhaAlarme;

                        String comparaSenha = getCampoSenha;

                        if(comparaSenha.equals(getJson))
                        {
                            Toast.makeText(getContext(),"Alarme Ativado", Toast.LENGTH_LONG).show();
                            estadoAlarme = "Ativado";
                            salvarEstadoAlarme("http://"+host+"/webService-Fatec/estadoAlarme.php");
                            dismiss();
                        }
                        else if(!comparaSenha.equals(getJson))
                        {
                            Toast.makeText(getContext(),"Senha inválida", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                catch (JSONException e)
                {
                    String texto = "Seu alarme está sem senha\nCrie uma e tente novamente.";
                    Spannable centralizarToast = new SpannableString(texto);
                    centralizarToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                            0, texto.length() - 1,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    Toast.makeText(getContext(), centralizarToast,Toast.LENGTH_LONG).show();
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
                dado.put("alarme_Android", getCampoSenha);

                return dado;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void salvarEstadoAlarme(String URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("getEstadoAlarme", "Salvou estado do alarme no MySQL");
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("getErrorEstadoAlarme", error.getMessage());
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
                    parametros.put("estado_Android", estadoAlarme);
                    parametros.put("hora_Android", getHoraAtual());
                    parametros.put("data_Android", getDataAtual());
                    parametros.put("dia_Android", getDiaSemana());
                }
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        updateEstadoAlarme("http://"+host+"/webService-Fatec/updateEstadoAlarme.php");

        ativarAlarme();
        restartFragment();
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

    public void ativarAlarme()
    {
        String url = "http://"+host+"/webService-Fatec/recebe.php?codigo="+1;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(), "Erro na ativação do alarme: "+error, Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

        dismiss();

        restartFragment();
    }

    public void updateEstadoAlarme(String URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("getUpdateDados", "Atualizou estado do alarme");
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("getErrorUpdateAlarme", error.getMessage());
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
                    parametros.put("estado_Android", "Ativado");
                }
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
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

    private void restartFragment()
    {
        PrincipalFragment principalFragment = new PrincipalFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer,principalFragment);
        fragmentTransaction.commit();
    }
}
