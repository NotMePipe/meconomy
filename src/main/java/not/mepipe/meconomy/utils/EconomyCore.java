package not.mepipe.meconomy.utils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import not.mepipe.meconomy.Main;
import not.mepipe.meconomy.managers.CurrencyManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class EconomyCore implements Economy {

    private final CurrencyManager manager = CurrencyManager.getInstance(Main.getPlugin());

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "MeConomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return String.valueOf(amount);
    }

    @Override
    public String currencyNamePlural() {
        return currencyNameSingular();
    }

    @Override
    public String currencyNameSingular() {
        return "$";
    }

    @Override
    public boolean hasAccount(String uuid) {
        return manager.hasCurrency(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return manager.hasCurrency(offlinePlayer);
    }

    @Override
    public boolean hasAccount(String uuid, String worldName) {
        if(Bukkit.getPlayer(uuid) != null) {
            Player player = Bukkit.getPlayer(uuid);
            if(Objects.requireNonNull(Bukkit.getWorld(worldName)).getPlayers().contains(player)) {
                assert player != null;
                return manager.hasCurrency(player.getUniqueId());
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        if(offlinePlayer == null) return false;
        return manager.hasCurrency(offlinePlayer);
    }

    @Override
    public double getBalance(String uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if(hasAccount(uuid)) {
            return manager.getPlayerCurrency(player);
        }
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        if(hasAccount(offlinePlayer)) {
            return manager.getPlayerCurrency(offlinePlayer);
        }
        return 0;
    }

    @Override
    public double getBalance(String s, String s1) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return 0;
    }

    @Override
    public boolean has(String s, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String uuid, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if(hasAccount(uuid)) {
            int balance = manager.getPlayerCurrency(player);
            if(balance >= amount) {
                manager.removeCurrencyFromPlayer(player, (int) amount);
                return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "You paid $" + (int) amount);
            } else {
                return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, "You don't have enough money!");
            }
        }

        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "You do not have an account!");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        if(hasAccount(offlinePlayer)) {
            int balance = manager.getPlayerCurrency(offlinePlayer);
            if(balance >= amount) {
                manager.removeCurrencyFromPlayer(offlinePlayer, (int) amount);
                return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "You paid $" + (int) amount);
            } else {
                return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, "You don't have enough money!");
            }
        }

        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "You do not have an account!");
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String uuid, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if(hasAccount(uuid)) {
            int balance = manager.getPlayerCurrency(player);
            manager.addCurrencyToPlayer(player, (int) amount);
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "You have been paid $" + (int) amount);
        }
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have an account!");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        if(hasAccount(offlinePlayer)) {
            int balance = manager.getPlayerCurrency(offlinePlayer);
            manager.addCurrencyToPlayer(offlinePlayer, (int) amount);
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "You have been paid $" + (int) amount);
        }
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have an account!");
    }

    @Override
    public EconomyResponse depositPlayer(String sender, String receiver, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if(!hasAccount(player)) {
            manager.setPlayerCurrency(player, 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        if(!hasAccount(offlinePlayer)) {
            manager.setPlayerCurrency(offlinePlayer, 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(String uuid, String worldName) {
        if(Bukkit.getPlayer(uuid) != null) {
            Player player = Bukkit.getPlayer(uuid);

            if(Objects.requireNonNull(Bukkit.getWorld(worldName)).getPlayers().contains(player)) {
                if(!hasAccount(uuid)) {
                    manager.setPlayerCurrency(Bukkit.getOfflinePlayer(uuid), 0);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        if(!hasAccount(offlinePlayer)) {
            manager.setPlayerCurrency(offlinePlayer, 0);
            return true;
        }
        return false;
    }
}
