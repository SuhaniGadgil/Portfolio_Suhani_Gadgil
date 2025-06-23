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


import com.fit2081.suhani_fit2081_a3.entities.Event;
import com.fit2081.suhani_fit2081_a3.provider.EventViewModel;

import java.util.ArrayList;
import java.util.StringTokenizer;
// Adapted from Chief Examiner’s A2 code provided in the A3 Starter Project
public class EventActivity extends AppCompatActivity implements IMessage {

    EditText editTextEventId;
    EditText editTextEventName;
    EditText editTextCategoryId;
    Switch isActive;

    EditText editTextTicketsAvailable;

    SharedPreferences sP;

    private EventViewModel eventViewModel;

    Event event;

    EventRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        editTextEventId = findViewById(R.id.editTextEventId);
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextCategoryId = findViewById(R.id.editTextCategoryId);
        isActive = findViewById(R.id.isActive);
        editTextTicketsAvailable = findViewById(R.id.editTextTicketsAvailable);
        sP = getSharedPreferences(Keys.FILE_NAME, MODE_PRIVATE);

        ActivityCompat.requestPermissions(this, new String[]{"android.permission.SEND_SMS",
                "android.permission.RECEIVE_SMS", "android.permission.READ_SMS"}, 0);
        SMSReceiver.bindListener(this);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        //it keeps saying to change it to eventCategory idk y
        eventViewModel.getAllEvent().observe(this, newData -> {
            adapter.setData(new ArrayList<Event>(newData));
            adapter.notifyDataSetChanged();
        });
    }

    private void updateFields(String _eventName, String _categoryId, String _ticketsAvailable, boolean _isActive) {
        editTextEventName.setText(_eventName);
        editTextCategoryId.setText(_categoryId);
        editTextTicketsAvailable.setText(_ticketsAvailable);
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
            if (command.equals("event")) {
                String eventName = sT.nextToken();
                String categoryId = sT.nextToken();
                String ticketsAvailable = sT.nextToken();
                String isActiveSt = sT.nextToken();
                if (isActiveSt.equals("TRUE") || isActiveSt.equals("FALSE")) {
                    boolean isActive = isActiveSt.equals("TRUE");
                    updateFields(eventName, categoryId, ticketsAvailable, isActive);
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

    public void onSaveClick(View view) {
        saveRecordSave();
    }

    private void saveRecordSave() {

        String eventName = editTextEventName.getText().toString();
        String categoryId = editTextCategoryId.getText().toString();
        String eventIdId = editTextEventId.getText().toString();
        boolean isActiveValue = isActive.isChecked();
        int ticketsAvailable = Integer.parseInt(editTextTicketsAvailable.getText().toString());

        SharedPreferences.Editor editor = sP.edit();
        editor.putString(Keys.EVENT_ID, eventIdId);
        editor.putString(Keys.EVENT_NAME, editTextEventName.getText().toString());
        editor.putString(Keys.EVENT_CATEGORY_ID, categoryId);
        editor.putBoolean(Keys.EVENT_IS_ACTIVE, isActive.isChecked());
        editor.putInt(Keys.EVENT_TICKETS_AVAILABLE, Integer.parseInt(editTextTicketsAvailable.getText().toString()));
        editor.apply();

        editTextEventId.setText(eventIdId);

        // Check if the category exists in the database
        if (!eventViewModel.doesCategoryExist(categoryId)) {
            Toast.makeText(this, "Category does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Event object
        Event newEvent = new Event(eventName, categoryId, ticketsAvailable, isActiveValue);

        // Insert the new event using the EventViewModel
        eventViewModel.insert(newEvent);

        eventViewModel.insert(event);

        Toast.makeText(this, "Event saved: " + eventIdId + " to "+ categoryId, Toast.LENGTH_SHORT).show();

    }

//    private void loadReceipt() {
//        editTextEventId.setText(sP.getString(Keys.ISSUER_NAME, ""));
//        editTextEventName.setText(sP.getString(Keys.BUYER_NAME, ""));
//        editTextCategoryId.setText(sP.getString(Keys.BUYER_ADDRESS, ""));
//        isActive.setChecked(sP.getBoolean(Keys.IS_PAID, false));
//        editTextTicketsAvailable.setText(sP.getInt(Keys.ITEM_QUANTITY, 0) + "");
//    }
}