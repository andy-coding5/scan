package com.E2Execel.scanner.Pojo.result_details;


import java.util.List;

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
    @SerializedName("aadhar")
    @Expose
    private String aadhar;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("address_line_1")
    @Expose
    private String addressLine1;
    @SerializedName("address_line_2")
    @Expose
    private String addressLine2;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("pumpsrno")
    @Expose
    private String pumpsrno;
    @SerializedName("pumpimage")
    @Expose
    private String pumpimage;
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
    @SerializedName("pvmodule")
    @Expose
    private List<Pvmodule> pvmodule = null;

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

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getPumpsrno() {
        return pumpsrno;
    }

    public void setPumpsrno(String pumpsrno) {
        this.pumpsrno = pumpsrno;
    }

    public String getPumpimage() {
        return pumpimage;
    }

    public void setPumpimage(String pumpimage) {
        this.pumpimage = pumpimage;
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

    public List<Pvmodule> getPvmodule() {
        return pvmodule;
    }

    public void setPvmodule(List<Pvmodule> pvmodule) {
        this.pvmodule = pvmodule;
    }

}
