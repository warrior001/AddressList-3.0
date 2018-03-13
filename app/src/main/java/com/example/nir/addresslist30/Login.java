package com.example.nir.addresslist30;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Nir on 12/31/2017.
 */

    public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
        Button googleSignInBtn;
        GoogleApiClient mGoogleApiClient;
        private static final int RC_SIGN_IN=9001;
        private static final String TAG="SignInActivity";
        private FirebaseAuth firebaseAuth;
        private String email;
        private String password;
        private EditText editTextEmail;
        private EditText editTextPassword;
        Button signInBtn;
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide();
        editTextEmail = (EditText) findViewById(R.id.editText13);
        editTextPassword= (EditText) findViewById(R.id.editText14);
        signInBtn= (Button) findViewById(R.id.button9);
        firebaseAuth = FirebaseAuth.getInstance();
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Login.this, "RIP " + task.getException(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleSignInBtn = (Button) findViewById(R.id.btnGoogleSignIn);
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

        private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            super.onActivityResult(requestCode,resultCode,data);

            if(requestCode==RC_SIGN_IN)
            {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }

        private void handleSignInResult(GoogleSignInResult result)
        {
            Log.d(TAG, "handleSignInResult:"+result.isSuccess());
            if(result.isSuccess())
            {
                GoogleSignInAccount acct= result.getSignInAccount();
                System.out.println("Hello, "+acct.getDisplayName());
                Intent nextActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(nextActivity);
            }
            else
            {

            }
        }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed:"+connectionResult);
    }

    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                System.out.println("Signed out!");
            }
        });
    }

}
