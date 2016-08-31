package de.blacky73.lop.atm;

import de.blacky73.lop.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

/**
 * Created by Schwarz on 31.08.2016.
 */
public class ATM implements Listener, CommandExecutor {
    Plugin pl;

    public ATM(Main pl){
        this.pl = pl;
        pl.getServer().getPluginManager().registerEvents(this,pl);
        //pl.getCommand("atm").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_AIR){
            this.openGUI(event.getPlayer());
        }
    }
    private void openGUI(Player player){
        Inventory atm_gui = Bukkit.createInventory(null, 36, "§cATM");

        player.openInventory(atm_gui);
    }
}
