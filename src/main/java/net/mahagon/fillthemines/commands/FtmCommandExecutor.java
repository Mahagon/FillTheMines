package net.mahagon.fillthemines.commands;

import net.mahagon.fillthemines.FillTheMinesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.Map;
import java.util.Set;

public class FtmCommandExecutor implements CommandExecutor {
    /**
     * Called on Command.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        try {
            if (args.length < 1) {
                throw new IllegalArgumentException("must be more than one");
            }
            PluginCommand pluginCmd = Bukkit.getServer().getPluginCommand(cmdLabel + " " + args[0]);

            if (!pluginCmd.testPermissionSilent(sender)) {
                sender.sendMessage(ChatColor.RED + "[" + FillTheMinesPlugin.getPlugin().getName()
                        + "] You don't have permission.");
                return true;
            }

            return pluginCmd.execute(sender, cmdLabel, args);
        } catch (Exception e) {
            PluginDescriptionFile description = FillTheMinesPlugin.getPlugin().getDescription();
            sender.sendMessage(ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                    + "] Version " + description.getVersion()); // show version
            Map<String, Map<String, Object>> customers = description.getCommands();
            Set<String> keys = customers.keySet();
            for (String singleKey : keys) {
                sender.sendMessage(ChatColor.GOLD + "  " + customers.get(singleKey).get("description"));
                sender.sendMessage(ChatColor.GOLD + "  - usage: " + ChatColor.WHITE
                        + customers.get(singleKey).get("usage"));
                sender.sendMessage(ChatColor.GOLD + "  - permission: " + ChatColor.WHITE
                        + customers.get(singleKey).get("permission"));
                sender.sendMessage(ChatColor.GOLD + " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
            }
            return true;
        }
    }
}
