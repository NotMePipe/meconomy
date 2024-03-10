package not.mepipe.meconomy.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import not.mepipe.meconomy.Main;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e) {
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            CustomBlockData customBlockData = new CustomBlockData(e.getBlock(), Main.getPlugin());
            NamespacedKey key = new NamespacedKey(Main.getPlugin(), "PLACED");
            customBlockData.set(key, PersistentDataType.BYTE, (byte) 1);
        }
    }
}
