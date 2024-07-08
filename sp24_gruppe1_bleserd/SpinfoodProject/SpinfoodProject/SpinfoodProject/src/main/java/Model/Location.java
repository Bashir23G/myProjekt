package Model;

import java.io.Serializable;

public class Location implements Serializable {
    private double longitude;
    private double latitude;
    private double kitchenStory;


    public Location(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(double longitude, double latitude, double kitchenStory){
        this.longitude = longitude;
        this.latitude = latitude;
        this.kitchenStory =  kitchenStory;
    }




    public  double distanceTo(Location targetLocation) {
        // Verwendet die Haversine Formel
        int radius = 6371;

        double lat = Math.toRadians(targetLocation.getLatitude() - latitude);
        double lon = Math.toRadians(targetLocation.getLongitude()- longitude);

        double a = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(targetLocation.getLatitude())) * Math.sin(lon / 2) * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c;

        return (double) Math.round(Math.abs(d) * 100) / 100; // Gerundet auf 2 Nachkommastellen
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getKitchenStory() {
        return kitchenStory;
    }

    @Override
    public String toString() {
        if (kitchenStory != 0) {
            return "Location: [Longitude: " + longitude + ", Latitude: " + latitude + ", Kitchen Story: " + kitchenStory + "]";
        } else {
            return "Location: [Longitude: " + longitude + ", Latitude: " + latitude + "]";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.longitude, longitude) == 0 &&
                Double.compare(location.latitude, latitude) == 0;
    }
}
