package net.heckerdev.warpplugin;

import co.aikar.commands.PaperCommandManager;
import net.heckerdev.warpplugin.commands.*;
import net.heckerdev.warpplugin.database.DataSource;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class WarpPlugin extends JavaPlugin {

    private static Permission perms = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        DataSource.initializeDatabase();
        setupCommands();
        setupPermissions();
        getLogger().info("enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("disabled");
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

    public static WarpPlugin getInstance() {
        return getPlugin(WarpPlugin.class);
    }
}
