package com.fit2081.suhani_fit2081_a3.entities;

import android.widget.Switch;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fit2081.suhani_fit2081_a3.Utils;

// Adapted from Chief Examiner’s A2 code provided in the A3 Starter Project
@Entity(tableName = "event")
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private  int id;

    @ColumnInfo(name = "eventId")
    private String eventId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "categoryId")
    private String categoryId;

    @ColumnInfo(name = "ticketsAvailable")
    private int ticketsAvailable;

    @ColumnInfo(name = "isActive")
    private boolean isActive;


    public Event(String name, String categoryId, int ticketsAvailable, boolean isActive) {
        this.eventId = Utils.generateEventId();
        this.name = name;
        this.categoryId = categoryId;
        this.ticketsAvailable = ticketsAvailable;
        this.isActive = isActive;
    }

    public Event(String eventName, String categoryId, int ticketsAvailable, Switch isActive) {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(int ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}