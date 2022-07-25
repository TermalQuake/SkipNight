package me.termalquake.nightskips;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class Handler implements Listener {
    private static Nightskips plugin;
    public static int NowSleep;
    long Time;
    long Time2;
    public static int cooldown;
    private final CooldownManager cooldownManager = new CooldownManager();
    public int PlayersOnline;
    public static float NeedToSkip;
    public Handler(Nightskips plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void PlayerIsSleeping(PlayerBedEnterEvent e) {
        PlayersOnline = Bukkit.getOnlinePlayers().size();
        Time = Bukkit.getServer().getWorld("world").getTime();
        cooldown = plugin.getConfig().getInt("msgcooldown");
        Player p = e.getPlayer();
        int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
        //e.getPlayer().sendMessage(Double.toString(distance));
        if(Time >= 12544 && Time <= 23460) {
            if(e.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
                NowSleep = NowSleep + 1;
                if(plugin.getConfig().getString("playermustsleep").equalsIgnoreCase("Половина")) {
                    NeedToSkip = PlayersOnline/2;
                }
                if(plugin.getConfig().getString("playermustsleep").equalsIgnoreCase("Четверть")) {
                    NeedToSkip = PlayersOnline/4;
                }
                if(plugin.getConfig().getString("playermustsleep").equalsIgnoreCase("Все")) {
                    NeedToSkip = PlayersOnline;
                }
                if(plugin.getConfig().getString("playermustsleep").equalsIgnoreCase("custom")) {
                    NeedToSkip = plugin.getConfig().getInt("playersneed");
                }
                if(timeLeft == 0){
                    cooldownManager.setCooldown(p.getUniqueId(), cooldown);
                    new BukkitRunnable() {
                        public void run() {
                            int timeLeft = cooldownManager.getCooldown(p.getUniqueId());
                            cooldownManager.setCooldown(p.getUniqueId(), --timeLeft);
                            if(timeLeft == 0){
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(this.plugin, 20, 20);
                    if(PlayersOnline != 1) {
                        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + p.getName() + ChatColor.WHITE + " Сейчас спит " + "["+ NowSleep + " из " + (int)NeedToSkip + "] ");
                    }else {
                        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + p.getName() +  ChatColor.WHITE + " Сейчас спит " + "["+ PlayersOnline + " из " + "1" + "] ");
                    }

                }



            }
        }


    }
    public static void getcooldown() {
        cooldown = plugin.getConfig().getInt("msgcooldown");
    }
    @EventHandler
    public void onPlayerBedEnter(PlayerBedLeaveEvent e) {
        NowSleep = NowSleep - 1;
    }

}
