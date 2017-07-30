package com.example.elberg.ayudate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elberg.ayudate.Resources.Registry;
import com.example.elberg.ayudate.Resources.Request;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    Button btn_iniciar,btn_registar;
    SignInButton btnSignIn;
    TextView txtNombre,txtEmail;
    private ProgressDialog mProgressDialog;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
    }

    /**
     * Escucha de los botones
     */
    private void btnListener() {
        btnSignIn.setOnClickListener(new View.OnClickListener(){;

            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, Request.RC_SIGN_IN );
            }
        });

    }

    /**
     * Gestiona la seguridad de la cuenta una vez seleccionada la cuenta
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request.RC_SIGN_IN) {
            GoogleSignInResult result =
                    Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     * Verifica que el resultado del login a sido exitoso
     * @param result
     */

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            //Usuario logueado --> Mostramos sus datos
            GoogleSignInAccount acct = result.getSignInAccount();
            txtNombre.setText(acct.getDisplayName());
            txtEmail.setText(acct.getEmail());
            updateUI(true);
        } else {
            //Usuario no logueado --> Lo mostramos como "Desconectado"
            updateUI(false);
        }
    }

    private void updateUI(boolean b) {
        if (b) {
          //  btnSignIn.setVisibility(View.GONE);

        } else {
            txtNombre.setText("Desconectado");
            txtEmail.setText("Desconectado");

            btnSignIn.setVisibility(View.VISIBLE);

        }
    }

    /**
     * Inicializa los componentes
     */
    public void findView (){
        btn_iniciar = (Button) findViewById(R.id.btn_iniciarsesion);
        btn_registar = (Button) findViewById(R.id.btn_registrarCuenta);
        btnSignIn = (SignInButton)findViewById(R.id.sign_in_button);
        txtEmail = (TextView)findViewById(R.id.idemail);
        txtNombre = (TextView)findViewById(R.id.idnom);
        styleText();
        signIn();

    }

    /**
     * Para inicializar variables del signin de google
     */
    public void signIn (){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnListener();
    }

    /**
     * Cambia el tipo de letra de los componentes referenciados
     */
    public void styleText(){
        Typeface TF = Typeface.createFromAsset(getAssets(), Registry.FONT_PATH);

        btn_iniciar.setTypeface(TF);
        btn_registar.setTypeface(TF);
    }


    /**
     * Error de conexion
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion!", Toast.LENGTH_SHORT).show();
        Log.e("GoogleSignIn", "OnConnectionFailed: " + connectionResult);
    }

    @Override
    public void onClick(View v) {

    }

}
