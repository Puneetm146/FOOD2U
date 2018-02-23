package com.example.dell.food2u;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity{
    Button reg_bn;
    EditText ed_firstname, ed_mobile, ed_email, ed_password, ed_lastname;
    String firstname, mobile, email, password, lastname;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        reg_bn = (Button)findViewById(R.id.register);

        ed_firstname = (EditText)findViewById(R.id.name);
        ed_lastname = (EditText)findViewById(R.id.last);
        ed_mobile = (EditText)findViewById(R.id.mobile);
        ed_email = (EditText)findViewById(R.id.email);
        ed_password = (EditText)findViewById(R.id.password);

        builder = new AlertDialog.Builder(Register.this);

        reg_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = ed_firstname.getText().toString();
                lastname = ed_lastname.getText().toString();
                mobile = ed_mobile.getText().toString();
                email = ed_email.getText().toString();
                password = ed_password.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(firstname.equals("") || email.equals("") || mobile.equals("") || password.equals(""))
                {
                    builder.setTitle("Something went wrong....");
                    builder.setMessage("Please fill all the fields...");
                    displayAlert("input_error");
                }

                else if((mobile.length() != 10))
                {
                    builder.setTitle("Mobile Number");
                    builder.setMessage("Please enter valid mobile number");
                    displayAlert("input_error");
                }
                else if(!email.matches(emailPattern))
                {
                    builder.setTitle("Email Address");
                    builder.setMessage("Please fill the complete gmail address");
                    displayAlert("input_error");
                }

                else
                {

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.base_url+URL.register,
                                new Response.Listener<String>() {
                                    @Override
                                       public void onResponse(String response) {

                                        startActivity(new Intent(Register.this,Login.class));
                                        Toast.makeText(Register.this, "Successful Registration", Toast.LENGTH_SHORT).show();

//                                        try {
//                                            JSONArray jsonArray = new JSONArray(response);
//                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
//                                            String code = jsonObject.getString("code");
//                                            String message = jsonObject.getString("message");
//                                            builder.setTitle("Server Response....");
//                                            builder.setMessage(message);
//                                          displayAlert(code);
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                       }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("first_name",firstname);
                                params.put("last_name",lastname);
                                params.put("email",email);
                                params.put("password",password);
                                params.put("mobile_no",mobile);

                                return params;
                            }
                        };

                        MySingleton.getInstance(Register.this).addToRequestque(stringRequest);




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
                    ed_password.setText("");
                }
//                else if(code.equals("Registration Success"))
//                {
//                    finish();
//                }
//                else if(code.equals("Registration Failed"))
//                {
//                     ed_name.setText("");
//                     ed_mobile.setText("");
//                     ed_email.setText("");
//                     ed_password.setText("");
//                     ed_compassword.setText("");
//                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
