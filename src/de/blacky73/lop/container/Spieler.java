package de.blacky73.lop.container;

import de.blacky73.lop.Main;

import java.util.HashMap;

/**
 * Created by Schwarz on 31.08.2016.
 */
public class Spieler {
    private String id;
    private String name;
    private int gold;
    private int bankGold;
    private Fraktion fraktion;
    private boolean isOnline;
    private HashMap<Integer, Bank> bank;

    public Spieler(String id){
        this.id = id;
    }

    public Spieler(String id, String name, int gold, int bankGold, Fraktion fraktion){
        this.id = id;
        this.bankGold = bankGold;
        this.fraktion = fraktion;
        this.gold = gold;
        this.name = name;
        bank = Main.datenbank.getUserBank(this.id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getBankGold() {
        return bankGold;
    }

    public void setBankGold(int bankGold) {
        this.bankGold = bankGold;
    }

    public Fraktion getFraktion() {
        return fraktion;
    }

    public void setFraktion(Fraktion fraktion) {
        this.fraktion = fraktion;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public HashMap<Integer, Bank> getBank() {
        return bank;
    }

    public void setBank(HashMap<Integer, Bank> bank) {
        this.bank = bank;
    }
}
