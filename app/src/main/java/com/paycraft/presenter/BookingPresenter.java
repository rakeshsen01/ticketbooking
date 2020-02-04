package com.paycraft.presenter;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.paycraft.model.dao.StationDao;
import com.paycraft.model.database.DatabaseClient;
import com.paycraft.model.beans.Station;
import com.paycraft.presenter.modelview.BookingView;
import com.paycraft.presenter.modelview.dao.BookingDataAccessDao;
import com.paycraft.ticketbookingoperator.R;
import com.paycraft.utils.Utility;

import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible to handle all booking facility
 */
public class BookingPresenter extends BasePresenter implements BookingDataAccessDao.Listener {
    private BookingView bookingView;
    private final BookingDataAccessDao bookingDao;
    private Station source;
    private Station destination;

    /**
     * Set source
     * @param source
     */
    public void setSource(Station source) {
        this.source = source;
        calculatePrize();
    }

    /**
     * Set Destination
     * @param destination
     */
    public void setDestination(Station destination) {
        this.destination = destination;
        calculatePrize();

    }

    public BookingPresenter(BookingView userView, BookingDataAccessDao bookingDao) {
        this.bookingView = userView;
        this.bookingDao = bookingDao;
    }

    @Override
    public void onFailure() {
        bookingView.showSnackBar(getString(R.string.error_failure));
    }

    @Override
    public void onSuccess(String response) {
        if (bookingView != null && !TextUtils.isEmpty(response)) {
            Gson gson = new Gson();
            List<Station> postsList = Arrays.asList(gson.fromJson(response,
                    Station[].class));
            Log.i("Station",postsList.toString());
            bookingView.initializeStations(postsList);
        }
        else{
            if(bookingView != null) {
                bookingView.initializeStations(null);
            }
        }
    }


    @Override
    public void getDatabaseRecord(List<Station> stations) {
        if(bookingView != null) {
            bookingView.initializeStations(stations);
        }
    }

    /**
     * Save Station list into database
     * @param stations
     */
    public void saveRecordIntoDataBase(final List<Station> stations){

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    for (Station station : stations) {
                        StationDao dao = DatabaseClient.getInstance((Context) bookingView).getAppDatabase().stationDao();
                        if(dao.getItemById(station.getStationID()) == null){
                            dao.insert(station);
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


    /**
     * Initiate request for Stations
     */
    public void requestStation() {

        bookingDao.getStationList(this);
    }


    /**
     *  This method is responsible to get List of station from database.
     */
    public void getAllStationFromDb() {
        bookingDao.getStationFromDb((Context) bookingView,this);
    }

    /**
     * Calculate Prize based on stations distance
     */
    private void calculatePrize(){
        int prizeFactor = 2;
        if(source != null && destination != null){

            double distance = Utility.distance(source.getLatitude(),source.getLongitude(),destination.getLatitude(),destination.getLongitude());
            double prize = distance*prizeFactor;
            bookingView.calculatePrize(prize+"");
        }

    }
    public void onDestroy() {
        bookingView = null;
    }

    private String getString(int id) {
        if(bookingView != null){
            return ((Context)bookingView).getString(id);
        }
        else
            return "";
    }
}

