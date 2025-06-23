package com.fit2081.suhani_fit2081_a3.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit2081.suhani_fit2081_a3.entities.Event;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    // reference to CardRepository
    private EMARepository repository;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Event>> allEventLiveData;

    EventViewModel eventDAO;
    public EventViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new EMARepository(application);

        // get all items by calling method defined in repository class
        allEventLiveData = repository.getAllEvent();
    }

    /**
     * ViewModel method to get all cards
     * @return LiveData of type List<Item>
     */
    public LiveData<List<Event>> getAllEvent() {
        return allEventLiveData;
    }

    /**
     * ViewModel method to insert one single item,
     * usually calling insert method defined in repository class
     * @param event object containing details of new Item to be inserted
     */
    public void insert(Event event) {
        repository.insert(event);
    }

    public boolean doesCategoryExist(String categoryId) {
        return false;
    }

    public void updateCategoryCount(String categoryId) {
    }

    public void deleteAllEvents() {
        // Executes the database operation to delete all events in a background thread.
        EMADatabase.databaseWriteExecutor.execute(() -> eventDAO.deleteAllEvents());
    }
}
