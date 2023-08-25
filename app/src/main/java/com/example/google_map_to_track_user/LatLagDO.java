package com.example.google_map_to_track_user;

public class LatLagDO {

    int Id;
    String UID;
    String RouteCode;
    String UserCode;
    Double Latitude;
    Double Longitude;
    String CurrentTime;
    String Attribute1,Attribute2,Attribute3,Attribute4,Attribute5;

    int Status;

    public LatLagDO(int id, String UID, String routeCode, String userCode, Double latitude, Double longitude, String CurrentTime, String attribute1, String attribute2, String attribute3, String attribute4, String attribute5, int status) {
        this.Id = id;
        this.UID = UID;
        this.RouteCode = routeCode;
        this.UserCode = userCode;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.CurrentTime = CurrentTime;
        this.Attribute1 = attribute1;
        this.Attribute2 = attribute2;
        this.Attribute3 = attribute3;
        this.Attribute4 = attribute4;
        this.Attribute5 = attribute5;
        this.Status = status;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getRouteCode() {
        return RouteCode;
    }

    public void setRouteCode(String routeCode) {
        RouteCode = routeCode;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getDATETIME() {
        return CurrentTime;
    }

    public void setDATETIME(String DATETIME) {
        this.CurrentTime = DATETIME;
    }

    public String getAttribute1() {
        return Attribute1;
    }

    public void setAttribute1(String attribute1) {
        Attribute1 = attribute1;
    }

    public String getAttribute2() {
        return Attribute2;
    }

    public void setAttribute2(String attribute2) {
        Attribute2 = attribute2;
    }

    public String getAttribute3() {
        return Attribute3;
    }

    public void setAttribute3(String attribute3) {
        Attribute3 = attribute3;
    }

    public String getAttribute4() {
        return Attribute4;
    }

    public void setAttribute4(String attribute4) {
        Attribute4 = attribute4;
    }

    public String getAttribute5() {
        return Attribute5;
    }

    public void setAttribute5(String attribute5) {
        Attribute5 = attribute5;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
