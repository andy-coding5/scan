package com.E2Execel.scanner.global;

import com.E2Execel.scanner.Pojo.result_details.Pvmodule;

import java.util.List;

public class globalValues {
    public static String APIKEY = "931d2c3421e9499098676ac0122aaca1";
    public static String IP = "https://nmscanner.herokuapp.com";
    private static String ID = "";
    private static List<Pvmodule> pvmodule;
    private static String controllersrno = "";
    private static String controllerimage = "";
    private static String pumpsrno = "";
    private static String pumpimage = "";
    private static String hpmotorsrno = "";
    private static String hpmotorimage = "";
    private static String installationimage = "";
    private static String installationstatus = "";

    public static String getPumpsrno() {
        return pumpsrno;
    }

    public static void setPumpsrno(String pumpsrno) {
        globalValues.pumpsrno = pumpsrno;
    }

    public static String getPumpimage() {
        return pumpimage;
    }

    public static void setPumpimage(String pumpimage) {
        globalValues.pumpimage = pumpimage;
    }

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {
        globalValues.ID = ID;
    }

    public static List<Pvmodule> getPvmodule() {
        return pvmodule;
    }

    public static void setPvmodule(List<Pvmodule> pvmodule) {
        globalValues.pvmodule = pvmodule;
    }

    public static String getControllersrno() {
        return controllersrno;
    }

    public static void setControllersrno(String controllersrno) {
        globalValues.controllersrno = controllersrno;
    }

    public static String getControllerimage() {
        return controllerimage;
    }

    public static void setControllerimage(String controllerimage) {
        globalValues.controllerimage = controllerimage;
    }

    public static String getHpmotorsrno() {
        return hpmotorsrno;
    }

    public static void setHpmotorsrno(String hpmotorsrno) {
        globalValues.hpmotorsrno = hpmotorsrno;
    }

    public static String getHpmotorimage() {
        return hpmotorimage;
    }

    public static void setHpmotorimage(String hpmotorimage) {
        globalValues.hpmotorimage = hpmotorimage;
    }

    public static String getInstallationimage() {
        return installationimage;
    }

    public static void setInstallationimage(String installationimage) {
        globalValues.installationimage = installationimage;
    }

    public static String getInstallationstatus() {
        return installationstatus;
    }

    public static void setInstallationstatus(String installationstatus) {
        globalValues.installationstatus = installationstatus;
    }
}
