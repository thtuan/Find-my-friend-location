package com.thtuan.FindFriendLocation.Activity.Maps.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

public class NewGroupActivity extends Activity implements NewGroupMgr{

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
}
