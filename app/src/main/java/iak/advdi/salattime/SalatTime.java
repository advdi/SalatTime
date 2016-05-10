package iak.advdi.salattime;

/**
 * Created by bakakkoii on 5/10/16.
 */
public class SalatTime {

    private int id;
    private String date_for, fajr, dhuhr, asr, maghrib, isha;

    public SalatTime(int id, String date_for, String fajr, String dhuhr, String asr, String maghrib, String isha) {
        this.id = id;
        this.date_for = date_for;
        this.fajr = fajr;
        this.dhuhr = dhuhr;
        this.asr = asr;
        this.maghrib = maghrib;
        this.isha = isha;
    }

    public int getId() {
        return id;
    }

    public String getDate_for() {
        return date_for;
    }

    public String getFajr() {
        return fajr;
    }

    public String getDhuhr() {
        return dhuhr;
    }

    public String getAsr() {
        return asr;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public String getIsha() {
        return isha;
    }
}
