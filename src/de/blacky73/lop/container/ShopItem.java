package de.blacky73.lop.container;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Schwarz on 31.08.2016.
 */
public class ShopItem {
    private int id;
    private int mcid;
    private String name;
    private int priceMin;
    private int priceMax;
    private int aktualPrice;

    public ShopItem(int id, int mcid, String name, int priceMin, int pricemax){
        this.id = id;
        this.mcid = mcid;
        this.name = name;
        this.priceMin = priceMin;
        this.priceMax = pricemax;
        this.aktualPrice = ThreadLocalRandom.current().nextInt(priceMin, pricemax +1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMcid() {
        return mcid;
    }

    public void setMcid(int mcid) {
        this.mcid = mcid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public int getAktualPrice() {
        return aktualPrice;
    }
}
