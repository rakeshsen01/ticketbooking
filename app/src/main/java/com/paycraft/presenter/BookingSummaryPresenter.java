package com.paycraft.presenter;

import com.paycraft.presenter.modelview.ConfirmBooking;
import com.paycraft.presenter.modelview.dao.ConfirmBookingDao;

public class BookingSummaryPresenter extends BasePresenter implements ConfirmBookingDao.Listener {

    private ConfirmBooking confirmBooking;
    private final ConfirmBookingDao bookingDao;

    public BookingSummaryPresenter(ConfirmBooking confirmBooking, ConfirmBookingDao bookingDao) {
        this.confirmBooking = confirmBooking;
        this.bookingDao = bookingDao;
    }

    public void generateBooking(){
        bookingDao.generateTicketID(this);
    }

    public void onDestroy() {
     confirmBooking = null;
    }

    @Override
    public void onFailure() {
        confirmBooking.showSnackBar("Something went wrong");
    }

    @Override
    public void onSuccess() {
        confirmBooking.showSnackBar("Booking Confirm");
    }
}
