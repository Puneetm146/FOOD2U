package com.example.dell.food2u;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button login;
    TextView ed_mobile, register;
    EditText ed_password;
    String mobile, password;
    Session session;

    AlertDialog.Builder builder;


    Boolean net_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button)findViewById(R.id.Login);
        register = (TextView) findViewById(R.id.register);
        ed_mobile = (TextView) findViewById(R.id.mobile_No);
        ed_password = (EditText)findViewById(R.id.password_one);

        session = new Session(getApplicationContext());

        builder = new AlertDialog.Builder(Login.this);

        net_check = netCheck();

        if(net_check == true){
            displayNetwork();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(Login.this,Register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = ed_mobile.getText().toString();
                password = ed_password.getText().toString();


                if(mobile.length() != 10)
                {
                    builder.setTitle("Mobile Number");
                    builder.setMessage("10-digit Mobile-No not entered");
                    displayAlert("input_error");
                }
                else
                {
                    if(password.length() == 0)
                    {
                        builder.setTitle("Password");
                        builder.setMessage("Password Not entered");
                        displayAlert("input_error1");
                    }
                    else {

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.base_url+URL.login,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObj=new JSONObject(response);

                                            String error = jsonObj.getString("error");
                                            String message = jsonObj.getString("message");
                                            if(error.equals(0)){
                                                builder.setMessage(message);
                                                builder.setTitle("Success!");
                                                displayAlert(error);
                                            }else{
                                                builder.setMessage(message);
                                                builder.setTitle("Alert!");
                                                displayAlert(error);
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {



                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError{
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("mobile_no",mobile);
                                params.put("password",password);

                                return params;
                            }

                        };
                        MySingleton.getInstance(Login.this).addToRequestque(stringRequest);

                    }
                }
            }
        });
    }




    public void displayAlert(final String code)
    {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(code.equals("input_error"))
                {
                    ed_mobile.setText("");
                }
                else if(code.equals("input_error1"))
                {
                    ed_password.setText("");
                }
                else if(code.equals("0"))
                {
                    session.createLoginSession(mobile);
                    startActivity(new Intent(Login.this,MainActivity.class));
                }
                else if(code.equals("1"))
                {
                    ed_mobile.setText("");
                    ed_password.setText("");
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void displayNetwork(){
        new AlertDialog.Builder(this).setMessage("Please check your internet connection")
                .setTitle("Network Error!!").setCancelable(true).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }


    public boolean netCheck(){

        ConnectivityManager connectionManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectionManager.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                && connectionManager.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED){
            net_check = true;
        }
        return net_check;
    }

}