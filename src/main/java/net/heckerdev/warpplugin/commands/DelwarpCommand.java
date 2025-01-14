package net.heckerdev.warpplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.warpplugin.database.DataSource;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static net.heckerdev.warpplugin.WarpPlugin.getInstance;

@CommandAlias("delwarp|removewarp")
public class DelwarpCommand extends BaseCommand {

    @Default
    @Syntax("<warpname>")
    public void onDefault(CommandSender sender, String[] args) {
        if (!sender.hasPermission("warpplugin.command.delwarp")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You do not have permission to use this command!"));
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You need to specify a warp name! <gray>Usage: /delwarp <warpname>"));
                } else {
                    String warpName = args[0];
                    try {
                        Connection connection = DataSource.getConnection();
                        PreparedStatement preparedStatement;
                        preparedStatement = connection.prepareStatement("DELETE FROM WarpsTable WHERE WarpName = ?");
                        preparedStatement.setString(1, warpName.toLowerCase());
                        preparedStatement.execute();
                        connection.close();
                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>✔ Successfully deleted warp <gold>" + warpName + "<green>!"));
                    } catch (SQLException ex) {
                        getInstance().getLogger().warning("Error deleting warp: " + ex);
                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>❌ An error occurred while deleting the warp, check the console for more information!"));
                    }
                }
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You must be a player to use this command!"));
            }
        }
    }
}
