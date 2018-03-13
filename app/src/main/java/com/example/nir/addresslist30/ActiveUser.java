package com.example.nir.addresslist30;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nir on 8/16/2017.
 */

public class ActiveUser extends User {
    private int locNum;
    List<Items> itemlst;

    // Default constructor required for calls to
    // DataSnapshot.getValue(ActiveUser.class)
    public ActiveUser() {  }


    public ActiveUser (String name, String email, String address, String phone, int locNum)
    {
        super(name,email,address,phone);
        this.locNum=locNum;
    }

    public ActiveUser (String name, String email, String address, String phone, int locNum,List<Items> itemlst)
    {
        super(name,email,address,phone);
        this.locNum=locNum;
        this.itemlst= new ArrayList<Items>();

        for(int i=0;i<itemlst.size();i++)
        {
            this.itemlst.add(itemlst.get(i));
        }



    }

    public int getLocNum() {
        return locNum;
    }

    public void setLocNum(int locNum) {
        this.locNum = locNum;
    }

    public List<Items> getItemlst() {
        return itemlst;
    }

    public void setItemlst(List<Items> itemlst) {
        this.itemlst = itemlst;
    }

    @Override
    public String toString() {
        return super.toString() +'\''+ "ActiveUser{" +
                "locNum=" + locNum +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
      result.putAll(super.toMap());;
        result.put("locNum", locNum);

        return result;
    }
}
