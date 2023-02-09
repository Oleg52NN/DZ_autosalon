package com.company;

public class Buyer {
    private String markaAuto;
    private int timeWaiting;
    private int numberClient;

    public int getNumberClient() {
        return numberClient;
    }

    public Buyer(String markaAuto, int timeWaiting, int numberClient) {
        this.markaAuto = markaAuto;
        this.timeWaiting = timeWaiting;
        this.numberClient = numberClient;
    }

    public String getMarkaAuto() {
        return markaAuto;
    }

    public int getTimeWaiting() {
        return timeWaiting;
    }

}
