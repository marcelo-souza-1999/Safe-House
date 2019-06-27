package com.example.safehouse.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.safehouse.R;

public class UsersFragment extends Fragment {

    private WebView navegador;
    private String endSite = "http://192.168.0.109/safehouse/usuarioscadastrados2.php";

    public UsersFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        navegador = view.findViewById(R.id.webViewUsers);

        navegador.setWebViewClient(new UsersFragment.MyBrowser());

        abrirPagina();

        if (savedInstanceState != null) {
            navegador.restoreState(savedInstanceState);
        } else {
            navegador.loadUrl(endSite);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        navegador.saveState(outState);
    }

    private void abrirPagina() {
        navegador.getSettings().setLoadsImagesAutomatically(true);
        navegador.getSettings().setJavaScriptEnabled(true);
        navegador.getSettings().setAppCacheEnabled(true);
        navegador.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        navegador.loadUrl(endSite);
    }

    private class MyBrowser extends WebViewClient {
        public boolean overrideUrlLoading(WebView view, String url) {
            return true;
        }
    }
}
