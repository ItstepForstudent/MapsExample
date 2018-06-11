package com.itstep.mapsexample.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionResponse {

    public class Route{
        @SerializedName("overview_polyline")
        @Expose
        public OverviewPolyline overviewPolyline;
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("routes")
    @Expose
    private List<Route> routes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
