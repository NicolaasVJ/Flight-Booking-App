package com.example.itjaproject;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Receipt {
    public static void genReceipt(String path,String username,String  fid, String paid){
        String R = "Flight ID:"+fid+
                " | User:"+username+
                " | Amount Paid:"+paid;
        try {

            File myObj = new File(path+"/Receipt_"+fid+"_"+username+".txt");

            FileWriter writer = new FileWriter(myObj);
            writer.append(R);
            writer.flush();
            writer.close();
            if (myObj.createNewFile()) {
                Log.d("myTag", "File created: " + myObj.getName());
                Log.d("myTag", "Absolute path: " + myObj.getAbsolutePath());
            } else {
                Log.d("myTag", "Exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
