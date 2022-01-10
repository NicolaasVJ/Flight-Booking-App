package com.example.itjaproject;

import static com.example.itjaproject.dbConnection.getConnectionToDb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button btn_lregister, btn_llogin;
    TextView errorText;
    EditText et_lusername, et_lpassword;
    public static String UN;

    @Override
    public void onResume(){
        super.onResume();
        et_lusername.setText("");
        et_lpassword.setText("");
        errorText.setText("");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        et_lusername = (EditText) findViewById(R.id.lusername);
        et_lpassword = (EditText) findViewById(R.id.lpassword);
        errorText = (TextView) findViewById(R.id.textView4);

        btn_llogin = (Button) findViewById(R.id.btn_llogin);
        btn_lregister = (Button) findViewById(R.id.btn_lregister);

        btn_lregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });
        btn_llogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UN = "Username";
                String uname = et_lusername.getText().toString();
                String pword = et_lpassword.getText().toString();
                UN = uname;
                if (uname.trim().equals("") || pword.trim().equals("")) {//Checks if empty
                    Toast.makeText(getApplicationContext(), "Please complete login details fields", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Check", Toast.LENGTH_SHORT).show();//Testing
                    new Async().execute();
                }
            }
        });

    }
    class Async extends AsyncTask<Void, Void, Void> {
        String records = "",error="";
        @Override
        protected Void doInBackground(Void... voids) {

            try

            {
                Statement statement = getConnectionToDb().createStatement();//getConnection uses the dbConnection class
                 String name1= et_lusername.getText().toString();
                 String pass1= et_lpassword.getText().toString();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM customer WHERE username = '"+name1+"' AND password = '"+pass1+"'");

                if(resultSet.next()) {
                    try {//testing
                        TimeUnit.MILLISECONDS.sleep(60);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                    Intent intent = new Intent(MainActivity.this, Flights.class);
                    startActivity(intent);
                }
                else{
                    //Failed
                    records="Username or password incorrect";
                    et_lusername.setText("");
                    et_lpassword.setText("");

                }

            }

            catch(Exception e)

            {
                error = e.toString();
            }

            return null;
        }
        protected void onPostExecute(Void aVoid) {

            errorText.setText(records);//Testing

            if(error != "")

                errorText.setText(error);

            super.onPostExecute(aVoid);

        }
    }

}

