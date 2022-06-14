package com.example.db;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class DetailPageAct extends AppCompatActivity {

    ImageButton deleteAlert;
    TextView dateT;
    EditText nameEt, topicEt, descriptionEt;
    Button deleteBtn,updateBtn;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();

        getDataFromAdapter();
        delete();
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu_item, menu);

        MenuItem delete = menu.findItem(R.id.moreBarDeleteDetail);
        MenuItem help = menu.findItem(R.id.moreBarHelpDetail);

        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                deleteFunction();
                goBackMainAct();

                return false;
            }
        });

        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Toast.makeText(getApplicationContext(), "Please Describe Your Issue", Toast.LENGTH_LONG).show();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+"MySoftwareCompany@MySoftwareCompany.com" ));
                String subject="Please describe your issue here";
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(Intent.createChooser(emailIntent, "Chooser Title"));

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void getDataFromAdapter(){

        Intent intent=getIntent();

        String name=intent.getStringExtra("notename");
        String dates=intent.getStringExtra("dates");
        String description=intent.getStringExtra("description");
        String topic=intent.getStringExtra("topic");

        topicEt =findViewById(R.id.detailTopicText);
        dateT=findViewById(R.id.detailDateText);
        descriptionEt =findViewById(R.id.detailDescriptionText);
        nameEt =findViewById(R.id.detailNameText);

        nameEt.setText(name);
        topicEt.setText(topic);
        dateT.setText(dates);
        descriptionEt.setText(description);

    }

    public void delete(){
        deleteBtn=findViewById(R.id.detailDeleteButton);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater=getLayoutInflater();
                View v=inflater.inflate(R.layout.delete_alert_dialog,null);

                deleteAlert= v.findViewById(R.id.deleteButtonAlert);

                AlertDialog.Builder alert=new AlertDialog.Builder(DetailPageAct.this);
                alert.setView(v);
                alert.setCancelable(true);
                AlertDialog dialog=alert.create();

                deleteAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteFunction();
                        goBackMainAct();
                    }
                });
                dialog.show();
            }
        });
    }


    public void deleteFunction(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                //date is primary key
                //user cannot add note more than one at same time

                Intent intent=getIntent();
                String dates=intent.getStringExtra("dates");

                NoteModel model=realm.where(NoteModel.class).equalTo("time",dates).findFirst();
                model.deleteFromRealm();

            }
        });
    }


    public void update(){
        updateBtn=findViewById(R.id.detailUpdateButton);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFunc();
                goBackMainAct();
            }
        });
    }

    public void updateFunc(){

        Intent intent=getIntent();
        String dates=intent.getStringExtra("dates");

        topicEt =findViewById(R.id.detailTopicText);
        dateT=findViewById(R.id.detailDateText);
        descriptionEt =findViewById(R.id.detailDescriptionText);
        nameEt =findViewById(R.id.detailNameText);

        String notname=nameEt.getText().toString();
        String nottopic=topicEt.getText().toString();
        String desc=descriptionEt.getText().toString();

        NoteModel model=realm.where(NoteModel.class).equalTo("time",dates).findFirst();

        realm.beginTransaction();
        model.setDescription(desc);
        model.setNotename(notname);
        model.setTopic(nottopic);
        realm.commitTransaction();
    }


    public void goBackMainAct(){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

}