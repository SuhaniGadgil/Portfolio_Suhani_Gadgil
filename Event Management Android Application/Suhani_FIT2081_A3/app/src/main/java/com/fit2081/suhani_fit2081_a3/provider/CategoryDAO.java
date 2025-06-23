package com.fit2081.suhani_fit2081_a3.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.fit2081.suhani_fit2081_a3.entities.EventCategory;

import java.util.List;

@Dao
public interface CategoryDAO {
    /*LiveData<List<EventCategoryActivity>> getAllEventCategory();*/

    // // Specifies a database query to retrieve all items from the "items" table. (referenced A.3.4)
    @Query("select * from eventCategory")
    LiveData<List<EventCategory>> getAllCategory(); // Returns a LiveData object containing a list of Item objects.

    // Indicates that this method is used to insert data into the database.
    @Insert
    void saveRecordSave(EventCategory eventCategory); // Method signature for inserting an Item object into the database.

    /*LiveData<List<EventCategory>> getAllCategory();*/

    /*// Method to insert a single category into the database
    @Insert
    void insertCategory(EventCategory category);

    // Method to update a category in the database
    @Update
    void updateCategory(EventCategory category);

    // Method to delete a category from the database
    @Delete
    void deleteCategory(EventCategory category);*/
}
