package com.computermon.monitorapp;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class listProcessActivity extends ListActivity {
    private TextView text;
    private List<String> listValues, listProcess;
    private ArrayAdapter<String> myAdapter;
    private String TAG="Process", email,macneeded,proce;
    private ListView listView;
    private  String listItemName;
    private String m_Text;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_process);
        initList();
        search=(EditText) findViewById(R.id.txtsearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    initList();
                }
                else{
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void searchItem(String look){

        Iterator<String> iter= listValues.iterator();
        while(iter.hasNext()){
            String str=iter.next();
            if(!str.contains(look)){
                iter.remove();
            }
        }
        myAdapter.notifyDataSetChanged();

    }
    public void initList(){

               proce=getIntent().getStringExtra("processes");
               listValues.add(proce);

                text = (TextView) findViewById(R.id.mainText);
                myAdapter = new ArrayAdapter <String>(getApplicationContext(), R.layout.row_layout, R.id.listText, listValues);
                setListAdapter(myAdapter);
                registerForContextMenu(listView);

            }



    }









