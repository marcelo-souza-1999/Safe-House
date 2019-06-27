package com.example.safehouse.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import com.example.safehouse.R;
import com.example.safehouse.firebase.ConfiguracaoFirebase;
import com.example.safehouse.firebase.UsuarioFirebase;
import com.example.safehouse.fragments.CameraFragment;
import com.example.safehouse.fragments.ConfiguracoesFragment;
import com.example.safehouse.fragments.GraficosFragment;
import com.example.safehouse.fragments.PerfilFragment;
import com.example.safehouse.fragments.PrincipalFragment;
import com.example.safehouse.fragments.SobreFragment;
import com.example.safehouse.fragments.UsersFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CircleImageView imageUser;
    private TextView nomeUsuario, emailUsuario;
    private String nomeRecuperado, emailRecuperado;
    private FirebaseAuth autenticacao;
    private FrameLayout frameLayout;
    private GoogleSignInResult result;
    private FirebaseUser userFirebase;

    @Override
    protected void onStart() {
        super.onStart();

        userFirebase = UsuarioFirebase.getUsuarioAtual();
        Uri url = userFirebase.getPhotoUrl();

        if (url != null) {
            Glide.with(PrincipalActivity.this)
                    .load(url)
                    .into(imageUser);
        } else {
            imageUser.setImageResource(R.drawable.foto_perfil);
        }

        nomeUsuario.setText(userFirebase.getDisplayName());
        emailUsuario.setText(userFirebase.getEmail());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        if (savedInstanceState != null) {
            frameLayout = findViewById(R.id.frameContainer);

            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

            Toolbar toolbar = findViewById(R.id.toolbarSafeHouse);
            toolbar.setTitle("Safe House");
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);

            imageUser = headerView.findViewById(R.id.imagem_logo_app);
            nomeUsuario = headerView.findViewById(R.id.textViewNomePrincipal);
            emailUsuario = headerView.findViewById(R.id.textViewEmailPrincipal);

            nomeUsuario.setText(nomeRecuperado);
            emailUsuario.setText(emailRecuperado);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                    (
                            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
                    );
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
        } else {
            frameLayout = findViewById(R.id.frameContainer);

            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

            Toolbar toolbar = findViewById(R.id.toolbarSafeHouse);
            toolbar.setTitle("Safe House");
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);

            imageUser = headerView.findViewById(R.id.imagem_logo_app);
            nomeUsuario = headerView.findViewById(R.id.textViewNomePrincipal);
            emailUsuario = headerView.findViewById(R.id.textViewEmailPrincipal);

            nomeUsuario.setText(nomeRecuperado);
            emailUsuario.setText(emailRecuperado);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                    (
                            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
                    );
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);

            PrincipalFragment principalFragment = new PrincipalFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, principalFragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_principal) {
            PrincipalFragment principalFragment = new PrincipalFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, principalFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_camera) {
            CameraFragment cameraFragment = new CameraFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, cameraFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_configuracoes) {
            ConfiguracoesFragment configuracoesFragment = new ConfiguracoesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, configuracoesFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_conta) {
            PerfilFragment perfilFragment = new PerfilFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, perfilFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_graficos) {
            GraficosFragment graficosFragment = new GraficosFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, graficosFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_usuarios) {
            UsersFragment usersFragment = new UsersFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, usersFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_logout) {
            deslogarUsuario();
        } else if (id == R.id.nav_sobre) {
            SobreFragment sobreFragment = new SobreFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, sobreFragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deslogarUsuario() {
        try {
            String provedor = "";
            FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

            if (usuarioAtual != null) {
                for (UserInfo profile : usuarioAtual.getProviderData()) {
                    provedor = profile.getProviderId();

                    if (provedor.equals("password")) {
                        autenticacao.signOut();
                        Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (provedor.equals("google.com")) {
                        autenticacao.signOut();
                        Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
