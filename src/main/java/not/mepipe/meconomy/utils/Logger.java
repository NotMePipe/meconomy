package not.mepipe.meconomy.utils;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

import static not.mepipe.meconomy.utils.Logger.MessageType.*;

public class Logger {

    public enum MessageType {
        GOOD, BAD, INFO
    }

    public static void send(MessageType type, String message) {
        IndexedHashmap<String, TextColor> map = new IndexedHashmap<>();
        String prefix = "[MeConomy] ";
        if(type == GOOD) {
            map.add(prefix + message, TextColor.color(85, 255, 85));
        } else if(type == BAD) {
            map.add(prefix + message, TextColor.color(255, 85, 85));
        } else if(type == INFO) {
            map.add(prefix + message, TextColor.color(255, 255, 85));
        }

        Bukkit.getConsoleSender().sendMessage(ChatComponent.build(map));
    }

    public static void send(Throwable e) {
        IndexedHashmap<String, TextColor> map = new IndexedHashmap<>();
        String prefix = "[MeConomy] ";
        map.add(prefix + e, TextColor.color(255, 85, 85));
        for (StackTraceElement s : e.getStackTrace()) {
            map.add("\n\t" + s, TextColor.color(255, 85, 85));
        }

        Bukkit.getConsoleSender().sendMessage(ChatComponent.build(map));
    }

}
