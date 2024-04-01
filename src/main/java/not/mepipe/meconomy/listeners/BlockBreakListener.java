package not.mepipe.meconomy.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import not.mepipe.meconomy.Main;
import not.mepipe.meconomy.managers.CurrencyManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class BlockBreakListener implements Listener {

    private final Main plugin = Main.getPlugin();
    private final FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        Block blockBroken = e.getBlock();
        UUID uuid = e.getPlayer().getUniqueId();
        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
        CurrencyManager manager = CurrencyManager.getInstance();
        if (blockBroken.getBlockData() instanceof Ageable) {
            if (config.contains("crop." + blockBroken.getType().name().toLowerCase() + ".stage" + ((Ageable) blockBroken.getBlockData()).getAge())) {
                manager.addCurrencyToPlayer(p, config.getInt("crop." + blockBroken.getType().name().toLowerCase() + ".stage" + ((Ageable) blockBroken.getBlockData()).getAge()));
            }
        } else {
            CustomBlockData customBlockData = new CustomBlockData(blockBroken, plugin);
            NamespacedKey key = new NamespacedKey(Main.getPlugin(), "PLACED");
            if (!customBlockData.has(key)) {
                String name  = blockBroken.getType().name().toLowerCase();
                if (blockBroken.getType().name().contains("DEEPSLATE_")) {
                    name = blockBroken.getType().name().substring(10).toLowerCase();
                } else if (blockBroken.getType().name().contains("NETHER_")) {
                    name = blockBroken.getType().name().substring(7).toLowerCase();
                } else if (blockBroken.getType().name().contains("AMETHYST")) {
                    if (blockBroken.getType().name().equals("AMETHYST_BLOCK")) {
                        name = "amethyst.block";
                    } else if ((blockBroken.getType().name().equals("SMALL_AMETHYST_BUD")) && e.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
                        name = "amethyst.buds.small";
                    } else if ((blockBroken.getType().name().equals("MEDIUM_AMETHYST_BUD")) && e.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
                        name = "amethyst.buds.medium";
                    } else if ((blockBroken.getType().name().equals("LARGE_AMETHYST_BUD")) && e.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
                        name = "amethyst.buds.large";
                    } else if ((blockBroken.getType().name().equals("AMETHYST_CLUSTER")) && e.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
                        name = "amethyst.buds.cluster";
                    } else if (blockBroken.getType().name().equals("AMETHYST_CLUSTER")) {
                        name = "amethyst.buds.shard";
                    }
                }
                if (config.contains("ore." + name)) {
                    manager.addCurrencyToPlayer(p, config.getInt("ore." + name));
                }
            }
        }
    }
}
