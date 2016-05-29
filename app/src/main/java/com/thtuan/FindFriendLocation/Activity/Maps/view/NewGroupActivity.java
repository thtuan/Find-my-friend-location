package com.thtuan.FindFriendLocation.Activity.Maps.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenter;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.MapPresenterMgr;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.NewGroupPresenter;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.NewGroupPresenterMgr;
import com.thtuan.FindFriendLocation.R;

import java.util.List;

public class NewGroupActivity extends AppCompatActivity implements NewGroupMgr{

    EditText nameGroup;
    Button newGroup;
    NewGroupPresenterMgr newGroupPresenterMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        nameGroup = (EditText) findViewById(R.id.etGroupName);
        newGroup = (Button) findViewById(R.id.btnNewGroup);
        newGroupPresenterMgr = new NewGroupPresenter(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGroupPresenterMgr.newGroup(nameGroup.getText().toString());
            }
        });
    }

    @Override
    public void showNotifycation(String notice) {
        Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        setResult(RESULT_OK);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
