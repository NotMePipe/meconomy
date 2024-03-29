package not.mepipe.meconomy.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import not.mepipe.meconomy.Main;
import not.mepipe.meconomy.utils.EconomyCore;
import not.mepipe.meconomy.utils.ChatComponent;
import not.mepipe.meconomy.utils.Helpers;
import not.mepipe.meconomy.utils.IndexedHashmap;
import not.mepipe.meconomy.managers.CurrencyManager;
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

public class CurrencyCommand implements CommandExecutor {

    private Main plugin;
    public EconomyCore economy = new EconomyCore();
    public CurrencyManager manager = CurrencyManager.getInstance(plugin);


    public CurrencyCommand(Main plugin) {
        this.plugin = plugin;

        Objects.requireNonNull(plugin.getCommand("currency")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("currency")).setTabCompleter(new TabComplete());
    }

    class TabComplete implements TabCompleter {
        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            ArrayList<String> options = new ArrayList<>();
            if(sender.hasPermission("meconomy.mod")) {
                if (args.length == 1) {
                    options.add("set");
                    options.add("get");
                    options.add("give");
                    options.add("take");
                    options.add("help");
                    options.add("config");
                } else {
                    if(!args[0].equalsIgnoreCase("help")) {
                        if (!args[0].equalsIgnoreCase("config")) {
                            if (args.length == 2) {
                                for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                                    options.add(player.getName());
                                }
                            } else if (args.length == 3) {
                                if(!args[0].equalsIgnoreCase("get")) {
                                    options.add("-100");
                                    options.add("0");
                                    options.add("100");
                                }
                            }
                        } else {
                            if (args.length == 2) {
                                options.add("set");
                                options.add("get");
                                options.add("reload");
                            } else if (args.length == 3) {
                                if (!args[1].equalsIgnoreCase("reload")) {
                                    FileConfiguration config = plugin.getConfig();
                                    for (String key : config.getKeys(true)) {
                                        if (key.contains(".") || key.equals("offlinepaymessage")) {
                                            options.add(key);
                                        }
                                    }
                                }
                            } else if(args.length == 4) {
                                if (args[1].equalsIgnoreCase("set") && !args[2].equals("offlinepaymessage")) {
                                    FileConfiguration config = plugin.getConfig();
                                    int max = 0;
                                    for (String key : config.getKeys(true)) {
                                        if (key.contains(".")) {
                                            max = Math.max(max, config.getInt(key));
                                        }
                                    }
                                    options.add("0");
                                    options.add(String.valueOf(max));
                                }
                            }
                        }
                    }
                }
            }

