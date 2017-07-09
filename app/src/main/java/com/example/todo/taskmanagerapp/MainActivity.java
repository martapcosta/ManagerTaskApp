package com.example.todo.taskmanagerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/up button, so long
        // as we specify a parent activity in AndroidManisfest.xml
        int id = item.getItemId();

        if (id== R.id.action_home){
            Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_LONG).show();
        }
        if (id== R.id.action_editnew){
            Intent intent = new Intent(this, CreatEditActivity.class);
            System.out.println("clicou em new");
            startActivity(intent);
        }
        if (id== R.id.action_all){
            Intent intent = new Intent(this, AllActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
