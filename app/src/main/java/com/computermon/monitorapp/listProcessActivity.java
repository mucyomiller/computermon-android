package com.computermon.monitorapp;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
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
    private List<String> listValues;
    private ArrayAdapter<String> myAdapter;
    private String TAG="Process";
    private String email;
    private String macneeded;
    private List<Process_> proce;
    private ListView listView;
    private  String listItemName;
    private String mac;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDB;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mFirebaseAuth;
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



        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDB = mFirebaseInstance.getReference("processes");
        listView = (ListView) findViewById(android.R.id.list);
        mFirebaseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot adress : dataSnapshot.getChildren()) {
                    Intent a =getIntent();
                    mac=a.getStringExtra("macadress");

                    if(adress.getValue(Process.class).getMac().equals(mac)) {
                        proce=new ArrayList<Process_>();
                        proce=adress.getValue(Process.class).getProcess();



                    }

                }
                if(proce!=null && !proce.isEmpty()) {

                   // Toast.makeText(getApplicationContext(),proce.get(0).getPName()+ proce.size(), Toast.LENGTH_LONG).show();
                    listValues = new ArrayList<String>();
                    for(int i=0;i<proce.size();i++){
                        listValues.add(proce.get(i).getPName());
                    }
                    text = (TextView) findViewById(R.id.mainText);
                    myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.process_layout, R.id.listText, listValues);
                    setListAdapter(myAdapter);
                    registerForContextMenu(listView);


                }
                else{

                    Toast.makeText(getApplicationContext(),"No Application to show!", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



            }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle((String) getListView().getItemAtPosition(info.position));
        String [] menuItems =getResources().getStringArray(R.array.mn);
        for (int i=0;i<menuItems.length;i++){
            menu.add(Menu.NONE,i,i,menuItems[i]);

        }

    }
    @Override

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex= item.getItemId();
        String [] menuItems =getResources().getStringArray(R.array.mn);
        String menuItemName =menuItems [menuItemIndex];
        listItemName =(String) getListView().getItemAtPosition(info.position);
        if (menuItemName.equals("Close")){
            AlertDialog.Builder adb=new AlertDialog.Builder(listProcessActivity.this);
            adb.setTitle("Close Remotely?");
            adb.setMessage("Are you sure you want to remotely close the application" + listItemName);
            adb.setNegativeButton("Cancel", null);

            adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFirebaseDatabase.orderByChild("processes")
                            .equalTo(listItemName)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChildren()){
                                        DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                        firstChild.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d(TAG,"Error occured "+databaseError.getCode());
                                }
                            });
                    myAdapter.notifyDataSetChanged();
                }
            });
            adb.show();
            ///Toast.makeText(getApplicationContext(),"Delete is Selected "+ " On "+listItemName, Toast.LENGTH_LONG).show();

        }
        return true;

    }



    }









