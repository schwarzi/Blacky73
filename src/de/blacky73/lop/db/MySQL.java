package de.blacky73.lop.db;

import com.mysql.jdbc.Statement;
import de.blacky73.lop.Main;
import de.blacky73.lop.container.Bank;
import de.blacky73.lop.container.Fraktion;
import de.blacky73.lop.container.ShopItem;
import de.blacky73.lop.container.Spieler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schwarz on 31.08.2016.
 */
public class MySQL {
    private Plugin pl;
    public MySQL(Main pl){
        this.pl = pl;
    }
    public Connection con;

    public boolean connect(String host, String port, String database, String user, String password){
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e){
            System.err.println("[LoPSystem] Konnte den MySQL-Treiber nicht finden!");
            return false;
        }

        try{
            con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?user="+user+"&password="+password+"&autoReconnect=true");
            if(!(con.isClosed())){
                Bukkit.getConsoleSender().sendMessage(Main.prefix+"Erfolgreich mit MySQL-Server verbunden!");
                return true;
            }
        } catch (SQLException e){
            System.err.println("[LoPSystem] Konnte nicht mit MySQL verbinden!");
            return false;
        }

        return false;
    }

    public void close(){
        try{
            if( con != null && (!con.isClosed())){
                con.close();
                if(con.isClosed()){
                    Bukkit.getConsoleSender().sendMessage(Main.prefix+"MySql-Verbindung wurde geschlossen!");
                }
            }
        } catch (SQLException e){
            System.err.println("[LoPSystem] Fehler beim schliessen der MySQL-Verbindung");
        }
    }

    //Fraktionsmethoden
    public HashMap<Integer, Fraktion> getFraktionen(){
        String sql = "SELECT * FROM lop_fractions";
        HashMap<Integer, Fraktion> result = new HashMap<Integer, Fraktion>();
        try{
            Statement statement = (Statement) con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                Fraktion f = new Fraktion(rs.getInt("id"), rs.getString("fname"),rs.getInt("sellrabatt"), rs.getInt("inheritid"));
                if(f != null){
                    f.setPermissions(getFraktionPermissions(f.getId()));
                    if(f.getInheritet() > 0){
                        HashMap<String, Boolean> perms = getFraktionPermissions(f.getInheritet());
                        if(perms != null){
                            for(Map.Entry<String, Boolean> p: perms.entrySet()){
                                if(!f.getPermissions().containsKey(p.getKey())){
                                    f.getPermissions().put(p.getKey(),p.getValue());
                                }
                            }
                        }
                    }
                    result.put(f.getId(), f);
                }
            }
        } catch (SQLException e){
            System.err.println("[LoPSystem] Konnte den Befehl"+sql+" nicht ausführen! Error: "+e.getMessage());
            result = null;
        }
        return result;
    }

    public HashMap<String, Boolean> getFraktionPermissions(int id){
        String sql = "SELECT * FROM lop_facrights WHERE fid="+id;
        HashMap<String, Boolean> result = new HashMap<String, Boolean>();
        try{
            Statement statement = (Statement) con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                result.put(rs.getString("rname"),rs.getBoolean("rval"));
            }
        } catch(SQLException e){
            System.err.println("[LoPSystem konnte Befehl "+sql+" nicht ausführen! Error: "+e.getMessage());
            result = null;
        }
        return result;
    }

    public int createFraction(Fraktion f){
        String sql = "INSERT INTO lop_fractions (fname,sellrabatt,inheritid) " +
                "VALUES('"+f.getName()+"',"+f.getSellRabatt()+","+f.getInheritet()+")";
        int result = -1;
        try{
            result = con.createStatement().executeUpdate(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
        }catch(SQLException e){
            System.err.println("[LoPSystem] Konnte die Fraktion "+f.getName()+" nicht anlegen! Error: "+e.getMessage());
            result = -1;
        }
        return result;
    }

    //Bankmethoden
    public int createBankItem(Bank b){
        String sql = "INSERT INTO lop_bank (uid, iid, anz, iname) " +
                "VALUES ('"+b.getUserId()+"',"+b.getItemId()+","+b.getAnz()+",'"+b.getItemName()+"')";
        int result = -1;
        try{
             result = con.createStatement().executeUpdate(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e){
            System.err.println("[LoPSystem] Konnte das Item '"+b.getItemName()+"' nicht in der Bank anlegen! Error: " +e.getMessage());
            result = -1;
        }
        return result;
    }

    public HashMap<Integer, Bank> getUserBank(String uid){
        String sql = "SELECT * FROM lop_bank WHERE uid='"+uid+"'";
        HashMap<Integer, Bank> result = new HashMap<Integer, Bank>();
        try{
            Statement statement = (Statement)con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                Bank b = new Bank(rs.getInt("id"), rs.getString("uid"), rs.getInt("iid"), rs.getInt("anz"), rs.getString("iname"));
                result.put(b.getId(), b);
            }
        } catch(SQLException e){
            System.err.println("[LoPSystem] Fehler beim laden des BankinvertarItems! Error: " + e.getMessage());
            result = null;
        }
        return result;
    }

    //UserMEthoden
    public HashMap<String, Spieler> getAllSpieler(){
        String sql = "SELECT * FROM lop_users";
        HashMap<String, Spieler> result = new HashMap<String, Spieler>();
        try{
            Statement statement = (Statement) con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                Fraktion f = Main.fractions.get(rs.getInt("fraction"));
                Spieler s = new Spieler(rs.getString("uid"), rs.getString("uname"), rs.getInt("mcgold"), rs.getInt("bankgold"), f);
                result.put(s.getId(), s);
            }
        } catch(SQLException e){
            System.err.println("[LoPSystem] Fehler laden der Spielerdaten! Error: "+e.getMessage());
            result = null;
        }
        return result;
    }

    public boolean createSpieler(Spieler s){
        String sql = "INSERT INTO lop_users (uid, uname,fraction.mcgold,bankgold) " +
                "VALUES ('"+s.getId()+"','"+s.getName()+"',"+s.getFraktion().getId()+","+s.getGold()+","+s.getBankGold()+")";
        boolean result = false;
        try{
            result = con.createStatement().executeUpdate(sql) > 0;
        } catch (SQLException e){
            System.err.println("[LoPSystem] Fehler beim anlegen des Spielers '"+s.getName()+"'! Error: " + e.getMessage());
            result = false;
        }
        return result;
    }
    //Shopitem Methoden
    public HashMap<Integer, ShopItem> getShopItems(){
        String sql = "SELECT * FROM lop_shopsystem";
        HashMap<Integer, ShopItem> result = new HashMap<Integer, ShopItem>();
        try{
            Statement statement = (Statement) con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                ShopItem si = new ShopItem(rs.getInt("id"), rs.getInt("mcid"), rs.getString("mcname"), rs.getInt("pricemin"), rs.getInt("pricemax"));
                 if(si != null){
                     result.put(si.getId(), si);
                 }
            }

        } catch(SQLException e){
            System.err.println("[LoPSystem] Fehler beim laden der ShopItems! Error: "+e.getMessage());
            result = null;
        }
        return  result;
    }
}
