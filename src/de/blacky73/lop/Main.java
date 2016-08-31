package de.blacky73.lop;

import de.blacky73.lop.atm.ATM;
import de.blacky73.lop.container.Fraktion;
import de.blacky73.lop.container.ShopItem;
import de.blacky73.lop.container.Spieler;
import de.blacky73.lop.db.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schwarz on 31.08.2016.
 */
public class Main extends JavaPlugin {

    public static FileConfiguration config;

    public static String prefix = ChatColor.BLUE+ "["+ChatColor.YELLOW+"LoPSystem"+ChatColor.BLUE+"] "+ChatColor.WHITE;
    public static String noPermissions;

    public static MySQL datenbank;
    public static HashMap<Integer, Fraktion> fractions;
    public static HashMap<String, Spieler> allSpieler;
    public static HashMap<Integer, ShopItem> priceList;

    private String host, port, database, user, password;

    static {
        noPermissions = prefix + " §4Du hast nicht die nötigen Rechte";
    }

    @Override
    public void onEnable(){
        Bukkit.getConsoleSender().sendMessage(this.prefix + "Plugin wurde aktiviert!");
        loadConfig();
        host = config.getString("database.host");
        port = config.getString("database.port");
        database = config.getString("database.dbname");
        user = config.getString("database.user");
        password = config.getString("database.password");

        datenbank = new MySQL(this);
        if(!datenbank.connect(host, port, database,user, password)){
            Bukkit.getConsoleSender().sendMessage(this.prefix + ChatColor.RED+"Muss deaktiviert werden! " +
                    "Passe die config.yml an und sorge für ausreichende Rechte des Datenabnkbenutzers!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        fractions = datenbank.getFraktionen();
        if(fractions != null){
            Bukkit.getConsoleSender().sendMessage(this.prefix + "Es wurden " + fractions.size() + " Fraktionen gefunden..");
            for(Map.Entry<Integer, Fraktion> f : fractions.entrySet()){
                Bukkit.getConsoleSender().sendMessage(this.prefix + "\t'["+ f.getValue().getName()+"]' Permissions: "+f.getValue().getPermissions().size());
            }
        }

        allSpieler = datenbank.getAllSpieler();
        if(allSpieler != null){
            Bukkit.getConsoleSender().sendMessage(this.prefix + "Es wurden "+allSpieler.size()+" registrierte Spielerdaten gefunden.");
        }

        if(config.getBoolean("shopsystem.enable")){
            Bukkit.getConsoleSender().sendMessage(this.prefix+"Das Shopsystem ist aktiviert.");
            if(config.getInt("shopsystem.actualize") > 0){
                Bukkit.getConsoleSender().sendMessage(this.prefix+"Die Preise werden aller "+config.getInt("shopsystem.actualize")+ " Minuten aktualisert!");
                aktualizePrices();
                long interval = config.getInt("shopsystem.actualize") * 60 * 20;
                getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                    @Override
                    public void run() {
                        aktualizePrices();
                    }
                }, interval);
            } else {
                Bukkit.getConsoleSender().sendMessage(this.prefix + "Die Preisliste gilt bis zum neustart des Servers!");
            }
        }

        new ATM(this);
    }

    private void aktualizePrices(){
        priceList = datenbank.getShopItems();
        if(priceList != null){
            Bukkit.getConsoleSender().sendMessage(this.prefix+"Die aktuellen Preise der verfügbaren Items lauten:");
            for(Map.Entry<Integer, ShopItem> si : priceList.entrySet()){
                Bukkit.getConsoleSender().sendMessage(this.prefix+"\t Item: '"+si.getValue().getName()+" - Preis: "+si.getValue().getAktualPrice()+" $");
            }
        }
    }
    @Override
    public void onDisable(){
        datenbank.close();
        Bukkit.getConsoleSender().sendMessage(this.prefix + "Plugin wurde deaktiviert!");
    }

    public void loadConfig(){
        config = getConfig();
        config.options().copyDefaults(true);

        if(new File("plugins/LoPSystem/config.yml").exists()){
            Bukkit.getConsoleSender().sendMessage(this.prefix+"Konfiguration wurde geladen!");
        } else {
            saveDefaultConfig();;
            Bukkit.getConsoleSender().sendMessage(this.prefix+"Konfiguration wurde mit Standartwerten in der config.yml erstellt!");
        }
    }
}
