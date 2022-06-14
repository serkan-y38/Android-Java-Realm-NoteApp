package com.example.db;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;

public class LoginAct extends AppCompatActivity {

    AppCompatButton loginBtn, signupBtn;
    EditText usernameEt, passwordEt;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        realm=Realm.getDefaultInstance();

        goRegisterAct();
        ifExist_getDataFromRegisterAct();
        loginAccount();
    }

    public void goRegisterAct(){
        signupBtn=findViewById(R.id.signupButton);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RegisterAct.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void ifExist_getDataFromRegisterAct() {

        Intent intent=getIntent();

        String name=intent.getStringExtra("username");
        String number=intent.getStringExtra("password");

        usernameEt=findViewById(R.id.loginNameEt);
        passwordEt=findViewById(R.id.loginPasswordEt);

        usernameEt.setText(name);
        passwordEt.setText(number);
    }

    public void loginAccount(){
        usernameEt=findViewById(R.id.loginNameEt);
        passwordEt=findViewById(R.id.loginPasswordEt);
        loginBtn=findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 String username=usernameEt.getText().toString();
                 String password=passwordEt.getText().toString();

                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Fill The Required Fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(getApplicationContext(),"Account Was Created Successfully",Toast.LENGTH_LONG).show();

                    UserModel user=realm.where(UserModel.class)
                            .equalTo("username",username)
                            .equalTo("password",password)
                            .findFirst();

                    if(user != null){
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        Intent intenty= new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intenty);
                    }else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
}