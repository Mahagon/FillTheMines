package net.mahagon.fillthemines.commands;


import net.mahagon.fillthemines.FillTheMinesPlugin;
import net.mahagon.fillthemines.SpigotUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FtmListCommandExecutor implements CommandExecutor {
    private FileConfiguration config = FillTheMinesPlugin.getPlugin().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        switch (args.length) {
            case 1:
                if (sender instanceof Player) {
                    sender.sendMessage(ChatColor.GOLD + "["
                            + FillTheMinesPlugin.getPlugin().getName() + "] Choose:");
                    SpigotUtils.sendTellraw((Player) sender,
                            ChatColor.GOLD + "  - " + ChatColor.WHITE + "List mines.", "/ftm list mines",
                            "Shows up a list of mines");
                    SpigotUtils.sendTellraw((Player) sender,
                            ChatColor.GOLD + "  - " + ChatColor.WHITE + "List templates.", "/ftm list templates",
                            "Shows up a list of templates");
                    return true;
                } else {
                    return false;
                }
            case 2:
                switch (args[1].toLowerCase()) {
                    case "mines":
                        sender.sendMessage(
                                ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                                        + "] List of configured mines:");
                        if (sender instanceof Player) {
                            for (String mine : config.getConfigurationSection("mines").getKeys(false)) {
                                SpigotUtils.sendTellraw((Player) sender,
                                        ChatColor.GOLD + "  - " + ChatColor.WHITE + mine, "/ftm info " + mine,
                                        "Shows up information for mine: " + mine);
                            }
                        } else {
                            for (String mine : config.getConfigurationSection("mines").getKeys(false)) {
                                sender.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + mine);
                            }
                        }
                        return true;
                    case "templates":
                        sender.sendMessage(
                                ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                                        + "] List of configured templates:");
                        for (String template : config.getConfigurationSection("templates").getKeys(false)) {
                            sender.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + template);
                            sender.sendMessage(ChatColor.GOLD + " contains: " + ChatColor.WHITE
                                    + config.getString("templates." + template));
                        }
                        return true;
                    default:
                        return false;
                }
            default:
                return false;
        }
    }
}
