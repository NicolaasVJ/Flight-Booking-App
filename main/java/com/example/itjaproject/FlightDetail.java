package com.example.itjaproject;

import static com.example.itjaproject.Flights.FID;
import static com.example.itjaproject.MainActivity.UN;
import static com.example.itjaproject.dbConnection.getConnectionToDb;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class FlightDetail extends Activity {
   EditText flightid;
    EditText destination;
    EditText departure;
    EditText arrival;
    EditText duration;
    EditText capacity;
    EditText fprice;
    Button bbutton;
    Button buttonView;
    Button buttonPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flightdetail);
        flightid = findViewById(R.id.efid);
        destination = findViewById(R.id.edest);
        departure = findViewById(R.id.edtime);
        arrival = findViewById(R.id.eatime);
        duration = findViewById(R.id.eduration);
        capacity = findViewById(R.id.ecapacity);
        fprice = findViewById(R.id.eprice);
        bbutton=findViewById(R.id.button);
        buttonView=findViewById(R.id.button2);
        buttonPay=findViewById(R.id.button3);
        new FDAsync().execute();
        try {//testing
            TimeUnit.MILLISECONDS.sleep(100);//Making sure data is loaded
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        bbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new BAsync().execute();

            }
        });
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FlightDetail.this, Bookings.class);
                startActivity(intent);
            }
        });
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bbutton.getText().equals("Cancel Booking")) {
                    Intent intent = new Intent(FlightDetail.this, PaymentMode.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "You do not have a reservation", Toast.LENGTH_SHORT).show();//Testing
                }
            }
        });


    }
    class BAsync extends AsyncTask<Void, Void, Void> {
                    String book;
        @Override
        protected Void doInBackground(Void... voids) {
            if(!bbutton.getText().equals("Capacity reached")) {
                if (bbutton.getText().equals("Cancel Booking") || bbutton.getText().equals("Rebook Flight")) {
                    if (bbutton.getText().equals("Cancel Booking")) {
                        book = "cancelled";
                    } else if (bbutton.getText().equals("Rebook Flight")) {
                        book = "booked";
                    }
                    PreparedStatement ps;
                    String Update = "UPDATE `flightbook` SET `status`=? WHERE `username`=? AND `flightID`=?";

                    String A = book;
                    String B = UN;
                    String D = flightid.getText().toString();
                    try {
                        ps = getConnectionToDb().prepareStatement(Update);
                        ps.setString(1, A);
                        ps.setString(2, B);
                        ps.setString(3, D);

                        ps.executeUpdate();

                    } catch (SQLException ex) {
                        ex.printStackTrace();

                    }
                } else {
                    try {
                        String book = "booked";
                        PreparedStatement ps;
                        String Insert = "INSERT INTO `flightbook` SET `username`=?,`flightID`=?,`status`=?";
                        String A = UN;
                        String B = flightid.getText().toString();
                        String C = book;

                        try {
                            ps = getConnectionToDb().prepareStatement(Insert);
                            ps.setString(1, A);
                            ps.setString(2, B);
                            ps.setString(3, C);
                            ps.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }}

                finish();
                startActivity(getIntent());
            return null;
        }
    }

        class FDAsync extends AsyncTask<Void, Void, Void> {
           TextView tvt = findViewById(R.id.eprice);;

                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                Statement statement = getConnectionToDb().createStatement();

                               ResultSet res = statement.executeQuery("SELECT * FROM flight WHERE flightID ="+FID);

                   Statement getCap = getConnectionToDb().createStatement();
                                res.next();
                  ResultSet C = getCap.executeQuery("SELECT COUNT(status)AS av FROM flightbook WHERE flightID ="+FID+" AND status = 'booked'");
                   C.next();

                    int limit= Integer.parseInt((res.getString(6)));//Max
                    int val =(C.getInt("av"));
                    int avCap= limit-val;//Available


                    String fid = (res.getString(1));
                    String dest = (res.getString(2));
                    String dtime = (res.getString(3));
                    String atime = (res.getString(4));
                    String dur = (res.getString(5));
                    String cap = String.valueOf(avCap);
                    String price = (res.getString(7));


    Statement getBook = getConnectionToDb().createStatement();
    ResultSet canBook = getBook.executeQuery("SELECT status FROM flightbook WHERE flightID =" + FID + " AND username = '" + UN + "'");

    if (canBook.next()) {//Check if resultset is empty
        String bookable = (canBook.getString(1));

        if (bookable.equals("booked")) {//.equals very important!!!
            //allow to cancel
            bbutton.setText("Cancel Booking");

        } else if (bookable.equals("cancelled")) {
            //allow to rebook
            if(avCap>0) {
                bbutton.setText("Rebook Flight");
            }
            else {
                bbutton.setText("Capacity reached");
            }
        }
    } else {
        //allow to book
        if(avCap>0) {
        bbutton.setText("Book now");
        }
        else {
            bbutton.setText("Capacity reached");
        }
    }


    if (!res.isClosed()) {
        res.close();
    }
    if (!C.isClosed()) {
        C.close();
    }
    if (!canBook.isClosed()) {
        C.close();
    }


                    flightid.setText((CharSequence)fid);
                    flightid.setFocusable(false);
                    flightid.setClickable(false);

                    destination.setText((CharSequence)dest);
                    destination.setFocusable(false);
                    destination.setClickable(false);

                    departure.setText((CharSequence)dtime);
                    departure.setFocusable(false);
                    departure.setClickable(false);

                    arrival.setText((CharSequence)atime);
                    arrival.setFocusable(false);
                    arrival.setClickable(false);

                    duration.setText((CharSequence)dur);
                    duration.setFocusable(false);
                    duration.setClickable(false);

                    capacity.setText((CharSequence)cap);
                    capacity.setFocusable(false);
                    capacity.setClickable(false);

                    fprice.setText((CharSequence)price);
                    fprice.setFocusable(false);
                    fprice.setClickable(false);


                } catch (SQLException throwables) {
                    tvt.setText(throwables.toString());

                } catch (Exception e) {


                }
            return  null;}
        }
}
