package com.fit2081.suhani_fit2081_a3.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fit2081.suhani_fit2081_a3.entities.Event;
import com.fit2081.suhani_fit2081_a3.entities.EventCategory;

import java.util.List;

public class EMARepository {
    private CategoryDAO categoryDAO;

    private LiveData<List<EventCategory>> allCategoryLiveData;

    private  EventDAO eventDAO;
    private LiveData<List<Event>> allEventLiveData;

    EMARepository(Application application) {
        // get reference/instance of the database
        EMADatabase db = EMADatabase.getDatabase(application);

        // get reference to DAO, to perform CRUD operations
        categoryDAO = db.categoryDAO();

        // once the class is initialised get all the items in the form of LiveData
        allCategoryLiveData = categoryDAO.getAllCategory();

        eventDAO = db.eventDAO();

        allEventLiveData = eventDAO.getAllEvent();
    }

    /**
     * Repository method to get all cards
     * @return LiveData of type List<Item>
     */
    LiveData<List<EventCategory>> getAllCategory() {
        return allCategoryLiveData;
    }

    /**
     * Repository method to insert one single item
     * @param eventCategory object containing details of new Item to be inserted
     */
    void insert(EventCategory eventCategory) {
        // Executes the database operation to insert the item in a background thread.
        EMADatabase.databaseWriteExecutor.execute(() -> categoryDAO.saveRecordSave(eventCategory));
    }

    /**
     * Repository method to get all cards
     * @return LiveData of type List<Item>
     */
    LiveData<List<Event>> getAllEvent() {
        return allEventLiveData;
    }

    /**
     * Repository method to insert one single item
     * @param event object containing details of new Item to be inserted
     */
    void insert(Event event) {
        // Executes the database operation to insert the item in a background thread.
        EMADatabase.databaseWriteExecutor.execute(() -> eventDAO.saveRecordSave(event));
    }

}
