package com.example.fortunate.mobile_crimehotzone;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Activity_Login extends AppCompatActivity {

       private ProgressDialog progressDialog;
    private final String URL = "http://10.254.7.203:80/WebService/";
    private Button btnLogin;
    private Button btnRegister;
    private EditText UserName;
    private EditText Password;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.Activity_Login);

        UserName = (EditText)findViewById(R.id.editText1);
        Password = (EditText)findViewById(R.id.editText2);
        OnClickLogInButtonListener();
        OnClickRegisterButtonListener();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void OnClickLogInButtonListener() {
        btnLogin = (Button) findViewById(R.id.login);
        btnLogin.setOnClickListener(
                new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {

                       SoapAccessTask task = new SoapAccessTask();
                       String UserNameString = UserName.getText().toString();
                        String PasswordString = Password.getText().toString();
                        if (UserNameString.isEmpty() || PasswordString.isEmpty())
                            Toast.makeText(getApplicationContext(), "The Name and Password Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                        else task.execute(new String[]{UserNameString, PasswordString});
                    }
                }
        );
    }

    public void OnClickRegisterButtonListener() {
        btnRegister = findViewById(R.id.sign_up);
        btnRegister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.example.fortunate.Mobile_CrimeHotZone.Activity_Register");
                        startActivity(intent);
                    }
                }
        );
    }

    //starting asynchronus task ----- ID Fetching
    private class SoapAccessTask extends AsyncTask<String, Void, String[]> {

        @Override
        public void onPreExecute() {
            //if you want, start progress dialog here
            progressDialog = ProgressDialog.show(Activity_Login.this,"Please wait.","Connecting..!", true);
        }

        @Override
        protected String[] doInBackground(String... urls) {
            String[] webResponse = null;
            try{
                final String NAMESPACE =  "http://tempuri.org/";
                final String SOAP_ACTION = "http://tempuri.org/Login";
                final String METHOD_NAME = "Login";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("username",urls[0]);
                request.addProperty("password",urls[1]);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject)envelope.getResponse();



                if(Integer.parseInt(response.getProperty(0).toString()) != 0)
                {
                    if(Integer.parseInt(response.getProperty(1).toString()) == 0)
                    {
                        webResponse = new String[3];
                        webResponse[0] = response.getProperty(0).toString();
                        webResponse[1] = response.getProperty(1).toString();
                        webResponse[2] = response.getProperty(6).toString();
                    }
                    else
                    {
                        webResponse = new String[3];
                        webResponse[0] = response.getProperty(0).toString();
                        webResponse[1] = response.getProperty(1).toString();
                        webResponse[2] = response.getProperty(2).toString();

                    }

                }
                else
                {
                    webResponse = new String[2];
                    webResponse[0] = response.getProperty(0).toString();
                    webResponse[1] = response.getProperty(1).toString();
                }

            }
            catch(Exception e){
                webResponse = new String[1];
                webResponse[0] = e.getLocalizedMessage();

            }
            return webResponse;
        }

        @Override
        protected void onPostExecute(String... result) {
            //if you started progress dialog dismiss it here

            if(result.length == 1)
            {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),result[0],Toast.LENGTH_LONG).show();
            }
            else
            {
                try {
                    int resultInt = Integer.parseInt(result[0]);

                    Intent intent = null;
                    if (resultInt == 0)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Incorrect User Name or Password!",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        int UserType = Integer.parseInt(result[1]);;
                        if(UserType == 1)
                        {
                            try {
                                progressDialog.dismiss();
                                intent = new Intent(getApplicationContext(), Class.forName("com.example.fortunate_Mobile_CrimeHotZone.Customer_Dashboard_New"));
                                intent.putExtra("User_ID",result[0]);
                                startActivity(intent);
                                progressDialog.dismiss();
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("name", "Elena");
                                editor.putInt("idName", 12);
                                editor.apply();

                            } catch (ClassNotFoundException e) {
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(UserType == 0)
                        {
                            try {

                                intent = new Intent(getApplicationContext(), Class.forName("com.example.fortunate_Mobile_CrimeHotZone.Collector_Dashboard_Tabbed_Final"));
                                intent.putExtra("User_ID",result[0]);
                                intent.putExtra("comp_ID",result[2]);
                                startActivity(intent);
                                progressDialog.dismiss();

                            } catch (ClassNotFoundException e) {
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Incorrect User Name or Password!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    }
