package com.computermon.monitorapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.*;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListPcActivity extends ListActivity {
    private TextView text;
    private List<String> listValues, listProcess;
    private ArrayAdapter<String> myAdapter;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDB;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String selectedItem;
    private String TAG="Pc", email,macneeded,id;
    private List<Process_> proce;
    private ListView listView;
    private  String listItemName;
    private String m_Text;
    private EditText search;
    private  AlertDialog.Builder builder;


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

        Iterator <String> iter= listValues.iterator();
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
                    listValues.add(child.getValue(Pc.class).getMac());

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
        Intent intent = new Intent(ListPcActivity.this, listProcessActivity.class);
        intent.putExtra("macadress",selectedItem);
        finish();
        startActivity(intent);

        // text.setText("You clicked " + selectedItem + " at position " + position);
    }


    private void requestFocus (View view){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

}
