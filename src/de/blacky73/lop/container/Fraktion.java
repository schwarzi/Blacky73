package de.blacky73.lop.container;

import de.blacky73.lop.Main;

import java.util.HashMap;

/**
 * Created by Schwarz on 31.08.2016.
 */
public class Fraktion {
    private int id;
    private String name;
    private int sellRabatt;
    private int inheritet;
    private HashMap<String, Boolean> permissions;

    public Fraktion(int id){
        this.id = id;
    }

    public Fraktion(int id, String name, int sellRabatt, int inheritet){
        this.id = id;
        this.name = name;
        this.sellRabatt = sellRabatt;
        this.inheritet = inheritet;
    }

    public boolean create(){
        this.id = Main.datenbank.createFraction(this);
        return this.id > 0;
    }

    public HashMap<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashMap<String, Boolean> permissions) {
        this.permissions = permissions;
    }

    public int getSellRabatt() {
        return sellRabatt;
    }

    public void setSellRabatt(int sellRabatt) {
        this.sellRabatt = sellRabatt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInheritet() {
        return inheritet;
    }
}
