package com.E2Execel.scanner.global;

public  class globalValues {
    public static String APIKEY = "931d2c3421e9499098676ac0122aaca1";
    private static String ID = "";
    private static String pvmodulesrno = "";
    private static String pvmoduleimage = "";
    private static String controllersrno = "";
    private static String controllerimage = "";
    private static String hpmotorsrno = "";
    private static String hpmotorimage = "";
    private static String installationimage = "";

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {
        globalValues.ID = ID;
    }

    private static String installationstatus = "";

    public static String getPvmodulesrno() {
        return pvmodulesrno;
    }

    public static String getPvmoduleimage() {
        return pvmoduleimage;
    }

    public static void setPvmoduleimage(String pvmoduleimage) {
        globalValues.pvmoduleimage = pvmoduleimage;
    }

    public static void setPvmodulesrno(String pvmodulesrno) {
        globalValues.pvmodulesrno = pvmodulesrno;
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
