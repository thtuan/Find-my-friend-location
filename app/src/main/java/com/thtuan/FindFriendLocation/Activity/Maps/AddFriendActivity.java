package com.thtuan.FindFriendLocation.Activity.Maps;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.thtuan.FindFriendLocation.Activity.Maps.MapsActivity;
import com.thtuan.FindFriendLocation.Class.GetFriendInformation;
import com.thtuan.FindFriendLocation.R;

import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    ParseQuery<ParseObject> queryObject;
    ParseQuery<ParseUser> queryUser;
    GetFriendInformation getFriendInformation;
    EditText etAdd;
    Button btnAdd;
    ParseUser user;
    ArrayAdapter<String> arrayAdapter;
    ListView lvAllUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        user = ParseUser.getCurrentUser();
        etAdd = (EditText) findViewById(R.id.etAddfriend);
        getFriendInformation = new GetFriendInformation();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        etAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (MapsActivity.itemSelected != null) {
                    queryUser = ParseUser.getQuery();
                    queryObject = ParseQuery.getQuery("GroupData");
                    queryObject.whereEqualTo("groupName", MapsActivity.itemSelected);
                    queryUser.whereDoesNotMatchKeyInQuery("username", "alias", queryObject).whereContains("username",s.toString());
                    queryUser.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> list, ParseException e) {
                            arrayAdapter.clear();
                            for (ParseUser obj : list) {
                                arrayAdapter.add(obj.getUsername());
                            }
                            lvAllUser.setAdapter(arrayAdapter);
                        }
                    });
                }
            }
        });
        btnAdd = (Button) findViewById(R.id.btnAddfriend);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriend(etAdd.getText().toString());
                finish();
            }
        });

        lvAllUser = (ListView) findViewById(R.id.lvListAllUser);
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_friend);
        getAddList();
        lvAllUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isSuccess = AddFriend(arrayAdapter.getItem(position));
                if (isSuccess) {
                    arrayAdapter.remove(arrayAdapter.getItem(position));
                }
            }
        });
    }

    private boolean AddFriend(final String name) {
        if (MapsActivity.itemSelected != null) {

            queryUser = ParseUser.getQuery();
            queryUser.whereEqualTo("username", name);
            queryUser.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(final List<ParseUser> list, ParseException e) {
                    if (e == null) {
                        if (list.size() != 0) {
                            queryObject = ParseQuery.getQuery("GroupData");
                            queryObject.whereEqualTo("groupName", MapsActivity.itemSelected).whereEqualTo("userID", list.get(0));
                            queryObject.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list1, ParseException e) {
                                    if (e == null) {

                                        if (list1.size() == 0) {
                                            ParseObject parseObject = new ParseObject("GroupData");
                                            parseObject.put("userID", list.get(0));
                                            parseObject.put("groupName", MapsActivity.itemSelected);
                                            parseObject.put("alias",list.get(0).getUsername());
                                            parseObject.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if(e == null){
                                                        Toast.makeText(getApplicationContext(), "Thêm vào nhóm thành công", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Đã có trong nhóm", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Không tìm thấy user này", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });

        }
        return true;
    }

    private void getAddList() {
        if (MapsActivity.itemSelected != null) {
            ParseQuery<ParseObject> queryObject = ParseQuery.getQuery("GroupData");
            queryObject.whereEqualTo("groupName",MapsActivity.itemSelected);
            queryObject.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    list.size();
                }
            });
            ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
            queryUser.whereDoesNotMatchKeyInQuery("username","alias",queryObject);
            queryUser.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if(e==null){
                        if(list.size()!=0){
                            arrayAdapter.clear();
                            for (ParseUser obj : list) {
                                arrayAdapter.add(obj.getUsername());
                            }
                            lvAllUser.setAdapter(arrayAdapter);
                        }
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(),"Bạn chưa có nhóm",Toast.LENGTH_SHORT).show();
            /*queryUser = ParseUser.getQuery();
            queryUser.whereExists("username");
            queryUser.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    for (ParseUser obj : list) {
                        arrayAdapter.add(obj.getUsername());
                    }
                    lvAllUser.setAdapter(arrayAdapter);
                }
            });*/
        }

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

}
