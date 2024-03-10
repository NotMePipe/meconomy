package not.mepipe.meconomy.listeners;

import not.mepipe.meconomy.Main;
import net.kyori.adventure.text.format.TextColor;
import not.mepipe.meconomy.utils.ChatComponent;
import not.mepipe.meconomy.utils.IndexedHashmap;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
public class WorldSaveListener implements Listener {

    @EventHandler
    public void onWorldSave(WorldSaveEvent e) {
        IndexedHashmap<String, TextColor> message = new IndexedHashmap<>();
        message.add("Game data saved", TextColor.color(170, 170, 170));

        if(e.getWorld().getName().equals("world_the_end")) {
            Main.getPlugin().saveData();
            Bukkit.broadcast(ChatComponent.build(message));
        }
    }
}
