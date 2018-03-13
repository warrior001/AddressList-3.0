package com.example.nir.addresslist30;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nir on 9/18/2017.
 */

public class Order extends AppCompatActivity {
    private SeekBar discounts;
    private TextView discountTxt;
    private Spinner spinner;
    private Button add;
    private Button discard;
    private TextView order;
    private TextView bill;
    private TextView spinnerItemSelectedText;
    private TextView newPrice;
    private Button back;
    private Button save;
    private Button finish;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("ItemList");
    private DatabaseReference myActiveRef = database.getReference("Active");
    private List orderlst = new ArrayList<>();
    private List<Items> itemlist = new ArrayList<>();
    private ActiveUser user;
    private boolean isSaved=false;
    private boolean wasSent = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);
        getSupportActionBar().hide();

        discounts =(SeekBar) findViewById(R.id.seekBar);
        discountTxt = (TextView) findViewById(R.id.textView);
        spinner = (Spinner) findViewById(R.id.spinner);
        add = (Button) findViewById(R.id.button4);
        discard = (Button) findViewById(R.id.button6);
        order = (TextView) findViewById(R.id.editText12) ;
        bill = (TextView) findViewById(R.id.textView3) ;
        newPrice = (TextView) findViewById(R.id.textView15) ;
        back = (Button) findViewById(R.id.button5);
        save = (Button) findViewById(R.id.button7);
        finish = (Button) findViewById(R.id.button8);
        spinnerItemSelectedText = (TextView) findViewById(R.id.textView13) ;
        order.setEnabled(false);
        discountTxt.setText("0%");

        discounts.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                discountTxt.setText(discounts.getProgress() +"%");
                setNewPrice();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setItemsInItemList();
        spinnerItemSet();
        getDBOrder();




     spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 Items temp = (Items) spinner.getSelectedItem();
                 Items spinnerItem= new Items(temp.getName(),temp.getPrice(),temp.getQuantity());
                 spinnerItemSelectedText.setText(spinnerItem.getName());

         }

         @Override
         public void onNothingSelected(AdapterView<?> parent) {
            spinnerItemSelectedText.setText("");
         }
     });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClick();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClick();
            }
        });
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDiscardButtonClick();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick();
                isSaved=true;

            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishClick();
            }
        });

    }

    public void spinnerItemSet()
    {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("ItemList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot itemDS: dataSnapshot.getChildren())
                {
                    Items item = itemDS.getValue(Items.class);
                    if(!itemlist.contains(item))
                    itemlist.add(item);

                }

                ArrayAdapter<Items> adapter=new ArrayAdapter<Items>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,itemlist);
                adapter.notifyDataSetChanged();
                spinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    public void setItemsInItemList()
    {
         DatabaseReference itemListRef = database.getReference("ItemList");
         Items pizza=new Items("Pizza",10.00,1);
         Items cola=new Items("cola",1.50,1);
         Items chips=new Items("chips",2.49,1);
         Items[] itemList= new Items[]{pizza,cola,chips};

        for(int i=0;i<itemList.length;i++)
        {
            itemListRef.child(itemList[i].getName()).setValue(itemList[i]);
        }




    }

    public void onFinishClick()
    {
        String email= getIntent().getStringExtra("email");
        sendEmail(email);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
               returnToMainAndRemoveActiveUser();
            }
        }, 10000);
    }

    public void sendEmail( String email) { //mail to buyer -need to add boss mail
        String[] TO = {email};
        String[] CC = {""};
        String emailDetails = order.getText() +"\n" +"מחיר סופי:" + newPrice.getText();
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");//("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "פרטי הזמנה");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailDetails);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            //Log.i("Finished sending email...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();

        }


    }

    public void returnToMainAndRemoveActiveUser()
    {
        String address= getIntent().getStringExtra("Address2"); //remove user from active orders
        myActiveRef.child(address).removeValue();

        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class); //return to main
        startActivity(mainActivity);
    }

    public void onAddButtonClick()
    {

        Items temp = (Items) spinner.getSelectedItem();
        Items spinnerItem= new Items(temp.getName(),temp.getPrice(),temp.getQuantity());
        if(orderlst!=null && orderlst.contains(spinnerItem))
        {
            int index=orderlst.indexOf(spinnerItem);
            ((Items)orderlst.get(index)).setQuantity(((Items)orderlst.get(index)).getQuantity()+1);
        }
        else
        {
            orderlst.add(spinnerItem);
        }


        String str="";
        double total=0;
        for(int i=0;i<orderlst.size();i++)
        {
            str+=orderlst.get(i);
            total+= ((Items)orderlst.get(i)).getPrice()* ((Items)orderlst.get(i)).getQuantity();
        }
        total=Math.round(total * 100.0) / 100.0;
        String strTotal=String.valueOf(total);
        order.setText(str);
        bill.setText(strTotal);
        setNewPrice();
    }

    public void onBackButtonClick()
    {
        Intent backActivity = new Intent(getApplicationContext(), client_info.class);
        backActivity.putExtra("Address", getIntent().getStringExtra("Address2"));
        backActivity.putExtra("locNum", getIntent().getIntExtra("locNum2",-999));
        backActivity.putExtra("isSaved",isSaved);
        startActivity(backActivity);
    }

    public void onDiscardButtonClick()
    {
        Items temp = (Items) spinner.getSelectedItem();
        Items spinnerItem= new Items(temp.getName(),temp.getPrice(),temp.getQuantity());
        if(orderlst.contains(spinnerItem))
        {
            if(orderlst!=null && orderlst.contains(spinnerItem))
            {

                int index=orderlst.indexOf(spinnerItem);
                if((((Items) orderlst.get(index)).getQuantity()<=1))
                {
                    orderlst.remove(spinnerItem);
                }
                else
                {
                    ((Items)orderlst.get(index)).setQuantity(((Items)orderlst.get(index)).getQuantity()-1);
                }


            }
            else
            {
                orderlst.add(spinnerItem);
            }
        }



        String str="";
        double total=0;
        for(int i=0;i<orderlst.size();i++)
        {
            str+=orderlst.get(i);
            total+= ((Items)orderlst.get(i)).getPrice()* ((Items)orderlst.get(i)).getQuantity();
        }
        total=Math.round(total * 100.0) / 100.0;
        String strTotal=String.valueOf(total);
        order.setText(str);
        bill.setText(strTotal);
        setNewPrice();
    }

    public void setNewPrice()
    {
        if(bill.getText().length()>0)
        {
            double price = Double.parseDouble(bill.getText().toString());
            int discount = discounts.getProgress();
            double newPriceNum= (price*discount)/100;
            price= price-newPriceNum;
            price=Math.round(price * 100.0) / 100.0;
            newPrice.setText(""+price);
        }

    }

    public void onSaveButtonClick()
    {
        removeDuplicatesFromOrderList();
        String address= getIntent().getStringExtra("Address2");
        String name= getIntent().getStringExtra("name");
        String phone= getIntent().getStringExtra("phone");
        String email= getIntent().getStringExtra("email");
        int locNum= getIntent().getIntExtra("locNum2",-999);
        ActiveUser newUser= new ActiveUser(name,email,address,phone,locNum,orderlst);
        System.out.println("USER:"+newUser);
        myActiveRef.child(address).removeValue();
        myActiveRef.child(address).setValue(newUser);
        removeDuplicatesFromOrderList();
        setOrderText();

        DatabaseReference DBListRef = database.getReference("Order list");
        DBListRef.setValue(orderlst);


    }


    public void getDBOrder()
    {
        System.out.println("ORDERLST:"+orderlst);
        final String address= getIntent().getStringExtra("Address2");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Active").child(address).child("itemlst").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userDS : dataSnapshot.getChildren()) {
                        {
                            Items item= userDS.getValue(Items.class);
                           orderlst.add(item);

                        }

                        System.out.println("orderlst:"+orderlst);
                        removeDuplicatesFromOrderList();
                        setOrderText();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }







        public void removeDuplicatesFromOrderList()
        {
            for(int i=0;i<orderlst.size();i++)
            {
                Items tempItem = (Items) orderlst.get(i);
                for(int j=0;j<orderlst.size();j++)
                {
                    if(orderlst.get(j).equals(tempItem))
                        orderlst.remove(j);
                }
                orderlst.add(tempItem);
            }
        }


        public void setOrderText()
        {
            String str="";
            double total=0;
            for(int i=0;i<orderlst.size();i++)
            {
                str+=orderlst.get(i);
                total+= ((Items)orderlst.get(i)).getPrice()* ((Items)orderlst.get(i)).getQuantity();
            }
            total=Math.round(total * 100.0) / 100.0;
            String strTotal=String.valueOf(total);
            order.setText(str);
            bill.setText(strTotal);
            setNewPrice();
        }






}