            return options;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        IndexedHashmap<String, TextColor> message = new IndexedHashmap<>();
        if(sender.hasPermission("meconomy.mod")) {
            if(args.length == 0) {
                if(sender instanceof Player) {
                    OfflinePlayer p = (OfflinePlayer) sender;
                    message.add("You have ", TextColor.color(85, 255, 255));
                    message.add(" $" + (int) economy.getBalance(p), TextColor.color(255, 85, 255));
                    sender.sendMessage(ChatComponent.build(message));
                } else {
                    message.add("You must be a player to run this command!", TextColor.color(255, 85, 85));
                    sender.sendMessage(ChatComponent.build(message));
                    return false;
                }
            } else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("help")) {
                    message.add("/currency set <player> <amount>\n", TextColor.color(85, 255, 255));
                    message.add("/currency get <player>\n", TextColor.color(85, 255, 255));
                    message.add("/currency give <player> <amount>\n", TextColor.color(85, 255, 255));
                    message.add("/currency take <player> <amount>\n", TextColor.color(85, 255, 255));
                    message.add("/currency help\n", TextColor.color(85, 255, 255));
                    message.add("/currency config <set:get:reload>", TextColor.color(85, 255, 255));
                    sender.sendMessage(ChatComponent.build(message));
                    return true;
                } else {
                    message.add("/currency set <player> <amount>\n", TextColor.color(255, 85, 85));
                    message.add("/currency get <player>\n", TextColor.color(255, 85, 85));
                    message.add("/currency give <player> <amount>\n", TextColor.color(255, 85, 85));
                    message.add("/currency take <player> <amount>\n", TextColor.color(255, 85, 85));
                    message.add("/currency help\n", TextColor.color(255, 85, 85));
                    message.add("/currency config <set:get:reload>", TextColor.color(255, 85, 85));
                    sender.sendMessage(ChatComponent.build(message));
                }
            } else if(args.length == 2) {
                OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);

                if (args[0].equalsIgnoreCase("get")) {
                    message.add("Player ", TextColor.color(85, 255, 255));
                    message.add(args[1], TextColor.color(255, 255, 85));
                    message.add(" has ", TextColor.color(85, 255, 255));
                    message.add(" $" + (int) economy.getBalance(p), TextColor.color(255, 85, 255));
                    sender.sendMessage(ChatComponent.build(message));
                } else if (args[0].equalsIgnoreCase("config")) {
                    if (args[1].equalsIgnoreCase("reload")) {
                        plugin.reloadConfig();
                        plugin.saveConfig();
                        plugin.getConfig();
                        sender.sendMessage(Component.text("Config Reloaded"));
                    } else {
                        message.add("/currency config set <path> <value>\n", TextColor.color(255, 85, 85));
                        message.add("/currency config get <path>\n", TextColor.color(255, 85, 85));
                        message.add("/currency config reload", TextColor.color(255, 85, 85));
                        sender.sendMessage(ChatComponent.build(message));
                    }
                } else {
                    message.add("/currency set <player> <amount>\n", TextColor.color(255, 85, 85));
                    message.add("/currency get <player>\n", TextColor.color(255, 85, 85));
                    message.add("/currency give <player> <amount>\n", TextColor.color(255, 85, 85));
                    message.add("/currency take <player> <amount>\n", TextColor.color(255, 85, 85));
                    message.add("/currency help\n", TextColor.color(255, 85, 85));
                    message.add("/currency config <set:get:reload>", TextColor.color(255, 85, 85));
                    sender.sendMessage(ChatComponent.build(message));
                }
            } else if(args.length == 3) {
                if (!args[0].equalsIgnoreCase("config")) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
                    int amount = Helpers.parseInt(args[2]);
                    if (args[0].equalsIgnoreCase("set")) {
                        manager.setPlayerCurrency(p, amount);
                        message.add("Player ", TextColor.color(85, 255, 255));
                        message.add(args[1], TextColor.color(255, 255, 85));
                        message.add(" now has ", TextColor.color(85, 255, 255));
                        message.add(" $" + (int) economy.getBalance(p), TextColor.color(255, 85, 255));
                        sender.sendMessage(ChatComponent.build(message));
                    } else if (args[0].equalsIgnoreCase("give")) {
                        economy.depositPlayer(p, amount);
                        message.add("Player ", TextColor.color(85, 255, 255));
                        message.add(args[1], TextColor.color(255, 255, 85));
                        message.add(" now has ", TextColor.color(85, 255, 255));
                        message.add(" $" + (int) economy.getBalance(p), TextColor.color(255, 85, 255));
                        sender.sendMessage(ChatComponent.build(message));
                    } else if (args[0].equalsIgnoreCase("take")) {
                        economy.withdrawPlayer(p, amount);
                        message.add("Player ", TextColor.color(85, 255, 255));
                        message.add(args[1], TextColor.color(255, 255, 85));
                        message.add(" now has ", TextColor.color(85, 255, 255));
                        message.add(" $" + (int) economy.getBalance(p), TextColor.color(255, 85, 255));
                        sender.sendMessage(ChatComponent.build(message));
                    }
                } else {
                    if (args[1].equalsIgnoreCase("get")) {
                        if(plugin.getConfig().get(args[2]) != null) {
                            if(args[2].equalsIgnoreCase("offlinepaymessage")) {
                                message.add("Path offlinepaymessage is \"" + plugin.getConfig().get(args[2]) + "\"", TextColor.color(85, 255, 85));
                            } else {
                                message.add("Path " + args[2] + " is $" + plugin.getConfig().getInt(args[2]), TextColor.color(85, 255, 85));
                            }
                            sender.sendMessage(ChatComponent.build(message));
                        } else {
                            message.add("Path does not exist.", TextColor.color(255, 85, 85));
                        }
                    } else {
                        message.add("/currency config set <path> <value>\n", TextColor.color(255, 85, 85));
                        message.add("/currency config get <path>\n", TextColor.color(255, 85, 85));
                        message.add("/currency config reload", TextColor.color(255, 85, 85));
                        sender.sendMessage(ChatComponent.build(message));
                    }
                }
            } else {
                if (args[0].equalsIgnoreCase("config")) {
                    if (args[1].equalsIgnoreCase("set")) {
                        if(args[2].equalsIgnoreCase("offlinepaymessage")) {
                            plugin.getConfig().set("offlinepaymessage", Helpers.concatenate(args, 3, args.length - 1, " "));
                            message.add("Path offlinepaymessage set to \"" + plugin.getConfig().get(args[2]) + "\"", TextColor.color(85, 255, 85));
                            sender.sendMessage(ChatComponent.build(message));
                            plugin.saveConfig();
                        } else {
                            if(args.length == 4) {
                                if (plugin.getConfig().get(args[2]) != null) {
                                    plugin.getConfig().set(args[2], Helpers.parseInt(args[3]));
                                    message.add("Path " + args[2] + " set to $" + plugin.getConfig().get(args[2]), TextColor.color(85, 255, 85));
                                    sender.sendMessage(ChatComponent.build(message));
                                    plugin.saveConfig();
                                } else {
                                    message.add("Path does not exist.", TextColor.color(255, 85, 85));
                                    sender.sendMessage(ChatComponent.build(message));
                                }
                            } else {
                                message.add("Invalid argument", TextColor.color(255, 85, 85));
                                sender.sendMessage(ChatComponent.build(message));
                            }
                        }
                    } else {
                        message.add("/currency config set <path> <value>\n", TextColor.color(255, 85, 85));
                        message.add("/currency config get <path>\n", TextColor.color(255, 85, 85));
                        message.add("/currency config reload", TextColor.color(255, 85, 85));
                        sender.sendMessage(ChatComponent.build(message));
                    }
                } else {
                    message.add("/currency set <player> <amount>\n", TextColor.color(255, 85, 85));
                    message.add("/currency get <player>\n", TextColor.color(255, 85, 85));
                    message.add("/currency give <player> <amount>\n", TextColor.color(255, 85, 85));
                    message.add("/currency take <player> <amount>\n", TextColor.color(255, 85, 85));
                    message.add("/currency help\n", TextColor.color(255, 85, 85));
                    message.add("/currency config <set:get:reload>", TextColor.color(255, 85, 85));
                    sender.sendMessage(ChatComponent.build(message));
                }
            }
        } else {
            OfflinePlayer p = (OfflinePlayer) sender;
            message.add("You have ", TextColor.color(85, 255, 255));
            message.add(" $" + (int) economy.getBalance(p), TextColor.color(255, 85, 255));
            sender.sendMessage(ChatComponent.build(message));
        }

        return false;
    }
}
