package com.example.itjaproject;

import static com.example.itjaproject.Flights.FID;
import static com.example.itjaproject.MainActivity.UN;
import static com.example.itjaproject.dbConnection.getConnectionToDb;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class PaymentMode extends Activity {
    TextView flightid;
    TextView fprice;
    TextView userN;
    TextView bal;
    Button buttonPay;
public static float resbal;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentmode);
        flightid = findViewById(R.id.textView19);
        userN = findViewById(R.id.textView20);
        fprice = findViewById(R.id.textView21);
        bal = findViewById(R.id.textView22);
        buttonPay = findViewById(R.id.button4);
        new PAsync().execute();
        try {//testing
            TimeUnit.MILLISECONDS.sleep(100);//Making sure data is loaded
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    makePayment();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void makePayment() throws SQLException {
        if(!buttonPay.getText().equals("Paid")) {
            float ab = Float.parseFloat(((String.valueOf(bal.getText()).trim()).substring(1)));
            float fp = Float.parseFloat((String.valueOf(fprice.getText()).trim().substring(1)));
            resbal = ab - fp;
            if (ab > fp) {

                new updateBal().execute();
                new storePay().execute();

            }
        }

    }

    class updateBal extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            PreparedStatement ps;
            String Update = "UPDATE `customer` SET `balance`=? WHERE `username`=?";

            String A = String.valueOf(resbal);
            String B = UN;

            try {
                ps = getConnectionToDb().prepareStatement(Update);
                ps.setString(1, A);
                ps.setString(2, B);

                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();

            }

            finish();
            startActivity(getIntent());
            return null;
        }
    }

    class storePay extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            PreparedStatement ps;
            String paidPrice = String.valueOf(fprice.getText()).trim().substring(1);
            String Insert = "INSERT INTO `payments` (`username`, `flightID`,  `paid`) VALUES (?, ?, ?)";
            String A = UN;
            String B = flightid.getText().toString();
            String C = String.valueOf(paidPrice);

            try {
                ps = getConnectionToDb().prepareStatement(Insert);
                ps.setString(1, A);
                ps.setString(2, B);
                ps.setString(3, C);
                ps.executeUpdate();
                String path = getApplicationContext().getFilesDir().toString();

                Receipt.genReceipt(path, A, B, C);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class PAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                PreparedStatement load= getConnectionToDb().prepareStatement("SELECT `flightbook`.*, `customer`.`balance`,`flight`.`price`" +
                        "FROM `flightbook`, `customer`,`flight` WHERE `customer`.`username`='"+UN+"' AND flightbook.username ='"+UN+"' AND" +
                        " flightbook.flightID ="+FID+" AND flight.flightID="+FID+";");

                ResultSet rs= load.executeQuery();
                if(rs.next()){
                    flightid.setText(rs.getString(1));
                    userN.setText(rs.getString(2));
                    bal.setText("R"+rs.getString(4));
                    fprice.setText("R"+rs.getString(5));
                }
                else{
                    flightid.setText("NO DATA");
                    userN.setText("NO DATA");
                    bal.setText("NO DATA");
                    fprice.setText("NO DATA");
                }
                PreparedStatement paid= getConnectionToDb().prepareStatement("SELECT `flightbook`.*, `payments`.*" +
                        "FROM `flightbook`, `payments` WHERE `payments`.`username`='"+UN+"' AND flightbook.username ='"+UN+"' AND" +
                        " flightbook.flightID ="+FID+" AND payments.flightID="+FID+";");
                ResultSet did= paid.executeQuery();
                if(did.next()){
                    buttonPay.setText("Paid");
                }

            }catch (SQLException e) {
                e.printStackTrace();
            }


        return null;}
        }

}


