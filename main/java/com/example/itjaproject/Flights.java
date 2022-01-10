package com.example.itjaproject;

import static com.example.itjaproject.dbConnection.getConnectionToDb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Flights extends AppCompatActivity {
    private ListView fList;
    public static int FID;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);


        new Flights.FAsync().execute();
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        fList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,long arg3) {

                String test = ((TextView) view).getText().toString();
                String id = test.substring(0,5);
               if(arg2>0) {//uses index to determine

                   try {
                       FID = Integer.parseInt(id.trim());

                       TimeUnit.MILLISECONDS.sleep(100);
                   } catch (InterruptedException ie) {
                       ie.printStackTrace();
                   }
                   Intent intent = new Intent(getApplicationContext(), FlightDetail.class); //replace with a display flight class
                   startActivity(intent);
               }

            }
        });
        }

    class FAsync extends AsyncTask<Void, Void, Void> {


    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Statement statement = getConnectionToDb().createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM flight");//test db

            List<String> itemList = new ArrayList<String>();

            if (rs.next()) {
                String resString = String.format("%4s","FID") + " | " + String.format("%4s","DES")+
                        " |" + String.format("%10s","DEPART")+ " |" + String.format("%10s","ARRIVE")+
                        " |" + String.format("%12s","DURATION")+ " | " + String.format("%2s","CA")+
                        " | PRICE";
                itemList.add(resString);
                do {
                    resString = String.format("%-4s",(rs.getString(1))) + " | " + String.format("%-4s",(rs.getString(2)))+
                            "|" + String.format("%10s",(rs.getString(3)))+ " |" + String.format("%10s",(rs.getString(4)))+
                            " |" + String.format("%12s",(rs.getString(5)))+ " | " + String.format("%2s",(rs.getString(6)))+
                            " |R" + rs.getString(7);
                    itemList.add(resString);
                } while (rs.next());
                ArrayAdapter arrayAdapter = new ArrayAdapter(Flights.this, (R.layout.flist), itemList);

                fList = (ListView) findViewById(R.id.listView1);
                fList.setAdapter(arrayAdapter);

            }
        }  catch (SQLException throwables) {
throwables.printStackTrace();
        } catch (Exception e) {
e.printStackTrace();
        }

        return null;
    }
    }

}


