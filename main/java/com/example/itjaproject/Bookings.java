package com.example.itjaproject;

import static com.example.itjaproject.Flights.FID;
import static com.example.itjaproject.dbConnection.getConnectionToDb;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Bookings extends Activity {
    private ListView pList;
    TextView fidbook;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        fidbook = findViewById(R.id.textView13);
        fidbook.setText("Passenger List");

        new Bsync().execute();
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }


            }
    class Bsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Statement statement = getConnectionToDb().createStatement();

                ResultSet rs = statement.executeQuery("SELECT username FROM flightbook WHERE flightID="+FID+" AND status ='booked'");//test db

                List<String> itemList = new ArrayList<String>();

                if (rs.next()) {
                    String resString;
                    do {
                        resString = "Passenger Name:  "+rs.getString(1);
                        itemList.add(resString);
                    } while (rs.next());
                    ArrayAdapter arrayAdapter = new ArrayAdapter(Bookings.this, R.layout.plist, itemList);

                    pList = (ListView) findViewById(R.id.listView1);
                    pList.setAdapter(arrayAdapter);

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();

            } catch (Exception e) {

            e.printStackTrace();
            }
        return null;
        }
    }
        }


