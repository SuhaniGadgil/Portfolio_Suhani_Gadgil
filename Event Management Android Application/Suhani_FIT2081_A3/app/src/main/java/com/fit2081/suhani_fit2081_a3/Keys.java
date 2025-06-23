package com.fit2081.suhani_fit2081_a3;

import com.fit2081.suhani_fit2081_a3.provider.EMADatabase;

// Adapted from Chief Examiner’s A2 code provided in the A3 Starter Project
class Keys{
    public static final String EVENT_ID ="event_id";
    public static final String EVENT_NAME ="event_name";
    public static final String EVENT_CATEGORY_ID="event_category_id";
    public static final String EVENT_IS_ACTIVE ="event_is_active";
    public static final String EVENT_LIST ="event_list";

    public static final String CATEGORY_ID ="category_id";
    public static final String CATEGORY_NAME ="category_name";
    public static final String CATEGORY_EVENT_COUNT="category_event_count";
    public static final String CATEGORY_IS_ACTIVE="category_is_active";
    public static final String CATEGORY_LIST = "category_list";



    public static final String EVENT_TICKETS_AVAILABLE="tickets_available";

    public static final String USER_NAME="user_name";
    public static final String PASSWORD="password";
    public static final String FILE_NAME = "Student_name";


    public static final String EMA_DATABASE = "ema_database";
    private static volatile EMADatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

}