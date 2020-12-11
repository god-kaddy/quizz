package com.example.quizz;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText name,email,pass,confirmPass;
    private Button signUp_button;
    private ImageView back_button;
    private FirebaseAuth mAuth;
    private  String emailStr, passStr, confirmPassStr, nameStr;
    private Dialog progressDailog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=findViewById(R.id.username);
        email=findViewById(R.id.emailID);
        pass=findViewById(R.id.password);
        confirmPass=findViewById(R.id.confirm_pass);
        signUp_button=findViewById(R.id.signup_button);
        back_button=findViewById(R.id.back_button);

        progressDailog=new Dialog(SignUpActivity.this);
        progressDailog.setContentView(R.layout.dailog_layout);
        progressDailog.setCancelable(false);
        progressDailog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText=progressDailog.findViewById(R.id.dailog_text);
        dialogText.setText("Registering User..");

        mAuth=FirebaseAuth.getInstance();


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()) {
                    signupNewUser();
                }
            }
        });
    }

    private  boolean validate()
    {
        nameStr=name.getText().toString().trim();
        passStr=pass.getText().toString().trim();
        emailStr=email.getText().toString().trim();
        confirmPassStr=confirmPass.getText().toString().trim();

        if(nameStr.isEmpty())
        {
            name.setError("Enter Your Name");
            return false;
        }
        if(emailStr.isEmpty())
        {
            email.setError("Enter Your Email Id");
            return false;
        }
        if(passStr.isEmpty())
        {
            pass.setError("Enter Password");
            return false;
        }
        if(confirmPassStr.isEmpty())
        {
            confirmPass.setError("Reenter Your Password");
            return false;
        }

        if(passStr.compareTo(confirmPassStr) !=0)
        {
            Toast.makeText(SignUpActivity.this, "Password and Confirm Password should be same !",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signupNewUser()
    {
        progressDailog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Sign up Successful",Toast.LENGTH_SHORT).show();

                            DbQuery.createUserData(emailStr,nameStr, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    progressDailog.dismiss();
                                    Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    SignUpActivity.this.finish();
                                }

                                @Override
                                public void onFailure() {

                                    Toast.makeText(SignUpActivity.this,"Something went wrong...! Plz try again",Toast.LENGTH_SHORT).show();

                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDailog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


}