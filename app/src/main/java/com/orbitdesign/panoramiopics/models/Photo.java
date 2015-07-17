
package com.orbitdesign.panoramiopics.models;

import android.os.Parcel;
import android.os.Parcelable;

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
    "height",
    "latitude",
    "longitude",
    "owner_id",
    "owner_name",
    "owner_url",
    "photo_file_url",
    "photo_id",
    "photo_title",
    "photo_url",
    "place_id",
    "upload_date",
    "width"
})
public class Photo implements Parcelable {

    @JsonProperty("height")
    private int height;
    @JsonProperty("latitude")
    private double latitude;
    @JsonProperty("longitude")
    private double longitude;
    @JsonProperty("owner_id")
    private int ownerId;
    @JsonProperty("owner_name")
    private String ownerName;
    @JsonProperty("owner_url")
    private String ownerUrl;
    @JsonProperty("photo_file_url")
    private String photoFileUrl;
    @JsonProperty("photo_id")
    private int photoId;
    @JsonProperty("photo_title")
    private String photoTitle;
    @JsonProperty("photo_url")
    private String photoUrl;
    @JsonProperty("place_id")
    private String placeId;
    @JsonProperty("upload_date")
    private String uploadDate;
    @JsonProperty("width")
    private int width;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    // Dummy Constructor
    public Photo() {}

    @Override
    public boolean equals(Object o) {
        if(o instanceof Photo){
            return ((Photo) o).getPhotoId() == photoId;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return photoId % 400;
    }

    /**
     * 
     * @return
     *     The height
     */
    @JsonProperty("height")
    public int getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The height
     */
    @JsonProperty("height")
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 
     * @return
     *     The latitude
     */
    @JsonProperty("latitude")
    public double getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude
     *     The latitude
     */
    @JsonProperty("latitude")
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return
     *     The longitude
     */
    @JsonProperty("longitude")
    public double getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude
     *     The longitude
     */
    @JsonProperty("longitude")
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * @return
     *     The ownerId
     */
    @JsonProperty("owner_id")
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * 
     * @param ownerId
     *     The owner_id
     */
    @JsonProperty("owner_id")
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * 
     * @return
     *     The ownerName
     */
    @JsonProperty("owner_name")
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * 
     * @param ownerName
     *     The owner_name
     */
    @JsonProperty("owner_name")
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * 
     * @return
     *     The ownerUrl
     */
    @JsonProperty("owner_url")
    public String getOwnerUrl() {
        return ownerUrl;
    }

    /**
     * 
     * @param ownerUrl
     *     The owner_url
     */
    @JsonProperty("owner_url")
    public void setOwnerUrl(String ownerUrl) {
        this.ownerUrl = ownerUrl;
    }

    /**
     * 
     * @return
     *     The photoFileUrl
     */
    @JsonProperty("photo_file_url")
    public String getPhotoFileUrl() {
        return photoFileUrl;
    }

    public String getLargePhotoFileUrl() {
        return "http://static.panoramio.com/photos/large/"+photoId+".jpg";
    }


    /**
     * 
     * @param photoFileUrl
     *     The photo_file_url
     */
    @JsonProperty("photo_file_url")
    public void setPhotoFileUrl(String photoFileUrl) {
        this.photoFileUrl = photoFileUrl;
    }

    /**
     * 
     * @return
     *     The photoId
     */
    @JsonProperty("photo_id")
    public int getPhotoId() {
        return photoId;
    }

    /**
     * 
     * @param photoId
     *     The photo_id
     */
    @JsonProperty("photo_id")
    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    /**
     * 
     * @return
     *     The photoTitle
     */
    @JsonProperty("photo_title")
    public String getPhotoTitle() {
        return photoTitle;
    }

    /**
     * 
     * @param photoTitle
     *     The photo_title
     */
    @JsonProperty("photo_title")
    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }

    /**
     * 
     * @return
     *     The photoUrl
     */
    @JsonProperty("photo_url")
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * 
     * @param photoUrl
     *     The photo_url
     */
    @JsonProperty("photo_url")
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * 
     * @return
     *     The placeId
     */
    @JsonProperty("place_id")
    public String getPlaceId() {
        return placeId;
    }

    /**
     * 
     * @param placeId
     *     The place_id
     */
    @JsonProperty("place_id")
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * 
     * @return
     *     The uploadDate
     */
    @JsonProperty("upload_date")
    public String getUploadDate() {
        return uploadDate;
    }

    /**
     * 
     * @param uploadDate
     *     The upload_date
     */
    @JsonProperty("upload_date")
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * 
     * @return
     *     The width
     */
    @JsonProperty("width")
    public int getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The width
     */
    @JsonProperty("width")
    public void setWidth(int width) {
        this.width = width;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.height);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.ownerId);
        dest.writeString(this.ownerName);
        dest.writeString(this.ownerUrl);
        dest.writeString(this.photoFileUrl);
        dest.writeInt(this.photoId);
        dest.writeString(this.photoTitle);
        dest.writeString(this.photoUrl);
        dest.writeString(this.placeId);
        dest.writeString(this.uploadDate);
        dest.writeInt(this.width);

    }


    private Photo(Parcel in) {
        this.height = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.ownerId = in.readInt();
        this.ownerName = in.readString();
        this.ownerUrl = in.readString();
        this.photoFileUrl = in.readString();
        this.photoId = in.readInt();
        this.photoTitle = in.readString();
        this.photoUrl = in.readString();
        this.placeId = in.readString();
        this.uploadDate = in.readString();
        this.width = in.readInt();

    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
