package not.mepipe.meconomy.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;

public class ChatComponent {

    public static TextComponent build(IndexedHashmap<String, TextColor> info) {
        ArrayList<TextComponent> components = new ArrayList<>();
        TextComponent.Builder builder = Component.text();

        for (int i = 0; i < info.size(); i++) {
            components.add(Component.text(info.getFirst(i), info.getSecond(i)));
        }

        for (TextComponent component : components) {
            builder.append(component);
        }

        return builder.build();
    }
}
