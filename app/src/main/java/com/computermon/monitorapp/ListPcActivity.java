package com.computermon.monitorapp;

import android.app.ListActivity;
import android.content.DialogInterface;
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

public class ListPcActivity extends ListActivity {
    private TextView text;
    private List<String> listValues;
    private ArrayAdapter<String> myAdapter;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDB;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String selectedItem;
    private String TAG="Pc", email,macneeded;
    private ListView listView;
    private  String listItemName;
    private String m_Text;
    private EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pc);
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
        mFirebaseDatabase = mFirebaseInstance.getReference("Pc");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        email=mFirebaseUser.getEmail();
        listView = (ListView) findViewById(android.R.id.list);
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listValues = new ArrayList<String>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.getValue(Pc.class).getEmail().equals(email)) {
                        listValues.add(child.getValue(Pc.class).getMac());
                    }
                }

                text = (TextView) findViewById(R.id.mainText);
                myAdapter = new ArrayAdapter <String>(getApplicationContext(), R.layout.row_layout, R.id.listText, listValues);
                setListAdapter(myAdapter);
                registerForContextMenu(listView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"Error occured "+databaseError.getCode());
            }
        });

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle((String) getListView().getItemAtPosition(info.position));
        String [] menuItems =getResources().getStringArray(R.array.menu);
        for (int i=0;i<menuItems.length;i++){
            menu.add(Menu.NONE,i,i,menuItems[i]);

        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex= item.getItemId();
        String [] menuItems =getResources().getStringArray(R.array.menu);
        String menuItemName =menuItems [menuItemIndex];
        listItemName =(String) getListView().getItemAtPosition(info.position);
        if (menuItemName.equals("Update"))
        {
            m_Text = "";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Update "+ listItemName);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                        mFirebaseDatabase.orderByChild("mac")
                                .equalTo(listItemName)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChildren()) {
                                            DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                            firstChild.getRef().child("mac").setValue(m_Text);
                                            Toast.makeText(getApplicationContext(),"Mac updated Successfully!", Toast.LENGTH_LONG).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d(TAG, "Error occured " + databaseError.getCode());
                                    }
                                });
                        myAdapter.notifyDataSetChanged();
                    }

            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

            myAdapter.notifyDataSetChanged();


            // Toast.makeText(getApplicationContext(),"Edit is Selected "+ " On "+listItemName, Toast.LENGTH_LONG).show();

        }
        if (menuItemName.equals("Delete")){
            AlertDialog.Builder adb=new AlertDialog.Builder(ListPcActivity.this);
            adb.setTitle("Delete?");
            adb.setMessage("Are you sure you want to delete " + listItemName);
            adb.setNegativeButton("Cancel", null);

            adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFirebaseDatabase.orderByChild("mac")
                            .equalTo(listItemName)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChildren()){
                                        DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                        firstChild.getRef().removeValue();
                                        Toast.makeText(getApplicationContext(),"Mac Delete", Toast.LENGTH_LONG).show();

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

    @Override

    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        selectedItem = (String) getListView().getItemAtPosition(position);
        //String selectedItem = (String) getListAdapter().getItem(position);
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.getValue(Pc.class).getMac().equals(selectedItem)) {
                        macneeded=child.getValue(Pc.class).getMac();
                        Log.d(TAG,"email "+macneeded);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDB = mFirebaseInstance.getReference("processes");
        mFirebaseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot adress : dataSnapshot.getChildren()) {
                        Toast.makeText(getApplicationContext(),"Display its process here", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

