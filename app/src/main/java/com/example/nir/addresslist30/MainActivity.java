package com.example.nir.addresslist30;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edttxt1;
    private EditText edttxt2;
    private EditText edttxt3;
    private EditText edttxt4;
    private EditText edttxt5;
    private EditText edttxt6;
    private EditText edttxt7;
    private int locNum;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Active");
    private List users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        edttxt1 = (EditText) findViewById(R.id.editText3);
        edttxt2 = (EditText) findViewById(R.id.editText4);
        edttxt3 = (EditText) findViewById(R.id.editText5);
        edttxt4 = (EditText) findViewById(R.id.editText6);
        edttxt5 = (EditText) findViewById(R.id.editText7);
        edttxt6 = (EditText) findViewById(R.id.editText8);
        edttxt7 = (EditText) findViewById(R.id.editText9);





        setTextByLocNum();
        longClickSystemSwitcher();

    }


    public void readUser(final String Address)
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Total").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot userDS: dataSnapshot.getChildren())
                {
                    User user = userDS.getValue(User.class);
                    users.add(user);


                    //add other inputs...
                }
                for(int i=0;i<users.size();i++)
                {
                    User user = (User) users.get(i);
                    if(user.getAddress().equalsIgnoreCase(Address))
                        System.out.println("Address: "+user.getAddress()+ " Name: "+user.getName() );
                }


                System.out.println("Value is:"+ users);

                //Toast.makeText(MainActivity.this, "Loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }



    public void longClickSystemSwitcher()
    {
        edttxt1.setOnLongClickListener(new View.OnLongClickListener() { //---> testing without if statement: if(edttxt1.length()>0)....
            @Override
            public boolean onLongClick(View v) {
               if(edttxt1.length()>0)
               {
                   locNum=1;

                   Intent nextActivity = new Intent(getApplicationContext(), client_info.class);
                   nextActivity.putExtra("locNum", locNum);
                   nextActivity.putExtra("Address", edttxt1.getText().toString());
                   nextActivity.putExtra("isSaved",false);

                   startActivity(nextActivity);
                   return true;
               }


            return  false;
            }
        });

        edttxt2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(edttxt2.length()>0) {
                    locNum = 2;

                    Intent nextActivity = new Intent(getApplicationContext(), client_info.class);
                    nextActivity.putExtra("locNum", locNum);
                    nextActivity.putExtra("Address", edttxt2.getText().toString());
                    nextActivity.putExtra("isSaved",false);

                    startActivity(nextActivity);
                    return true;
                }
                return  false;
            }
        });

        edttxt3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(edttxt3.length()>0) {
                    locNum = 3;

                    Intent nextActivity = new Intent(getApplicationContext(), client_info.class);
                    nextActivity.putExtra("locNum", locNum);
                    nextActivity.putExtra("Address", edttxt3.getText().toString());
                    nextActivity.putExtra("isSaved",false);

                    startActivity(nextActivity);
                    return true;
                }
                return  false;
            }
        });

        edttxt4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(edttxt4.length()>0) {
                    locNum = 4;

                    Intent nextActivity = new Intent(getApplicationContext(), client_info.class);
                    nextActivity.putExtra("locNum", locNum);
                    nextActivity.putExtra("Address", edttxt4.getText().toString());
                    nextActivity.putExtra("isSaved",false);

                    startActivity(nextActivity);
                    return true;
                }
                return  false;
            }
        });

        edttxt5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(edttxt5.length()>0) {
                    locNum = 5;

                    Intent nextActivity = new Intent(getApplicationContext(), client_info.class);
                    nextActivity.putExtra("locNum", locNum);
                    nextActivity.putExtra("Address", edttxt5.getText().toString());
                    nextActivity.putExtra("isSaved",false);

                    startActivity(nextActivity);
                    return true;
                }
                return  false;
            }
        });

        edttxt6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(edttxt6.length()>0) {
                    locNum = 6;

                    Intent nextActivity = new Intent(getApplicationContext(), client_info.class);
                    nextActivity.putExtra("locNum", locNum);
                    nextActivity.putExtra("Address", edttxt6.getText().toString());
                    nextActivity.putExtra("isSaved",false);

                    startActivity(nextActivity);
                    return true;
                }
                return  false;
            }
        });

        edttxt7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(edttxt7.length()>0) {
                    locNum = 7;

                    Intent nextActivity = new Intent(getApplicationContext(), client_info.class);
                    nextActivity.putExtra("locNum", locNum);
                    nextActivity.putExtra("Address", edttxt7.getText().toString());
                    nextActivity.putExtra("isSaved",false);

                    startActivity(nextActivity);
                    return true;
                }
                return  false;
            }
        });
    }

    public void setTextByLocNum( )
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Active").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userDS: dataSnapshot.getChildren())
                {
                    ActiveUser user = userDS.getValue(ActiveUser.class);
                    users.add(user);


                    //add other inputs...
                }

                for(int i=0;i<users.size();i++)
                {
                    ActiveUser user = (ActiveUser) users.get(i);
                    if(user.getLocNum()==1)
                       edttxt1.setText(user.getAddress());
                    if(user.getLocNum()==2)
                        edttxt2.setText(user.getAddress());
                    if(user.getLocNum()==3)
                        edttxt3.setText(user.getAddress());
                    if(user.getLocNum()==4)
                        edttxt4.setText(user.getAddress());
                    if(user.getLocNum()==5)
                        edttxt5.setText(user.getAddress());
                    if(user.getLocNum()==61)
                        edttxt6.setText(user.getAddress());
                    if(user.getLocNum()==7)
                        edttxt7.setText(user.getAddress());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




}
