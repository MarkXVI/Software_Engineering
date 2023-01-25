package Task1;

import java.io.Serializable;

public class LatLng implements Serializable {
    private double latitude;
    private double longitude;

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLat() {
        return latitude;
    }

    public double getLng() {
        return longitude;
    }
}
