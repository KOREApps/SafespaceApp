package kore.ntnu.no.safespace.data;


/**
 * Class description..
 *
 * @author Robert
 */
public class KnownLocation {

    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer radius;

    public KnownLocation() {}

    public KnownLocation(Long id, String name, Double latitude, Double longitude, Integer radius) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getRadius() {
        return radius;
    }
}
