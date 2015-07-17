
package com.orbitdesign.panoramiopics.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
    "count",
    "has_more",
    "map_location",
    "photos"
})
public class Panoramio {

    @JsonProperty("count")
    private int count;
    @JsonProperty("has_more")
    private boolean hasMore;
    @JsonProperty("map_location")
    private MapLocation mapLocation;
    @JsonProperty("photos")
    private List<Photo> photos = new ArrayList<Photo>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    // Dummy Constructor
    public Panoramio(){}

    /**
     * 
     * @return
     *     The count
     */
    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    @JsonProperty("count")
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 
     * @return
     *     The hasMore
     */
    @JsonProperty("has_more")
    public boolean isHasMore() {
        return hasMore;
    }

    /**
     * 
     * @param hasMore
     *     The has_more
     */
    @JsonProperty("has_more")
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * 
     * @return
     *     The mapLocation
     */
    @JsonProperty("map_location")
    public MapLocation getMapLocation() {
        return mapLocation;
    }

    /**
     * 
     * @param mapLocation
     *     The map_location
     */
    @JsonProperty("map_location")
    public void setMapLocation(MapLocation mapLocation) {
        this.mapLocation = mapLocation;
    }

    /**
     * 
     * @return
     *     The photos
     */
    @JsonProperty("photos")
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * 
     * @param photos
     *     The photos
     */
    @JsonProperty("photos")
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
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
