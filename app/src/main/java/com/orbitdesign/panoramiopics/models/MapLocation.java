
package com.orbitdesign.panoramiopics.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
    "lat",
    "lon",
    "panoramio_zoom"
})
public class MapLocation {

    @JsonProperty("lat")
    private double lat;
    @JsonProperty("lon")
    private double lon;
    @JsonProperty("panoramio_zoom")
    private int panoramioZoom;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    // Dummy Constructor
    public MapLocation(){}

    /**
     * 
     * @return
     *     The lat
     */
    @JsonProperty("lat")
    public double getLat() {
        return lat;
    }

    /**
     * 
     * @param lat
     *     The lat
     */
    @JsonProperty("lat")
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * 
     * @return
     *     The lon
     */
    @JsonProperty("lon")
    public double getLon() {
        return lon;
    }

    /**
     * 
     * @param lon
     *     The lon
     */
    @JsonProperty("lon")
    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * 
     * @return
     *     The panoramioZoom
     */
    @JsonProperty("panoramio_zoom")
    public int getpanoramioZoom() {
        return panoramioZoom;
    }

    /**
     * 
     * @param panoramioZoom
     *     The panoramio_zoom
     */
    @JsonProperty("panoramio_zoom")
    public void setpanoramioZoom(int panoramioZoom) {
        this.panoramioZoom = panoramioZoom;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
