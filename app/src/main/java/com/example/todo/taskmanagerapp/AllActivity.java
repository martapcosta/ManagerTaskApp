package com.example.todo.taskmanagerapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.todo.taskmanagerapp.Constants.FIRST_COLUMN;
import static com.example.todo.taskmanagerapp.Constants.SECOND_COLUMN;
import static com.example.todo.taskmanagerapp.Constants.THIRD_COLUMN;
import static com.example.todo.taskmanagerapp.R.id.listView;

public class AllActivity extends AppCompatActivity {

    private final String NAMESPACE = "http://service.taskmanagerapp.example.com";
    private final String URL = "http://10.0.2.2:8080/TaskManagerWS/services/TaskServiceImpl";
    private final String METHOD_NAME = "getAllTasks";
    private String TAG = "PGGURU";

    TextView tv;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    ListView myListView;
    Context txt;
    Task[] array_tasks;
    String[] title;
    String[] description;
    Date[] tododate;
    Boolean[] status;
    private ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myListView = (ListView) findViewById(listView);

        //android.R.layout.simple_list_item_1 is a view available dans SDK android,
        //It contains a TextView with id "@android:id/text1"
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        //ListAdapter adapter = new ListAdapter(AllActivity.this, list);
        myListView.setAdapter(adapter);

        //execute WebService
        WebService_call1 ws = new WebService_call1();
        ws.execute();


       myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                  Intent intent = new Intent(AllActivity.this, DetailsActivity.class);
                                                  intent.putExtra("title", myListView.getItemIdAtPosition(position));
                                                  intent.putExtra("description", myListView.getItemIdAtPosition(position));
                                                  //TODO pass tasks values to the details part
                                                  //intent.putExtra("description", myListView.getItemIdAtPosition(position));
                                                  startActivity(intent);
                                              }
                                          });

        // Locate filter button and attach click listener
        //TODO can't se button filter
        Button btnFilter = (Button) findViewById(R.id.filter_btn);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterPopup(v);
            }
        });
    }

    public Task[] getAllTasks() throws IOException, XmlPullParserException
    {
        //Prépare la requête SOAP
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        //Create the connection and call the WS
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        androidHttpTransport.call(NAMESPACE, envelope);

        //Log the request and reponse
        Log.d("dump Request: ", androidHttpTransport.requestDump);
        Log.d("dump response: ", androidHttpTransport.responseDump);

        List<Task> tasks = new ArrayList<>();

        if(envelope.bodyIn == null) {
            return tasks.toArray(new Task[0]);
        }

        SoapObject arrayTasks = (SoapObject)envelope.bodyIn;
        int sizeofArray = arrayTasks.getPropertyCount();

        for(int i = 0; i < sizeofArray; i++) {
            //For each task, convert it into a Task
            SoapObject taskXml = (SoapObject) arrayTasks.getProperty(i);
            Task t = new Task();
            //t.setId(Integer.valueOf(taskXml.getProperty("id").toString()));
            t.setDescription(taskXml.getProperty("description").toString());
            t.setTitle(taskXml.getProperty("title").toString());
            //TODO missing Dates
            //t.setTodoDate((Date) taskXml.getProperty("todoDate"));
            //t.setUpdateDate((Date) taskXml.getProperty("updateDate"));
            //t.setStatus((Boolean) taskXml.getProperty("status"));
            tasks.add(t);
        }
        return tasks.toArray(new Task[sizeofArray]);
    }

        // Display anchored popup menu based on view selected
        private void showFilterPopup(View v) {
            PopupMenu popup = new PopupMenu(this, v);
            // Inflate the menu from xml
            popup.getMenuInflater().inflate(R.menu.popup_menu_filters, popup.getMenu());
            // Setup menu item selection
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.filter_by_date_recent:
                            Toast.makeText(AllActivity.this, "recent!", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.filter_by_date_old:
                            Toast.makeText(AllActivity.this, "Oldest!", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.filter_by_status_closed:
                            Toast.makeText(AllActivity.this, "Closed tasks!", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.filter_by_status_open:
                            Toast.makeText(AllActivity.this, "Open tasks!", Toast.LENGTH_SHORT).show();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            // Handle dismissal with: popup.setOnDismissListener(...);
            // Show the menu
            popup.show();
        }


    public class WebService_call1 extends AsyncTask<Void, String, Void> {

        private ArrayAdapter<String> adapter;

        public WebService_call1() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                array_tasks = getAllTasks();
                //define the new arrays
                title = new String[array_tasks.length];
                description = new String[array_tasks.length];
                tododate = new Date[array_tasks.length];
                status = new Boolean[array_tasks.length];

                for(int i = 0; i < array_tasks.length; i++) {
                    title[i]= array_tasks[i].getTitle();
                    description[i] = array_tasks[i].getDescription();
                    //tododate[i] = array_tasks[i].getTodoDate();
                    status[i] = array_tasks[i].getStatus();
                }

                for(String item: title){
                    publishProgress(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Toast.makeText(AllActivity.this, "All tasks were added!", Toast.LENGTH_LONG).show();
        }
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            adapter = (ArrayAdapter<String>) myListView.getAdapter();
            //tv.setText("Calculating...");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.i(TAG, "onProgressUpdate");
            adapter.add(values[0]);
        }
    }
}
