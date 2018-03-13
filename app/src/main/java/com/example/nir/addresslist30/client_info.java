package com.example.nir.addresslist30;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Nir on 8/13/2017.
 */

public class client_info extends AppCompatActivity {
    private int locNum;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myActiveRef = database.getReference("Active");
    private DatabaseReference myTotalRef = database.getReference("Total");
    DatabaseReference myListRef = database.getReference("Order list");
    private EditText name, address, phone, email;
    private Button saveBtn,nextActivityBtn;
    private List users;
    private  boolean isRemoved=false;
    private List<Items> orderlst = new ArrayList<>();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_info);
        getSupportActionBar().hide();

        name = (EditText) findViewById(R.id.editText);
        phone = (EditText) findViewById(R.id.editText2);
        address = (EditText) findViewById(R.id.editText3);
        email = (EditText) findViewById(R.id.editText4);
        saveBtn = (Button) findViewById(R.id.button2);
        nextActivityBtn=(Button) findViewById(R.id.button3);

        System.out.println("Address:" + getIntent().getStringExtra("Address"));
        System.out.println("Address2:" + getIntent().getStringExtra("Address2"));

            if(getIntent().getStringExtra("Address")!=null)
            {
                address.setEnabled(false);

            }




        ReadOrCreateFunc();



    }



    private void createUser(String name, String email, String address, String phone) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        String userId = getIntent().getStringExtra("Address");
        int placeNumber = getIntent().getIntExtra("locNum", -999);
        if (TextUtils.isEmpty(userId)) {
            userId = address;
        }

        ActiveUser activeUser = new ActiveUser(name, email, address, phone, placeNumber, orderlst);
        User user = new User(name, email, address, phone);


        myActiveRef.child(userId).setValue(activeUser);
        myTotalRef.child(userId).setValue(user);

    }

    public void readUser(final String Address) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Total").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List users = new ArrayList<>();
                for (DataSnapshot userDS : dataSnapshot.getChildren()) {
                    User user = userDS.getValue(User.class);
                    users.add(user);


                    //add other inputs...
                }
                for (int i = 0; i < users.size(); i++) {
                    User user = (User) users.get(i);
                    if (user.getAddress().equalsIgnoreCase(Address)) {
                        //System.out.println("Address: "+user.getAddress()+ " Name: "+user.getName());
                        name.setText(user.getName());
                        address.setText(user.getAddress());
                        phone.setText(user.getPhone());
                        email.setText(user.getEmail());

                    }


                }


                System.out.println("Value is:" + users);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    public void updateUser(final String Address) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        locNum= getIntent().getIntExtra("locNum", -999);


        database.child("Active").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

          //NOT PERFECT BUT WORKING... //WHEN CREATING NEW USER AND TRYING TO SWITCH PLACES,
                // IT MIGHT NOT REMOVE FROM LIST AND CREATE 2 OBJECTS WITH SAME LOCNUM.
                final List<Items> orderlst2 = new ArrayList<>();
                for(DataSnapshot userDS: dataSnapshot.getChildren())
                {
                    if(isRemoved==false)
                    {

                        if(userDS.getValue(ActiveUser.class).getLocNum()==locNum)
                        {

                            ActiveUser user = userDS.getValue(ActiveUser.class);
                            System.out.println("User's Itemlst"+user.getItemlst());
                            //database.child("Active").child(user.getAddress()).removeValue();
                            ActiveUser activeUser;
                            /*if(user.getItemlst()!=null)
                            {
                                 activeUser = new ActiveUser(name.getText().toString(), email.getText().toString(), address.getText().toString(), phone.getText().toString(), locNum,user.getItemlst());

                            }
                            else
                            {
                                 activeUser = new ActiveUser(name.getText().toString(), email.getText().toString(), address.getText().toString(), phone.getText().toString(), locNum);

                            }*/

                            activeUser = new ActiveUser(name.getText().toString(), email.getText().toString(), address.getText().toString(), phone.getText().toString(), locNum);

                            myActiveRef.child("test").setValue(new ActiveUser("test","test","test","test",0));
                            Map<String, Object> postValues = activeUser.toMap();
                            database.child("Active").child(Address).updateChildren(postValues);


                            //myActiveRef.child(user.getAddress()).removeValue();
                            //myActiveRef.child(activeUser.getAddress()).setValue(activeUser);
                            //database.child("Active").child(Address).child("ItemList").setValue(orderlst);
                            isRemoved=true;
                            database.child("Order list").removeValue();

                        }
                        else
                        {

                            ActiveUser user = userDS.getValue(ActiveUser.class);
                            System.out.println("User's Itemlst"+user.getItemlst());
                            //database.child("Active").child(user.getAddress()).removeValue();
                            ActiveUser activeUser;

                            /*if(user.getItemlst()!=null)
                            {
                                activeUser = new ActiveUser(name.getText().toString(), email.getText().toString(), address.getText().toString(), phone.getText().toString(), locNum,user.getItemlst());

                            }
                            else
                            {
                                activeUser = new ActiveUser(name.getText().toString(), email.getText().toString(), address.getText().toString(), phone.getText().toString(), locNum);

                            }*/

                            activeUser = new ActiveUser(name.getText().toString(), email.getText().toString(), address.getText().toString(), phone.getText().toString(), locNum);
                            myActiveRef.child("test").setValue(new ActiveUser("test","test","test","test",0));
                            Map<String, Object> postValues = activeUser.toMap();
                            database.child("Active").child(Address).updateChildren(postValues);

                            //database.child("Active").child(Address).child("ItemList").setValue(orderlst);
                            isRemoved=true;
                            database.child("Order list").removeValue();

                        }
                    }


                }







            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        database.child("Total").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = new User(name.getText().toString(),email.getText().toString(),address.getText().toString(),phone.getText().toString());
                Map<String, Object> postValues = user.toMap();
                database.child("Total").child(Address).updateChildren(postValues);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void ReadOrCreateFunc() {
        final String addressIntent = getIntent().getStringExtra("Address");

            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child("Total").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List users = new ArrayList<>();
                    for (DataSnapshot userDS : dataSnapshot.getChildren()) {
                        User user = userDS.getValue(User.class);
                        users.add(user);



                    }
                    for (int i = 0; i < users.size(); i++) {
                        User user = (User) users.get(i);
                        if (user.getAddress().equalsIgnoreCase(addressIntent)) {  //if User is existing
                            name.setText(user.getName());
                            address.setText(user.getAddress());
                            phone.setText(user.getPhone());
                            email.setText(user.getEmail());
                        }


                    }

                    if (address.getText().length() == 0) {
                        address.setText(addressIntent);
                        saveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                    createUser(name.getText().toString(), email.getText().toString(), address.getText().toString(), phone.getText().toString());
                                    Intent back = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(back);


                            }
                        });

                        nextActivityBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                createUser(name.getText().toString(), email.getText().toString(), address.getText().toString(), phone.getText().toString());
                                Intent nextActivity = new Intent(getApplicationContext(), Order.class);
                                nextActivity.putExtra("Address2",getIntent().getStringExtra("Address"));
                                nextActivity.putExtra("locNum2",getIntent().getIntExtra("locNum",-999));
                                nextActivity.putExtra("name",name.getText());
                                nextActivity.putExtra("phone",phone.getText());
                                nextActivity.putExtra("email",email.getText());
                                startActivity(nextActivity);

                            }
                        });

                    }
                    else
                    {
                        saveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println("IsSaved: "+getIntent().getBooleanExtra("isSaved",false));




                                if(getIntent().getBooleanExtra("isSaved",false)==false)
                                {
                                    updateUser(address.getText().toString());
                                    Intent back = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(back);
                                }
                                else
                                {
                                    Intent back = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(back);
                                }

                            }
                        });

                        nextActivityBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateUser(address.getText().toString());
                                Intent nextActivity = new Intent(getApplicationContext(), Order.class);
                                nextActivity.putExtra("Address2",getIntent().getStringExtra("Address"));
                                nextActivity.putExtra("locNum2",getIntent().getIntExtra("locNum",-999));
                                nextActivity.putExtra("name",name.getText().toString());
                                nextActivity.putExtra("phone",phone.getText().toString());
                                nextActivity.putExtra("email",email.getText().toString());
                                startActivity(nextActivity);

                            }
                        });
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        public void setOrderList()
        {

            myListRef.child("Order list").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot userDS: dataSnapshot.getChildren())
                    {
                        Items item = userDS.getValue(Items.class);
                        orderlst.add(item);
                    }
                    System.out.println("ORDERLST-"+orderlst);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }





