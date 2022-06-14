package com.example.db;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<NoteModel> listModel=new ArrayList<>();
    Realm realm;
    Adapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm=Realm.getDefaultInstance();

        startAddNoteAct();
        showDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_item, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search_view);
        MenuItem help = menu.findItem(R.id.moreBarHelp);

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

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adp=new Adapter(listModel,getApplicationContext());
                listView.setAdapter(adp);

                filter(newText.toLowerCase());
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text) {

        ArrayList<NoteModel> filteredlist = new ArrayList<>();
        for (NoteModel item : listModel) {
            if (item.getNotename().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "Note Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adp.filterList(filteredlist);
        }
    }

    public void startAddNoteAct(){

        FloatingActionButton addBtn=findViewById(R.id.addNoteFloating);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), AddNoteAct.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void showDb(){

        listView=findViewById(R.id.listView);
        RealmResults<NoteModel> results=realm.where(NoteModel.class).findAll();

        for(NoteModel m:results){
            listModel.add(m);
        }

        if(results.size()>0){
            Adapter adapter=new Adapter(listModel,getApplicationContext());
            listView.setAdapter(adapter);
        }

    }

}