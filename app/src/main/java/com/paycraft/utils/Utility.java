package com.paycraft.utils;

public class Utility {

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double dlon1 = lon2 - lon1;
        double dlat1 = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat1 / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon1 / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);

    }

}
