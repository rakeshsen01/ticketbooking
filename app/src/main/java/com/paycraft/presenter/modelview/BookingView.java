package com.paycraft.presenter.modelview;

import com.paycraft.model.beans.Station;

import java.util.List;

public interface BookingView extends BaseClassView {

    /**
     * initialize Station
     * @param stations
     */
    void initializeStations(List<Station> stations);

    /**
     * Calculate Prize
     * @param prize
     */
    void calculatePrize(String prize);
}
