package net.mahagon.fillthemines.commands;


import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.mahagon.fillthemines.FillTheMinesPlugin;
import net.mahagon.fillthemines.worldedit.WorldEditRegion;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FtmCreateCommandExecutor implements CommandExecutor {
    private WorldEditPlugin worldEdit = (WorldEditPlugin) FillTheMinesPlugin.getPlugin().getServer()
            .getPluginManager().getPlugin("WorldEdit");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (!(sender instanceof Player)) {
            FillTheMinesPlugin.getPlugin().getServer().getLogger()
                    .info("[FTM] Sry but using this from console is not implemented yet.");
            // TODO extra areguments for console
            return true;
        }
        FileConfiguration config = FillTheMinesPlugin.getPlugin().getConfig();
        final Player player = (Player) sender;
        try {
            if (args.length == 5) {
                // get worldedit selection from player
                final int[] weCoordSel = WorldEditRegion.getPlayerSelectionCoord(player, worldEdit);
                String firstCorner = Integer.toString(weCoordSel[0]) + "," + Integer.toString(weCoordSel[1])
                        + "," + Integer.toString(weCoordSel[2]);
                String secondCorner = Integer.toString(weCoordSel[3]) + ","
                        + Integer.toString(weCoordSel[4]) + "," + Integer.toString(weCoordSel[5]);
                String world = player.getWorld().getName().toLowerCase();
                String name = args[1].toLowerCase();
                String template = args[2].toLowerCase();
                boolean refillonrestart = Boolean.parseBoolean(args[3]);
                boolean rundaily = Boolean.parseBoolean(args[4]);
                // check: template exists in config
                String mineName = config.getString("mines." + name);
                if (!(mineName == null || mineName.isEmpty())) {
                    throw new IllegalArgumentException("Mine " + name + " already exist.");
                }
                // check: template exists in config
                String mineTemplate = config.getString("templates." + template);
                if (mineTemplate == null || mineTemplate.isEmpty()) {
                    throw new IllegalArgumentException("Template " + template + " does not exist.");
                }
                // writing to config
                config.options().configuration().set("mines." + name + ".refillonrestart", refillonrestart);
                config.options().configuration().set("mines." + name + ".rundaily", rundaily);
                config.options().configuration().set("mines." + name + ".lastrun", 00000000);
                config.options().configuration().set("mines." + name + ".corners.first", firstCorner);
                config.options().configuration().set("mines." + name + ".corners.second", secondCorner);
                config.options().configuration().set("mines." + name + ".world", world);
                config.options().configuration().set("mines." + name + ".template", template);
                FillTheMinesPlugin.getPlugin().saveConfig();
                // sending infos to player
                player
                        .sendMessage(ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                                + "] " + name + " has been saved.");
                player.sendMessage(
                        ChatColor.GOLD + "  - " + ChatColor.WHITE + "First corner: " + firstCorner);
                player.sendMessage(
                        ChatColor.GOLD + "  - " + ChatColor.WHITE + "Second corner: " + secondCorner);
                player.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + "World: " + world);
                player.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + "Number of Blocks: "
                        + Integer.toString(weCoordSel[6]));
                player.sendMessage(ChatColor.GOLD + "  - " + ChatColor.WHITE + "Template: " + template);
            } else {
                throw new IllegalArgumentException("Wrong number of arguments.");
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + e.getMessage());
            return false;
        }
        return true;
    }
}
