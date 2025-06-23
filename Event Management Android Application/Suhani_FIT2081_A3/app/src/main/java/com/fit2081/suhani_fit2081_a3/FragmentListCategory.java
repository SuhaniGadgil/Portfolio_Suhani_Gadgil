package com.fit2081.suhani_fit2081_a3;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.suhani_fit2081_a3.entities.EventCategory;
import com.fit2081.suhani_fit2081_a3.provider.CategoryViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
// Adapted from Chief Examiner’s A2 code provided in the A3 Starter Project
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListCategory extends Fragment {

    SharedPreferences sP;
    ArrayList<EventCategory> eventCategories;
    String eventCategoriesString;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CategoryRecyclerAdapter adapter;

    Gson gson = new Gson();

    CategoryViewModel categoryViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListCategory newInstance(String param1, String param2) {
        FragmentListCategory fragment = new FragmentListCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*sP = getActivity().getSharedPreferences(Keys.FILE_NAME, MODE_PRIVATE);
        eventCategoriesString = sP.getString(Keys.CATEGORY_LIST, "[]");*/
        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        eventCategories = gson.fromJson(eventCategoriesString, type);

        //Toast.makeText(getContext(), "Size" + eventCategories.size(), Toast.LENGTH_SHORT).show();

        View v = inflater.inflate(R.layout.fragment_list_category, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new CategoryRecyclerAdapter();
        adapter.setData(eventCategories);


        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Initialize CategoryViewModel
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);


        categoryViewModel.getAllCategory().observe(getActivity(), newData ->{
            adapter.setData((ArrayList<EventCategory>) eventCategories);
            adapter.notifyDataSetChanged();

        });

        //READ ALL


        return v;
    }
}