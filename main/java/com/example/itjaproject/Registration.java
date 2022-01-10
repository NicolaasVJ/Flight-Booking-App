package com.example.itjaproject;

import static com.example.itjaproject.dbConnection.getConnectionToDb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class Registration extends AppCompatActivity {
    EditText et_username, et_password, et_cpassword, et_name,et_surname;
    Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        et_cpassword = (EditText)findViewById(R.id.et_cpassword);
        et_name = (EditText)findViewById(R.id.et_name);
        et_surname = (EditText)findViewById(R.id.et_surname);
        btn_register = (Button)findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String confirm_password = et_cpassword.getText().toString();

                if(username.equals("") || password.equals("") || confirm_password.equals("")){
                    Toast.makeText(getApplicationContext(), "Fields Required", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.equals(confirm_password)){
                        new RegAsync().execute();
                        }
                    else{
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                    }
                }

        });
    }
    class RegAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Statement statement = getConnectionToDb().createStatement();
                String name1= et_username.getText().toString();


                ResultSet resultSet = statement.executeQuery("SELECT * FROM customer WHERE username = '"+name1+"'");

                if(resultSet.next()) {

                }
                else{

                    PreparedStatement ps;
                    String Insert="INSERT INTO `customer` SET `username`=?,`password`=?,`name`=?,`surname`=?,`balance`=?";

                    String A=et_username.getText().toString();
                    String B=et_password.getText().toString();
                    String C=et_name.getText().toString();
                    String D=et_surname.getText().toString();
                    String E= "0.00";

                    try {
                        ps=getConnectionToDb().prepareStatement(Insert);
                        ps.setString(1,A);
                        ps.setString(2,B);
                        ps.setString(3,C);
                        ps.setString(4,D);
                        ps.setString(5,E);
                        ps.executeUpdate();


                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                    Intent intent = new Intent(Registration.this, MainActivity.class);
                    startActivity(intent);
                }
                }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }


}}
