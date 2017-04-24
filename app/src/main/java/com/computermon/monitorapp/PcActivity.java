package com.computermon.monitorapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PcActivity extends AppCompatActivity {

        private DatabaseReference mFirebaseDatabase;
        private FirebaseDatabase mFirebaseInstance;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseUser mFirebaseUser;
        private String userId,macadress;
        private EditText names;
        private EditText mac;
        private Button macSave;
        private Vibrator vib;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pc);
            mac=(EditText) findViewById(R.id.Mac);
             macSave = (Button) findViewById(R.id.btn_save);
            macSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (!validate()) {
//                        onRegisterFailed();
//                        return;
//                    }
                    macadress=mac.getText().toString();
//                    if(!isValidName(name)){
//                        vib= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vib.vibrate(120);
//                        Toast.makeText(getApplicationContext(),"Not a valid Name entered", Toast.LENGTH_LONG).show();
//
//                        return;
//                    }
//                    else{
                        createUser(macadress);
                        Toast.makeText(getApplicationContext(), "Thanks, Mac Saved!", Toast.LENGTH_LONG).show();
                        finish();
//                    }

                }
            });
        }

    private void createUser(String macadress) {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Pc");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String email=mFirebaseUser.getEmail();

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseUser.getUid();
        }

        Pc pc = new Pc(email,macadress);

        mFirebaseDatabase.push().setValue(pc);

        // addUserChangeListener();
    }
    private void addUserChangeListener() {
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            public static final String TAG ="KId" ;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pc pc = dataSnapshot.getValue(Pc.class);
                if (pc == null) {
                    Log.e(TAG, "Pc data is null!");
                    return;
                }
                Log.e(TAG, "Pc data is changed!" + pc.email +  ", " + pc.mac);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read Pc");
            }
        });
    }

//    public boolean validate() {
//        boolean valid = true;
//
//        String mName = names.getText().toString();
//        String mKidmail = kidemail.getText().toString();
//
//        if (mKidmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mKidmail).matches()) {
//            kidemail.setError("enter a valid email address");
//            valid = false;
//        } else {
//            kidemail.setError(null);
//        }
//
//        if (mName.isEmpty()) {
//            names.setError("Kid Names Must not be empty");
//            valid = false;
//        } else {
//            names.setError(null);
//        }
//
//        return valid;
//    }
//
//    public void onRegisterFailed() {
//        Toast.makeText(getBaseContext(), "Adding Kid info failed", Toast.LENGTH_LONG).show();
//
//        kidSave.setEnabled(true);
//    }
//    private boolean isValidName(String name){
//
//        if(name.trim().isEmpty() || name.length()<5 ||!Pattern.matches(".*[a-zA-Z]+.*[a-zA-Z]",name)){
//            names.setError("invalid name");
//            return false;
//
//
//        }
//        return true;
//    }
}
