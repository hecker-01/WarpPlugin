package net.heckerdev.warpplugin;

import co.aikar.commands.PaperCommandManager;
import net.heckerdev.warpplugin.commands.*;
import net.heckerdev.warpplugin.database.DataSource;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class WarpPlugin extends JavaPlugin {

    private static WarpPlugin plugin;

    private static Permission perms = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setupConfig();
        setupPermissions();
        DataSource.initializeDatabase();
        setupCommands();
        getLogger().info("Successfully loaded WarpPlugin!");
        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("WarpPlugin disabled!");
    }

    private void setupCommands() {
        // Registering commands.
        PaperCommandManager manager= new PaperCommandManager(this);
        manager.registerCommand(new SetwarpCommand());
        manager.registerCommand(new WarpCommand());
        manager.registerCommand(new DelwarpCommand());
        manager.registerCommand(new WarplistCommand());
    }

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning("- Disabled because Vault is not installed!");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Permission getPermissions() {
        return perms;
    }

    private void setupConfig() {
        // Registering config.
        // Current version
        double currentVersion = 1.0;
        saveDefaultConfig();
        if (getConfig().getDouble("file-version") < currentVersion) {
            getLogger().warning("Updating config.yml...");
            saveResource("config.yml", true);
            reloadConfig();
            getLogger().info("Successfully updated config.yml!");
            getLogger().warning("Please reconfigure the config.yml file, is has been reset!");
        } else if (getConfig().getDouble("file-version") > currentVersion) {
            getLogger().warning(" - Disabled because the config.yml is from a newer version!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static WarpPlugin getInstance() {
        return plugin;
    }
}
