package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private TextView appName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName=findViewById(R.id.app_name);

        mAuth=FirebaseAuth.getInstance();

        // Access a Cloud Firestore instance from your Activity
        DbQuery.g_firestore = FirebaseFirestore.getInstance();

        new Thread()
        {
            @Override
            public void run()
            {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(mAuth.getCurrentUser() != null)
                {

                    DbQuery.loadData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {

                            Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();

                        }

                        @Override
                        public void onFailure() {

                            Toast.makeText(SplashActivity.this, "Something went wrong ! Plz try again",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                else
                {
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }


            }
        }.start();
    }
}