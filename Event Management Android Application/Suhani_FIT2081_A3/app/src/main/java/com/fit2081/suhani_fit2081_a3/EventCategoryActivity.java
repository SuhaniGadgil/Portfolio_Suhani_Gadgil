package com.fit2081.suhani_fit2081_a3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.fit2081.suhani_fit2081_a3.entities.EventCategory;
import com.fit2081.suhani_fit2081_a3.provider.CategoryViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

// Adapted from Chief Examiner’s A2 code provided in the A3 Starter Project
public class EventCategoryActivity extends AppCompatActivity implements IMessage {

    EditText editTextCategoryId, editTextCategoryName, editTextEventCount, editTextEventLocation;
    Switch isActive;


    ArrayList<EventCategory> eventCategories;
    String eventCategoriesString;

    SharedPreferences sP;
    Gson gson = new Gson();

    private CategoryViewModel categoryViewModel;
    private CategoryRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        editTextCategoryId = findViewById(R.id.editTextCategoryId);
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        isActive = findViewById(R.id.isActive);
        editTextEventCount = findViewById(R.id.editTextEventCount);
        editTextEventLocation = findViewById(R.id.editTextEventLocation);

        sP = getSharedPreferences(Keys.FILE_NAME, MODE_PRIVATE);

        eventCategoriesString = sP.getString(Keys.CATEGORY_LIST, "[]");
        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        eventCategories = gson.fromJson(eventCategoriesString, type);

//        eventCategories =
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.SEND_SMS",
                "android.permission.RECEIVE_SMS", "android.permission.READ_SMS"}, 0);
        SMSReceiver.bindListener(this);

        // initialise ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        categoryViewModel.getAllCategory().observe(this, newData -> {
            // cast List<Item> to ArrayList<Item>
            adapter.setData(new ArrayList<EventCategory>( newData));
            adapter.notifyDataSetChanged();
        });
    }

    public void onSaveClick(View view) {
        String categoryName = editTextCategoryName.getText().toString();
        String location = editTextEventLocation.getText().toString();
        int eventCount = Integer.parseInt(editTextEventCount.getText().toString());
        boolean isActive = this.isActive.isChecked();

        // Create a new EventCategory object
        EventCategory newCategory = new EventCategory(categoryName, eventCount, isActive, location);

        // Insert the new category into the database via ViewModel
        categoryViewModel.insert(newCategory);

        // Display a success message
        Toast.makeText(this, "Category saved successfully", Toast.LENGTH_SHORT).show();

        // Finish the activity
        finish();
        /*saveRecordSave();*/
    }

    private void updateFields(String _categoryName, String _eventCount, boolean _isActive) {
        editTextCategoryName.setText(_categoryName);
        editTextEventCount.setText(_eventCount);
        isActive.setChecked(_isActive);
    }

    @Override
    public void messageReceived(String message) {
        Log.d("Assignment_1_TAG", "MessageReceived: " + message);

        try {
            int commandIndex = message.indexOf(':');
            String command = message.substring(0, commandIndex);
            String params = message.substring(commandIndex + 1);
            StringTokenizer sT = new StringTokenizer(params, ";");
            if (command.equals("category")) {
                String categoryName = sT.nextToken();
                String eventCount = sT.nextToken();
                String isActiveSt = sT.nextToken();

                if (!Utils.isNumeric(eventCount)) {
                    throw new Exception("Invalid Format");
                }

                if (isActiveSt.equals("TRUE") || isActiveSt.equals("FALSE")) {
                    boolean isActive = isActiveSt.equals("TRUE");
                    updateFields(categoryName, eventCount, isActive);
                } else {
                    throw new Exception("Invalid Format");
                }
            } else {
                Toast.makeText(this, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
        }

    }



}