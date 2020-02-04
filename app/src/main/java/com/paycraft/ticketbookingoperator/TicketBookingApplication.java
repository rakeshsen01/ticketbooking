package com.paycraft.ticketbookingoperator;

import android.app.Application;

public class TicketBookingApplication extends Application {

    private static TicketBookingApplication instance;

    public static TicketBookingApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

}
