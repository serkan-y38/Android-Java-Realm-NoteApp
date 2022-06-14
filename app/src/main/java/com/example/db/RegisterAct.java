package com.example.db;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import io.realm.Realm;
import io.realm.RealmResults;

public class RegisterAct extends AppCompatActivity {

    Button backToLoginBtn;
    EditText usernameEt, passwordEt, passwordagainEt, emailEt;
    AppCompatButton signupbtn;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        realm=Realm.getDefaultInstance();

        Register();
        backToLogin();
    }

    public void backToLogin(){
        backToLoginBtn=findViewById(R.id.backToLoginBtn);
        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginAct.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void Register(){

        usernameEt=findViewById(R.id.signupNameEt);
        passwordEt=findViewById(R.id.signupPasswordEt);
        passwordagainEt=findViewById(R.id.signupPasswordAgainEt);
        emailEt=findViewById(R.id.signupMailEt);
        signupbtn=findViewById(R.id.signupNowButton);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = usernameEt.getText().toString();
                final String password = passwordEt.getText().toString();
                final String passwordagain = passwordagainEt.getText().toString();
                final String mail = emailEt.getText().toString();

                if (!password.equals(passwordagain)) {
                    Toast.makeText(getApplicationContext(), "Passwords Different", Toast.LENGTH_LONG).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill The Required Fields", Toast.LENGTH_LONG).show();
                } else if (passwordagain.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill The Required Fields", Toast.LENGTH_LONG).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill The Required Fields", Toast.LENGTH_LONG).show();
                } else {
                    insertUserDb(username, password, mail);
                }
            }
        });
    }

    public void insertUserDb(final String username, final String password, final String email){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                UserModel model=realm.createObject(UserModel.class);
                model.setMail(email);
                model.setUsername(username);
                model.setPassword(password);

                usernameEt.setText("");
                emailEt.setText("");
                passwordagainEt.setText("");
                passwordEt.setText("");

                goBackSignUp();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"Account Was Created Successfully",Toast.LENGTH_LONG).show();
                LogDb();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void LogDb(){
        RealmResults<UserModel> models=realm.where(UserModel.class).findAll();
        for(UserModel m:models){
            Log.i("members",m.toString());
        }
    }

    public void goBackSignUp(){

        usernameEt=findViewById(R.id.signupNameEt);
        passwordEt=findViewById(R.id.signupPasswordEt);

        String username=usernameEt.getText().toString();
        String password=passwordEt.getText().toString();

        Intent intent=new Intent(getApplicationContext(),LoginAct.class);
        intent.putExtra("username",username);
        intent.putExtra("password",password);
        startActivity(intent);
        finish();
    }

}