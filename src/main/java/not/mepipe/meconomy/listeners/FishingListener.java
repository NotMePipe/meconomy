package not.mepipe.meconomy.listeners;

import not.mepipe.meconomy.Main;
import not.mepipe.meconomy.managers.CurrencyManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.UUID;

public class FishingListener implements Listener {

    private final Main plugin = Main.getPlugin();
    FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void onPlayerFishes(PlayerFishEvent e) {
        if (e.getCaught() != null) {
            UUID uuid = e.getPlayer().getUniqueId();
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            CurrencyManager manager = CurrencyManager.getInstance(plugin);

            manager.addCurrencyToPlayer(p, config.getInt("fishing"));
        }
    }
}
