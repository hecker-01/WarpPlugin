package net.heckerdev.warpplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.warpplugin.database.DataSource;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static net.heckerdev.warpplugin.WarpPlugin.getInstance;

@CommandAlias("setwarp|createwarp")
public class SetwarpCommand extends BaseCommand {
    @Default
    @Syntax("<warpname> [hidden]")
    @CommandCompletion(" true|false")
    public void onDefault(CommandSender sender, String[] args) {
        if (!sender.hasPermission("warpplugin.command.setwarp")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You do not have permission to use this command!"));
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You need to specify a warp name! <gray>Usage: /setwarp <n><warpname>"));
                } else {
                    String warpName = args[0];
                    Location loc = player.getLocation();
                    String worldName = loc.getWorld().getName();
                    double x = loc.getX();
                    double y = loc.getY();
                    double z = loc.getZ();
                    double pitch = loc.getPitch();
                    double yaw = loc.getYaw();
                    boolean hidden = false;
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("true")) {
                            hidden = true;
                        }
                    }
                    try {
                        Connection connection = DataSource.getConnection();
                        PreparedStatement preparedStatement;
                        preparedStatement = connection.prepareStatement("INSERT INTO WarpsTable(WarpName, WorldName, X, Y, Z, Yaw, Pitch, Hidden) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                        preparedStatement.setString(1, warpName.toLowerCase());
                        preparedStatement.setString(2, worldName);
                        preparedStatement.setDouble(3, x);
                        preparedStatement.setDouble(4, y);
                        preparedStatement.setDouble(5, z);
                        preparedStatement.setDouble(6, yaw);
                        preparedStatement.setDouble(7, pitch);
                        preparedStatement.setBoolean(8, hidden);
                        preparedStatement.execute();
                        connection.close();
                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<green><bold>✔ <green>Successfully created warp <gold>" + warpName + "<green>!"));
                    } catch (SQLException ex) {
                        getInstance().getLogger().warning("Error adding warp: " + ex);
                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌ <red>An error occurred while adding the warp, check the console for more information!"));
                    }
                }
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You must be a player to use this command!"));
            }
        }
    }
}
