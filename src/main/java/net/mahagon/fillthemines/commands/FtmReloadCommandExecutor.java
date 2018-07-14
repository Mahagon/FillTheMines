package net.mahagon.fillthemines.commands;

import net.mahagon.fillthemines.FillTheMinesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FtmReloadCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        FillTheMinesPlugin.getPlugin().reloadConfig();
        sender.sendMessage(ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                + "] Config reloaded.");
        return true;
    }

}
