package com.thtuan.FindFriendLocation.Activity.Maps.view;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.AddFriendPresenter;
import com.thtuan.FindFriendLocation.Activity.Maps.presenter.AddFriendPresenterMgr;
import com.thtuan.FindFriendLocation.R;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity implements AddFriendMgr {

    EditText etAdd;
    ParseUser user;
    boolean isSuccess;
    ArrayAdapter<String> arrayAdapter;
    ListView lvAllUser;
    AddFriendPresenterMgr addFriendPresenterMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        addFriendPresenterMgr = new AddFriendPresenter(this);
        user = ParseUser.getCurrentUser();
        etAdd = (EditText) findViewById(R.id.etAddfriend);
        lvAllUser = (ListView) findViewById(R.id.lvListAllUser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addFriendPresenterMgr.getCanAddFriendList("");
        etAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                addFriendPresenterMgr.getCanAddFriendList(s.toString());
            }
        });
//        addFriendPresenterMgr.getCanAddFriendList();
        lvAllUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addFriendPresenterMgr.getFriend(arrayAdapter.getItem(position),position);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(AddFriendActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddList(ArrayList<String> arrayList) {
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
        lvAllUser.setAdapter(arrayAdapter);
    }

    @Override
    public void isSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public void removeAtPostion(int position) {
        arrayAdapter.remove(arrayAdapter.getItem(position));
    }
}
