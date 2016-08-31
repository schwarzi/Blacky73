package de.blacky73.lop.container;

import de.blacky73.lop.Main;

/**
 * Created by Schwarz on 31.08.2016.
 */
public class Bank {
    private int id;
    private String userId;
    private int itemId;
    private int anz;
    private String itemName;

    public Bank(String uid){

    }

    public Bank(int id, String userId, int itemId, int anz, String itemName){
        this.id = id;
        this.anz = anz;
        this.itemId = itemId;
        this.itemName = itemName;
        this.userId = userId;
    }

    public boolean create(){
        this.id = Main.datenbank.createBankItem(this);
        return this.id > 0;
    }

    public void sort(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getAnz() {
        return anz;
    }

    public void setAnz(int anz) {
        this.anz = anz;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
