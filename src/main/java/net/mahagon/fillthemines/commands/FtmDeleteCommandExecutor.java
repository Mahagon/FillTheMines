package net.mahagon.fillthemines.commands;

import net.mahagon.fillthemines.FillTheMinesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class FtmDeleteCommandExecutor implements CommandExecutor {
    private FileConfiguration config = FillTheMinesPlugin.getPlugin().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        try {
            if (args.length == 2) {
                String name = args[1].toLowerCase();
                // check: template exists in config
                String mineName = config.getString("mines." + name);
                if (mineName == null || mineName.isEmpty()) {
                    throw new IllegalArgumentException("Mine " + name + " does not exist.");
                } else {
                    config.set("mines." + name, null);
                    FillTheMinesPlugin.getPlugin().saveConfig();
                    sender.sendMessage(
                            ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName() + "] "
                                    + name + " has been deleted.");
                }
            } else {
                throw new IllegalArgumentException("Wrong number of arguments.");
            }
        } catch (IllegalArgumentException e) {
            // Send errormessage to player
            sender.sendMessage(ChatColor.RED + "[" + FillTheMinesPlugin.getPlugin().getName()
                    + "] " + e.getMessage());
            return false;
        }
        return true;
    }
}
