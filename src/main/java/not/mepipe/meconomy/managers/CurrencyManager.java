package not.mepipe.meconomy.managers;

import not.mepipe.meconomy.Main;
import not.mepipe.meconomy.utils.Logger;
import org.bukkit.OfflinePlayer;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CurrencyManager {

    private static CurrencyManager manager = null;

    private static HashMap<UUID, Integer> currency = new HashMap<>();

    private final Main plugin = Main.getPlugin();

    public static CurrencyManager getInstance() {
        if (manager == null) {
            manager = new CurrencyManager();
        }
        return manager;
    }

    private File getDataFile() {
        File file = new File(plugin.getDataFolder(), "currency.dat");
        if (!file.exists()) {
            Logger.send(Logger.MessageType.INFO, "currency.dat file not found. Attempting to create...");
            try {
                if (file.createNewFile()) {
                    Logger.send(Logger.MessageType.GOOD, "currency.dat file created successfully");
                }
            } catch (IOException e) {
                Logger.send(Logger.MessageType.BAD, "currency.dat file creation failed");
                Logger.send(e);
            }
        } else {
            Logger.send(Logger.MessageType.GOOD, "currency.dat file found");
        }

        return file;
    }

    public void saveCurrencyFile() {
        File file = getDataFile();
        try {
            ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(Files.newOutputStream(file.toPath())));

            output.writeObject(currency);
            output.flush();
            output.close();
            Logger.send(Logger.MessageType.GOOD, "Data saved successfully");
        } catch(IOException e) {
            Logger.send(Logger.MessageType.BAD, "Data saving error");
            Logger.send(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadCurrencyFile() {
        File file = getDataFile();

        try {
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(Files.newInputStream(file.toPath())));
            Object readObject = input.readObject();
            currency = (HashMap<UUID, Integer>) readObject;

            input.close();
            Logger.send(Logger.MessageType.GOOD, "Data loaded successfully");
        } catch (EOFException ignored) {
        } catch (Throwable e) {
            Logger.send(Logger.MessageType.BAD, "Data loading error");
            Logger.send(e);
        }
    }

    // Create add, remove, set, and get methods
    public void addCurrencyToPlayer(OfflinePlayer p, int amount) {
        if(currency.get(p.getUniqueId()) != null) {
            currency.put(p.getUniqueId(), currency.get(p.getUniqueId()) + amount);
        } else {
            currency.put(p.getUniqueId(), amount);
        }
    }

    public void removeCurrencyFromPlayer(OfflinePlayer p, int amount) {
        if(currency.get(p.getUniqueId()) != null) {
            currency.put(p.getUniqueId(), currency.get(p.getUniqueId()) - amount);
        }
    }

    public void setPlayerCurrency(OfflinePlayer p, int amount) {
        currency.put(p.getUniqueId(), amount);
    }

    public int getPlayerCurrency(OfflinePlayer p) {
        if(hasCurrency(p)) {
            return currency.get(p.getUniqueId());
        } else {
            return 0;
        }
    }

    public boolean hasCurrency(OfflinePlayer p) {
        return currency.get(p.getUniqueId()) != null;
    }

    public boolean hasCurrency(UUID uuid) {
        return currency.get(uuid) != null;
    }

}
