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
import com.parse.ParseObject;
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
    ParseUser parseUser;
    ParseObject parseObject;
    ParseQuery<ParseObject> query;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        nameGroup = (EditText) findViewById(R.id.etGroupName);
        newGroup = (Button) findViewById(R.id.btnNewGroup);
        Activity ac = MapsActivity.mContext;
        spinner = (Spinner) ac.findViewById(R.id.spinner);
        parseUser = ParseUser.getCurrentUser();
        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameGroup.getTextSize()==0){
                    Toast.makeText(getApplicationContext(),"Phải nhập tên nhóm",Toast.LENGTH_SHORT).show();
                }
                else {
                    query = ParseQuery.getQuery("GroupData");
                    query.whereEqualTo("groupName",nameGroup.getText().toString());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if(e==null){
                                if(list.size()!=0){
                                    Toast.makeText(getApplicationContext(),"Tên nhóm đã tồn tại",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    parseObject = new ParseObject("GroupData");
                                    parseObject.put("groupName",nameGroup.getText().toString());
                                    parseObject.put("captainGroup",parseUser);
                                    parseObject.put("userID",parseUser);
                                    parseObject.put("alias",parseUser.getUsername());
                                    parseObject.saveInBackground(new SaveCallback() {
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
        query = ParseQuery.getQuery("GroupData");
        query.whereEqualTo("userID",parseUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){
                    if(list.size()!=0){
                        MapsActivity.arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item);

                        for (ParseObject obj : list){
                           MapsActivity.arrayAdapter.add(obj.getString("groupName"));
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
