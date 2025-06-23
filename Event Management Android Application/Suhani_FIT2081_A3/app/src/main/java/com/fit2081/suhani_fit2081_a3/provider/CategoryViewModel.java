package com.fit2081.suhani_fit2081_a3.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit2081.suhani_fit2081_a3.entities.EventCategory;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private EMARepository repository;
    private LiveData<List<EventCategory>> allCategoryLiveData;

    public CategoryViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new EMARepository(application);

        // get all items by calling method defined in repository class
        allCategoryLiveData = repository.getAllCategory();
    }

    /**
     * ViewModel method to get all cards
     * @return LiveData of type List<Item>
     */
    public LiveData<List<EventCategory>> getAllCategory() {
        return allCategoryLiveData;
    }

    /**
     * ViewModel method to insert one single item,
     * usually calling insert method defined in repository class
     * @param eventCategory object containing details of new Item to be inserted
     */
    public void insert(EventCategory eventCategory) {
        repository.insert(eventCategory);
    }

    public void deleteAllCategories() {
    }

    /*public LiveData<Object> getAllCategory() {
        return null;
    }*/
}
