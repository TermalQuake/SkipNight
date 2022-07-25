package me.termalquake.nightskips;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Nightskips extends JavaPlugin{
    public boolean TS;
    int Task;
    long Time;
    long Time2;
    int CTime;
    public static int NeedToSkip;
    public static int cooldown;
    public static Nightskips instance;
    public void onEnable() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if(!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        TS = false;
        instance = this;
        getLogger().info("Плагин работает заебись");
        Bukkit.getPluginManager().registerEvents(new Handler(this), this);
        Task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                Time = Bukkit.getServer().getWorld("world").getTime();
                int PlayersOnline = Bukkit.getOnlinePlayers().size();
                if(getConfig().getString("playermustsleep").equalsIgnoreCase("Половина")) {
                    NeedToSkip = PlayersOnline/2;
                }
                if(getConfig().getString("playermustsleep").equalsIgnoreCase("Четверть")) {
                    NeedToSkip = PlayersOnline/4;
                }
                if(getConfig().getString("playermustsleep").equalsIgnoreCase("Все")) {
                    NeedToSkip = PlayersOnline;
                }
                if(getConfig().getString("playermustsleep").equalsIgnoreCase("custom")) {
                    NeedToSkip = getConfig().getInt("playersneed");
                }
                if(Handler.NowSleep >= NeedToSkip && PlayersOnline > 1) {
                    if(TS == false) {
                        Time2 = Time + CTime;
                        TS = true;
                    }
                    if(Time > Time2) {
                        Bukkit.getServer().getWorld("world").setTime(0);
                        TS = false;
                        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Доброе утро, работяги!");
                    }
                }
            }
        }, 0, 10);
    }

    public static JavaPlugin getInstance() {
        return instance;
    }
}
