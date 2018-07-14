package net.mahagon.fillthemines.commands;


import net.mahagon.fillthemines.FillTheMinesPlugin;
import net.mahagon.fillthemines.SpigotUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FtmInfoCommandExecutor implements CommandExecutor {
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
                }
                // reading from config
                // sending infos to player
                sender.sendMessage(ChatColor.GOLD + "["
                        + FillTheMinesPlugin.getPlugin().getName() + "] " + name + " information.");
                sender.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + "First corner: "
                        + config.options().configuration().get("mines." + name + ".corners.first"));
                sender.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + "Second corner: "
                        + config.options().configuration().get("mines." + name + ".corners.second"));
                sender.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + "World: "
                        + config.options().configuration().get("mines." + name + ".world"));
                sender.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + "Template: "
                        + config.options().configuration().get("mines." + name + ".template"));
                sender.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + "Run on restart: " + config
                        .options().configuration().get("mines." + name + ".refillonrestart").toString());
                sender.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + "Run daily: "
                        + config.options().configuration().get("mines." + name + ".rundaily").toString());
            } else {
                throw new IllegalArgumentException("Wrong number of arguments.");
            }
        } catch (IllegalArgumentException e) {
            // Send errormessage to player
            sender.sendMessage(ChatColor.RED + "[" + FillTheMinesPlugin.getPlugin().getName()
                    + "] " + e.getMessage());
            if (sender instanceof Player) {
                for (String mine : config.getConfigurationSection("mines").getKeys(false)) {
                    SpigotUtils.sendTellraw((Player) sender,
                            ChatColor.GOLD + "  - " + ChatColor.WHITE + mine, "/ftm info " + mine,
                            "Shows information about mine: " + mine);
                }
            }
            return false;
        }
        return true;
    }

}
