package de.blacky73.lop.listener;

import de.blacky73.lop.Main;
import de.blacky73.lop.container.Fraktion;
import de.blacky73.lop.container.Spieler;
import org.apache.commons.lang.math.Fraction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created by Schwarz on 31.08.2016.
 */
public class L_Player implements Listener {
    private Plugin pl;

    public L_Player(Main pl){
        this.pl = pl;
        pl.getServer().getPluginManager().registerEvents(this,pl);
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(player.hasPlayedBefore()){
            if(Main.allSpieler != null){
                Spieler spieler = Main.allSpieler.get(player.getUniqueId().toString());
                if(spieler != null){
                    Bukkit.getConsoleSender().sendMessage(Main.prefix+"Der Spieler hat sich auf diesem Server zum " +
                            "erstenaml eingeloggt, jedoch gibt es für diesen Spieler, bereits ein Datensatz. Dieser wird nun geprüft.");
                    if(spieler.getFraktion() == null){
                        if(Main.fractions != null){
                            spieler.setFraktion(Main.fractions.get(1));
                            //Spieler updaten
                        }
                    }
                    spieler.setOnline(true);
                } else {
                    //die standartfraktion ist immer die ID=1
                    if(Main.fractions != null){
                        Fraktion f = Main.fractions.get(1);
                        spieler = new Spieler(player.getUniqueId().toString(),player.getName(),0,0,f);
                        if(Main.datenbank.createSpieler(spieler)){
                            spieler.setOnline(true);
                            Main.allSpieler.put(spieler.getId(),spieler);
                            Bukkit.getConsoleSender().sendMessage(Main.prefix+"Der Spieler wurde erfogreich in der Datenbank registriert!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

    }
}
