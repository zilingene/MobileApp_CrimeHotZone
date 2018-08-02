package com.example.fortunate.mobile_crimehotzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Activity_Register extends AppCompatActivity {

    //private ProgressDialog progressDialog;
    private final String URL = "http://10.254.7.203/webservice.asmx";



    private EditText FirstName;
    private EditText LastName;
    private EditText ContactNumber;
    private EditText Email;
    private EditText UserName;
    private EditText Password;
    private EditText ConfirmPassword;
    private EditText UserType;
    private EditText Disability;
    private EditText Res;


    private Button button_register;


    //private SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__register);

        FirstName = findViewById(R.id.editText2);
        LastName = findViewById(R.id.editText2);
        ContactNumber = findViewById(R.id.editText2);
        Email = findViewById(R.id.editText2);
        UserName = findViewById(R.id.editText2);
        Password = findViewById(R.id.editText2);
        ConfirmPassword = findViewById(R.id.editText2);
        UserType = findViewById(R.id.editText2);
        Disability = findViewById(R.id.editText2);
        Res = findViewById(R.id.editText2);
        button_register = findViewById(R.id.btnregister);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Activity_Register.this,
        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        //Specify the layout to use when the list of choices appears
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(myAdapter);
        //OnClickRegisterButtonListener();
    }
public void OnClickRegisterButtonListener(){
    button_register = (Button)findViewById(R.id.btnregister);
    button_register.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String UsrName = UserName.getText().toString();
                    String PasWord = Password.getText().toString();
                    String FrstName = FirstName.getText().toString();
                    String LstName = LastName.getText().toString();
                    String CntactNumber = ContactNumber.getText().toString();
                    String Emil = Email.getText().toString();
                    String CnfirmPass = ConfirmPassword.getText().toString();
                    String Usertype = UserType.getText().toString();
                    String Disabilities = Disability.getText().toString();
                    String Residence = Res.getText().toString();

                    if(UsrName.isEmpty() || PasWord.isEmpty() || FrstName.isEmpty() || LstName.isEmpty() || CntactNumber.isEmpty() || UsrName.isEmpty() || Emil.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"The Text Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        SoapAccessTask task = new SoapAccessTask();
                        task.execute(new String[]{UsrName,PasWord,FrstName,LstName,CntactNumber,CnfirmPass});


                        if(CnfirmPass == PasWord)
                        {

                        }
                        else
                        {
                            //Toast.makeText(getApplicationContext(),"Password does not match!", Toast.LENGTH_SHORT).show();
                        }
                    }


                }

            }
    );

}
    //starting asynchronus task ----- ID Fetching
    private class SoapAccessTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";

            try{

                final String NAMESPACE =  "http://tempuri.org/";
                final String URL = "http://10.254.7.203:80/WebService/";
                final String SOAP_ACTION = "http://tempuri.org/Register";
                final String METHOD_NAME = "Register";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("username",urls[0]);
                request.addProperty("password",urls[0]);
                request.addProperty("first_name",urls[0]);
                request.addProperty("surname",urls[0]);
                request.addProperty("contact",urls[0]);
                request.addProperty("email",urls[0]);
                request.addProperty("usertype",urls[0]);
                request.addProperty("disability",urls[0]);
                request.addProperty("res",urls[0]);



                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();

                result = response.toString();





            }
            catch(Exception e)
            {

                result = "ERROR " + e.getLocalizedMessage();

            }

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here

            if(result.contains("ERROR"))
            {
                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
            }
            else
            {
                int resultInt = Integer.parseInt(result);
                if(resultInt == 0)
                {
                    Toast.makeText(getApplicationContext(),"User Name Already Registered.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Success!", Toast.LENGTH_LONG).show();

                }
            }
        }
    }


}