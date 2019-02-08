package com.E2Execel.scanner.Pojo.result_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("srno")
    @Expose
    private String srno;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("town")
    @Expose
    private String town;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("pvmodulesrno")
    @Expose
    private String pvmodulesrno;
    @SerializedName("pvmoduleimage")
    @Expose
    private String pvmoduleimage;
    @SerializedName("controllersrno")
    @Expose
    private String controllersrno;
    @SerializedName("controllerimage")
    @Expose
    private String controllerimage;
    @SerializedName("hpmotorsrno")
    @Expose
    private String hpmotorsrno;
    @SerializedName("hpmotorimage")
    @Expose
    private String hpmotorimage;
    @SerializedName("installationimage")
    @Expose
    private String installationimage;
    @SerializedName("installationstatus")
    @Expose
    private String installationstatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPvmodulesrno() {
        return pvmodulesrno;
    }

    public void setPvmodulesrno(String pvmodulesrno) {
        this.pvmodulesrno = pvmodulesrno;
    }

    public String getPvmoduleimage() {
        return pvmoduleimage;
    }

    public void setPvmoduleimage(String pvmoduleimage) {
        this.pvmoduleimage = pvmoduleimage;
    }

    public String getControllersrno() {
        return controllersrno;
    }

    public void setControllersrno(String controllersrno) {
        this.controllersrno = controllersrno;
    }

    public String getControllerimage() {
        return controllerimage;
    }

    public void setControllerimage(String controllerimage) {
        this.controllerimage = controllerimage;
    }

    public String getHpmotorsrno() {
        return hpmotorsrno;
    }

    public void setHpmotorsrno(String hpmotorsrno) {
        this.hpmotorsrno = hpmotorsrno;
    }

    public String getHpmotorimage() {
        return hpmotorimage;
    }

    public void setHpmotorimage(String hpmotorimage) {
        this.hpmotorimage = hpmotorimage;
    }

    public String getInstallationimage() {
        return installationimage;
    }

    public void setInstallationimage(String installationimage) {
        this.installationimage = installationimage;
    }

    public String getInstallationstatus() {
        return installationstatus;
    }

    public void setInstallationstatus(String installationstatus) {
        this.installationstatus = installationstatus;
    }
}
