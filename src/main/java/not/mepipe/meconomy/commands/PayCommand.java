package not.mepipe.meconomy.commands;

import net.kyori.adventure.text.format.TextColor;
import not.mepipe.meconomy.Main;
import not.mepipe.meconomy.managers.CurrencyManager;
import not.mepipe.meconomy.utils.EconomyCore;
import not.mepipe.meconomy.utils.ChatComponent;
import not.mepipe.meconomy.utils.Helpers;
import not.mepipe.meconomy.utils.IndexedHashmap;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PayCommand implements CommandExecutor {

    private final Main plugin = Main.getPlugin();
    private final FileConfiguration config = plugin.getConfig();

    private final EconomyCore economy = new EconomyCore();

    public PayCommand() {
        Objects.requireNonNull(plugin.getCommand("pay")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("pay")).setTabCompleter(new TabComplete());
    }

    static class TabComplete implements TabCompleter {

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if(args.length == 1) {
                ArrayList<String> playerNames = new ArrayList<>();
                for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                    playerNames.add(player.getName());
                }

                return playerNames;
            } else if(args.length == 2) {
                ArrayList<String> amounts = new ArrayList<>();
                amounts.add("1");
                if (sender instanceof Player) {
                    OfflinePlayer player = (OfflinePlayer) sender;
                    amounts.add(String.valueOf(CurrencyManager.getInstance().getPlayerCurrency(player)));
                }

                return amounts;
            }


            return new ArrayList<>();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        IndexedHashmap<String, TextColor> message = new IndexedHashmap<>();
        if(args.length == 0) {
            message.add("/pay <player> <amount>", TextColor.color(255, 85, 85));
            sender.sendMessage(ChatComponent.build(message));
            return true;
        } else if(args.length == 1) {
            message.add("/pay <player> <amount>", TextColor.color(255, 85, 85));
            sender.sendMessage(ChatComponent.build(message));
            return true;
        } else if(args.length == 2) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);

            int amount = Helpers.parseInt(args[1]);
            UUID uuid;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                uuid = player.getUniqueId();
            } else {
                message.add("You must be a player to run this command!", TextColor.color(255, 85, 85));
                sender.sendMessage(ChatComponent.build(message));
                return false;
            }
            OfflinePlayer self = Bukkit.getOfflinePlayer(uuid);
            if(economy.getBalance(self) - amount >= 0 && amount > 0 && p != self) {
                economy.depositPlayer(p, amount);
                economy.withdrawPlayer(self, amount);
                message.add("You have successfully paid ", TextColor.color(85, 255, 85));
                message.add(p.getName(), TextColor.color(255, 255, 85));
                message.add(" $" + amount, TextColor.color(255, 85, 255));
                sender.sendMessage(ChatComponent.build(message));
                message.clear();
                message.add("You currently have ", TextColor.color(85, 255, 255));
                message.add("$" + (int) economy.getBalance(self), TextColor.color(255, 85, 255));
                sender.sendMessage(ChatComponent.build(message));
                message.clear();
                if(p.isOnline()) {
                    message.add("You have been paid ", TextColor.color(85, 255, 85));
                    message.add("$" + amount, TextColor.color(255, 85, 255));
                    message.add(" by ", TextColor.color(85, 255, 85));
                    message.add(self.getName(), TextColor.color(255, 255, 85));
                    Objects.requireNonNull(p.getPlayer()).sendMessage(ChatComponent.build(message));
                    message.clear();
                    message.add("You currently have ", TextColor.color(85, 255, 255));
                    message.add("$" + (int) economy.getBalance(p), TextColor.color(255, 85, 255));
                    Objects.requireNonNull(p.getPlayer()).sendMessage(ChatComponent.build(message));
                } else {
                    if(config.getString("offlinepaymessage") != null) {
                        message.add(config.getString("offlinepaymessage"), TextColor.color(85, 85, 255));
                        Objects.requireNonNull(self.getPlayer()).sendMessage(ChatComponent.build(message));
                    }
                }
            } else {
                if(p == self) {
                    message.add("You cannot pay yourself", TextColor.color(255, 85, 85));
                    sender.sendMessage(ChatComponent.build(message));
                } else {
                    if (amount == 0) {
                        message.add("You cannot pay someone $0", TextColor.color(255, 85, 85));
                        sender.sendMessage(ChatComponent.build(message));
                    } else if(amount < 0) {
                        message.add("You cannot negatively pay someone", TextColor.color(255, 85, 85));
                        sender.sendMessage(ChatComponent.build(message));
                    } else {
                        message.add("You do not have enough money.", TextColor.color(255, 170, 0));
                        sender.sendMessage(ChatComponent.build(message));
                        message.clear();
                        message.add("You currently have ", TextColor.color(85, 255, 255));
                        message.add("$" + (int) economy.getBalance(self), TextColor.color(255, 85, 255));
                        sender.sendMessage(ChatComponent.build(message));
                    }
                }
            }
        }
        return false;
    }
}
