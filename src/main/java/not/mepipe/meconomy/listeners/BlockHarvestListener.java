package not.mepipe.meconomy.listeners;

import not.mepipe.meconomy.Main;
import not.mepipe.meconomy.managers.CurrencyManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

import java.util.UUID;

public class BlockHarvestListener implements Listener {
    private final Main plugin = Main.getPlugin();
    FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void onPlayerHarvestBlock(PlayerHarvestBlockEvent e) {
        Block blockHarvested = e.getHarvestedBlock();
        UUID uuid = e.getPlayer().getUniqueId();
        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
        CurrencyManager manager = CurrencyManager.getInstance(plugin);

        if (blockHarvested.getBlockData() instanceof Ageable) {
            if (config.contains("crop." + blockHarvested.getType().name().toLowerCase() + ".stage" + ((Ageable) blockHarvested.getBlockData()).getAge())) {
                manager.addCurrencyToPlayer(p, config.getInt("crop." + blockHarvested.getType().name().toLowerCase() + ".stage" + ((Ageable) blockHarvested.getBlockData()).getAge()));
            }
        }
    }
}
