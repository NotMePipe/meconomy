package not.mepipe.meconomy;

import net.milkbowl.vault.economy.Economy;
import not.mepipe.meconomy.commands.CurrencyCommand;
import not.mepipe.meconomy.commands.PayCommand;
import not.mepipe.meconomy.listeners.*;
import not.mepipe.meconomy.managers.CurrencyManager;
import not.mepipe.meconomy.utils.EconomyCore;
import not.mepipe.meconomy.utils.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    FileConfiguration config = getConfig();

    private static Main plugin;

    public static final String name = "MeConomy";

    public boolean meMailEnabled;

    @Override
    public void onEnable() {
        plugin = this;
        config.options().copyDefaults(true);
        saveDefaultConfig();
        registerListeners();
        registerManagers();

        if(!setupEconomy()) {
            Logger.send(Logger.MessageType.BAD, "Economy could not be registered:\n\tVault is missing!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        meMailEnabled = setupMeMail();

        Logger.send(Logger.MessageType.GOOD, "Economy registered successfully");

        registerCommands();
        Logger.send(Logger.MessageType.GOOD, "Plugin initialized!");
    }

    private boolean setupMeMail() {
        if (getServer().getPluginManager().getPlugin("MeMail") == null) {
            Logger.send(Logger.MessageType.INFO, "MeMail not found");
            return false;
        } else {
            Logger.send(Logger.MessageType.INFO, "MeMail found in plugins folder");
        }

        if (!config.getBoolean("use-memail")) {
            Logger.send(Logger.MessageType.INFO, "MeMail disabled in config");
            return false;
        } else {
            Logger.send(Logger.MessageType.INFO, "Enabled MeMail");
        }

        return true;
    }

    private boolean setupEconomy() {
        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        getServer().getServicesManager().register(Economy.class, new EconomyCore(), this, ServicePriority.Highest);
        return true;
    }

    @Override
    public void onDisable() {
        saveData();
    }

    public void saveData() {
        CurrencyManager.getInstance().saveCurrencyFile();
    }

    public void registerManagers() {
        CurrencyManager currencyManager = CurrencyManager.getInstance();
        currencyManager.loadCurrencyFile();
    }

    public void registerCommands() {
        new PayCommand();
        new CurrencyCommand();
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockHarvestListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSaveListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new FishingListener(), this);
    }

    public static Main getPlugin() {
        return plugin;
    }


}
