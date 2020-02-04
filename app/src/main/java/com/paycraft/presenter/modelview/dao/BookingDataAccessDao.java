package com.paycraft.presenter.modelview.dao;

import android.content.Context;
import android.os.AsyncTask;

import com.paycraft.model.database.DatabaseClient;
import com.paycraft.model.beans.Station;
import com.paycraft.service.Service;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class BookingDataAccessDao {


    /**
     *  This interface will communicate between Presenter and Activity.
     */
    public interface Listener {
        /**
         *  Notify  for failure
         */
        void onFailure();

        /**
         * Notify  for success
         * @param response : Server response
         */
        void onSuccess(String response);

        /**
         *  Notify when data get failed from server call
         * @param stations
         */
        void getDatabaseRecord(List<Station> stations);
    }

    /**
     * Get list of Station from API call
     * @param context
     * @param listener
     */
    public void getStationList(final BookingDataAccessDao.Listener listener) {

        Service service = Service.getInstance();
        Call<ResponseBody> result = service.getUserService().getStations(1);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    listener.onSuccess(response.body().string());
                    System.out.println(response.body().string());//convert response to string
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
                t.printStackTrace();
            }
        });
    }

    /**
     *  Get list of station from Database
     * @param context
     * @param listener
     */
    public void getStationFromDb(final Context context,final BookingDataAccessDao.Listener listener) {
        class GetStationList extends AsyncTask<Void, Void, List<Station>> {

            @Override
            protected List<Station> doInBackground(Void... voids) {
                return DatabaseClient
                        .getInstance(context)
                        .getAppDatabase()
                        .stationDao()
                        .getAll();
            }

            @Override
            protected void onPostExecute(List<Station> stations) {
                super.onPostExecute(stations);
                listener.getDatabaseRecord(stations);
            }
        }

        GetStationList gt = new GetStationList();
        gt.execute();
    }
}
