package com.example.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText Email,Password;
    private Button Login_button;
    private TextView forgot_pass, Signup_button;
    private FirebaseAuth mAuth;
    private Dialog progressDailog;
    private TextView dialogText;
    private RelativeLayout gSignB;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email =findViewById(R.id.email);
        Password=findViewById(R.id.password);
        Login_button=findViewById(R.id.login_button);
        forgot_pass=findViewById(R.id.forgot_pass);
        Signup_button=findViewById(R.id.signup_button);
        gSignB=findViewById(R.id.google_signin_button);

        progressDailog=new Dialog(LoginActivity.this);
        progressDailog.setContentView(R.layout.dailog_layout);
        progressDailog.setCancelable(false);
        progressDailog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText=progressDailog.findViewById(R.id.dailog_text);
        dialogText.setText("Signing in...");

        mAuth=FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);



        Login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateData())
                {
                    login();
                }

            }
        });

        Signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        gSignB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

    }

    private  boolean ValidateData()
    {

        if (Email.getText().toString().isEmpty())
        {
            Email.setError("ENTER EMAIL ID");
            return false;
        }

        if (Password.getText().toString().isEmpty())
        {
            Password.setError("ENTER PASSWORD");
            return false;
        }

        return true;
    }

    private void login()
    {

        progressDailog.show();
        mAuth.signInWithEmailAndPassword(Email.getText().toString().trim(), Password.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_SHORT).show();

                            DbQuery.loadData(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {

                                    progressDailog.dismiss();
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure() {

                                    progressDailog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Something went wrong ! Plz try again",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDailog.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            // ...
                        }

                        // ...
                    }
                });
    }

    private  void googleSignIn()
    {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // Log.w(TAG, "Google sign in failed", e);
                // ...
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
        private void firebaseAuthWithGoogle(String idToken) {


            progressDailog.show();
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                Toast.makeText(LoginActivity.this,"Google Sign In Success",Toast.LENGTH_SHORT).show();

                                FirebaseUser user = mAuth.getCurrentUser();

                                if(task.getResult().getAdditionalUserInfo().isNewUser())
                                {
                                    DbQuery.createUserData(user.getEmail(), user.getDisplayName(), new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {

                                            DbQuery.loadData(new MyCompleteListener() {
                                                @Override
                                                public void onSuccess() {

                                                    progressDailog.dismiss();
                                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                                    startActivity(intent);
                                                    LoginActivity.this.finish();

                                                }

                                                @Override
                                                public void onFailure() {

                                                    progressDailog.dismiss();
                                                    Toast.makeText(LoginActivity.this, "Something went wrong ! Plz try again",
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                            });


                                        }

                                        @Override
                                        public void onFailure() {

                                            progressDailog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Something went wrong ! Plz try again",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else
                                {
                                    DbQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {

                                            progressDailog.dismiss();
                                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                            startActivity(intent);

                                        }

                                        @Override
                                        public void onFailure() {

                                            progressDailog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Something went wrong ! Plz try again",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }


                            } else {
                                // If sign in fails, display a message to the user.
                                progressDailog.dismiss();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }