package not.mepipe.meconomy.listeners;

import not.mepipe.meconomy.Main;
import not.mepipe.meconomy.managers.CurrencyManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class EntityDeathListener implements Listener {

    public Main plugin = Main.getPlugin();
    FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void onPlayerKillsEntity(EntityDeathEvent e) {

        OfflinePlayer p;
        CurrencyManager manager = CurrencyManager.getInstance(plugin);

        if(e.getEntity().getKiller() != null) {
            UUID uuid = e.getEntity().getKiller().getUniqueId();
            p = Bukkit.getOfflinePlayer(uuid);
            if (e.getEntity() instanceof Ageable) {
                if (!((Ageable) e.getEntity()).isAdult()) {
                    if (config.contains("entity.baby_" + e.getEntity().getType().name().toLowerCase())) {
                        manager.addCurrencyToPlayer(p, config.getInt("entity.baby_" + e.getEntity().getType().name().toLowerCase()));
                    } else if (config.contains("entity." + e.getEntity().getType().name().toLowerCase())) {
                        manager.addCurrencyToPlayer(p, config.getInt("entity." + e.getEntity().getType().name().toLowerCase()));
                    }
                } else {
                    if (config.contains("entity." + e.getEntity().getType().name().toLowerCase())) {
                        manager.addCurrencyToPlayer(p, config.getInt("entity." + e.getEntity().getType().name().toLowerCase()));
                    }
                }
            } else {
                if (config.contains("entity." + e.getEntity().getType().name().toLowerCase())) {
                    manager.addCurrencyToPlayer(p, config.getInt("entity." + e.getEntity().getType().name().toLowerCase()));
                }
            }
        }
    }
}
