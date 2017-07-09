package com.example.todo.taskmanagerapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreatEditActivity extends AppCompatActivity {

    private final String NAMESPACE = "http://service.taskmanagerapp.example.com";
    private final String URL = "http://10.0.2.2:8080/TaskManagerWS/services/TaskServiceImpl";
    private final String METHOD_NAME = "addTask";
    private final String SOAP_ACTION = "http://service.taskmanagerapp.example.com/addTask";
    private static String resp;
    private String TAG = "PGGURU";
    EditText mytitle_ed;
    EditText mydescription_ed;
    EditText mytododate_ed;
    EditText myid_ed;
    String title;
    String description;
    String tododate;
    //TextView tv;
    String id;
    Date todoDate;
    int id_int;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button save;
    Button test;
    Task t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addListenerOnButton();

        mytitle_ed = (EditText) findViewById(R.id.task_title);
        mydescription_ed = (EditText) findViewById(R.id.task_description);
        mytododate_ed = (EditText) findViewById(R.id.task_tododate);
        myid_ed = (EditText) findViewById(R.id.task_id);

       // tv = (TextView) findViewById(R.id.tv);
        save = (Button) findViewById(R.id.btn_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mytitle_ed.getText().length() != 0 &&
                        mydescription_ed.getText().length() != 0
                        && mytododate_ed.getText().length() != 0) {
                            //get values
                            title = mytitle_ed.getText().toString();
                            description = mydescription_ed.getText().toString();
                            tododate = mytododate_ed.getText().toString();
                            DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                            try {
                                Date todoDate = df.parse(tododate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            id = myid_ed.getText().toString();
                            int id_int = Integer.parseInt(id);
                    //execute WebService
                    WebService_call2 ws = new WebService_call2();
                    ws.execute();
                }
            }
        });

    }

    public void addListenerOnButton() {

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        test = (Button) findViewById(R.id.btn_radio);

        test.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

                Toast.makeText(CreatEditActivity.this,
                        radioButton.getText(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    public boolean addTask(Task t) throws IOException, XmlPullParserException
    {
        //Prépare la requête SOAP
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        t = new Task();
        t.setTitle(title);
        t.setDescription(description);
        //t.setTodoDate(todoDate);
        //t.setId(33);

        //Property which holds input parameters
        PropertyInfo tPI = new PropertyInfo();
        //set name
        tPI.setName("t");
        // set value
        tPI.setValue(t);

        //Set dataType
        tPI.setType(t.getClass());

        //Add the property to request object
        request.addProperty(tPI);

        //Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "Task",new Task().getClass());
        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;

        try {

            androidHttpTransport.call(NAMESPACE, envelope);
            SoapObject response = (SoapObject)envelope.getResponse();
            t.Id =  Long.parseLong(response.getProperty(0).toString());
            t.Title =  response.getProperty(1).toString();
            t.Description = (String) response.getProperty(2).toString();
            //t.TodoDate = (String) response.getProperty(3).toString();
            //SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            //Assign it to fahren static variable
            resp = response.toString();
            Log.i(TAG, "Merci : "+resp);

        } catch (Exception e) {
                e.printStackTrace();
        }
        return true;
    }

    public class WebService_call2 extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            try {
                addTask(t);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //tv.setText(" added or not? " + resp);
        }
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
           // tv.setText("Calculating...");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.i(TAG, "onProgressUpdate");
        }
    }

}
