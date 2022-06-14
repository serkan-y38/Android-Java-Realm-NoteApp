package com.example.db;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import io.realm.Realm;
import io.realm.RealmResults;

public class AddNoteAct extends AppCompatActivity {

    Button Addbtn;
    EditText noteNameEt, topicNameEt, descriptionEt;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm= Realm.getDefaultInstance();
        add();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu_item, menu);

        MenuItem help = menu.findItem(R.id.moreBarHelpAdd);

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

    public void add(){

        noteNameEt=findViewById(R.id.addNoteName);
        topicNameEt=findViewById(R.id.addTopicName);
        descriptionEt=findViewById(R.id.addDescription);
        Addbtn=findViewById(R.id.addNoteButton);

        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name=noteNameEt.getText().toString();
                final String topic=topicNameEt.getText().toString();
                final String desc=descriptionEt.getText().toString();
                final String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

                if(name.isEmpty() || desc.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Fill The Required Fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    insert(name,topic,desc,timeStamp);
                }
            }
        });
    }

    public void insert(final String name,final String topic, final String desc, final String date){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                NoteModel model=realm.createObject(NoteModel.class);
                model.setTopic(topic);
                model.setNotename(name);
                model.setDescription(desc);
                model.setTime(date);

                goBackMainAct();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"Note Was Added Successfully",Toast.LENGTH_LONG).show();
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
        RealmResults<NoteModel> models=realm.where(NoteModel.class).findAll();
        for(NoteModel m:models){
            Log.i("models",m.toString());
        }
    }

    public void goBackMainAct(){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}