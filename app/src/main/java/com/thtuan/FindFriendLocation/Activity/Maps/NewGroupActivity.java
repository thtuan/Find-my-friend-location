package com.thtuan.FindFriendLocation.Activity.Maps;

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
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.thtuan.FindFriendLocation.Activity.Maps.MapsActivity;
import com.thtuan.FindFriendLocation.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class NewGroupActivity extends Activity{

    EditText nameGroup;
    Button newGroup;
    ParseUser user;
    ParseQuery<ParseUser> query;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        nameGroup = (EditText) findViewById(R.id.etGroupName);
        newGroup = (Button) findViewById(R.id.btnNewGroup);
        Activity ac = MapsActivity.mContext;
        spinner = (Spinner) ac.findViewById(R.id.spinner);
        user = ParseUser.getCurrentUser();
        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameGroup.getTextSize()==0){
                    Toast.makeText(getApplicationContext(),"Phải nhập tên nhóm",Toast.LENGTH_SHORT).show();
                }
                else {
                    query = ParseUser.getQuery();
                    query.whereEqualTo("groupName",nameGroup.getText().toString());
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> list, ParseException e) {
                            if(e==null){
                                if(list.size()!=0){
                                    Toast.makeText(getApplicationContext(),"Tên nhóm đã tồn tại",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    user.add("groupName",nameGroup.getText().toString());
                                    user.add("captain",user);
                                    user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e==null){
                                                Toast.makeText(getApplicationContext(),"Tạo nhóm thành công",Toast.LENGTH_SHORT).show();
                                                loadSpiner();
                                            }
                                            else{
                                                Log.d("else2:", e.getMessage());
                                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                            else {
                                Log.v("else1:", e.getMessage());
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                finish();
            }
        });
    }
    public void loadSpiner(){
        query = ParseUser.getQuery();
        query.whereEqualTo("username",user.getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if(e==null){
                    if(list.size()!=0){
                        MapsActivity.arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item);
                        JSONArray jsonArray = list.get(0).getJSONArray("groupName");
                        for (int i = 0 ; i < jsonArray.length(); i++){
                            try {
                                MapsActivity.arrayAdapter.add(jsonArray.getString(i));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        spinner.setAdapter(MapsActivity.arrayAdapter);
                    }
                }
                else {

                }
            }
        });
    }

}
