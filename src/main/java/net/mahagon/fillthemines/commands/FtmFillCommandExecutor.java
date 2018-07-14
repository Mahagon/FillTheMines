package net.mahagon.fillthemines.commands;


import net.mahagon.fillthemines.BlockEdit;
import net.mahagon.fillthemines.FillTheMinesPlugin;
import net.mahagon.fillthemines.SpigotUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FtmFillCommandExecutor implements CommandExecutor {
    private FileConfiguration config = FillTheMinesPlugin.getPlugin().getConfig();

    /**
     * Called on Command.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        try {
            if (args.length == 2) {
                // check: mine exists in config
                String mineName = config.getString("mines." + args[1].toLowerCase());
                if (mineName == null || mineName.isEmpty()) {
                    throw new IllegalArgumentException("Mine does not exist.");
                }
                // check: mine has template set
                String mineTemplateName = config.getString("mines." + args[1].toLowerCase() + ".template");
                if (mineTemplateName == null || mineTemplateName.isEmpty()) {
                    throw new IllegalArgumentException("No template defined for " + mineName + ".");
                }
                // check: template exists
                String mineTemplate = config.getString("templates." + mineTemplateName.toLowerCase());
                if (mineTemplate == null || mineTemplate.isEmpty()) {
                    throw new IllegalArgumentException("Template " + mineTemplateName + " does not exist.");
                }
                // check: mine has 1st corner set
                String mineCorner1st =
                        config.getString("mines." + args[1].toLowerCase() + ".corners.first");
                if (mineCorner1st == null || mineCorner1st.isEmpty()) {
                    throw new IllegalArgumentException("No first corner defined for " + mineName + ".");
                }
                // check: mine has 2nd corner set
                String mineCorner2nd =
                        config.getString("mines." + args[1].toLowerCase() + ".corners.second");
                if (mineCorner2nd == null || mineCorner2nd.isEmpty()) {
                    throw new IllegalArgumentException("No second corner defined for " + mineName + ".");
                }
                // check region
                String[] mineCorner1stArray = mineCorner1st.replaceAll("\\s+", "").split(",");
                if (!(mineCorner1stArray.length == 3)) {
                    throw new IllegalArgumentException(
                            "First corner of " + mineName + " has no valid format: x,y,z.");
                }
                String[] mineCorner2ndArray = mineCorner2nd.replaceAll("\\s+", "").split(",");
                if (!(mineCorner2ndArray.length == 3)) {
                    throw new IllegalArgumentException(
                            "Second corner of " + mineName + " has no valid format: x,y,z.");
                }
                int[] areaCoords = new int[6];
                // X coords
                if (Integer.parseInt(mineCorner1stArray[0]) < Integer.parseInt(mineCorner2ndArray[0])) {
                    areaCoords[0] = Integer.parseInt(mineCorner1stArray[0]);
                    areaCoords[3] = Integer.parseInt(mineCorner2ndArray[0]);
                } else {
                    areaCoords[3] = Integer.parseInt(mineCorner1stArray[0]);
                    areaCoords[0] = Integer.parseInt(mineCorner2ndArray[0]);
                }
                // Y coords
                if (Integer.parseInt(mineCorner1stArray[1]) < Integer.parseInt(mineCorner2ndArray[1])) {
                    areaCoords[1] = Integer.parseInt(mineCorner1stArray[1]);
                    areaCoords[4] = Integer.parseInt(mineCorner2ndArray[1]);
                } else {
                    areaCoords[4] = Integer.parseInt(mineCorner1stArray[1]);
                    areaCoords[1] = Integer.parseInt(mineCorner2ndArray[1]);
                }
                // Z coords
                if (Integer.parseInt(mineCorner1stArray[2]) < Integer.parseInt(mineCorner2ndArray[2])) {
                    areaCoords[2] = Integer.parseInt(mineCorner1stArray[2]);
                    areaCoords[5] = Integer.parseInt(mineCorner2ndArray[2]);
                } else {
                    areaCoords[5] = Integer.parseInt(mineCorner1stArray[2]);
                    areaCoords[2] = Integer.parseInt(mineCorner2ndArray[2]);
                }
                // check: mine has world set
                String mineWorld = config.getString("mines." + args[1].toLowerCase() + ".world");
                if (mineWorld.isEmpty()) {
                    throw new IllegalArgumentException("No world defined for " + mineName + ".");
                } else if (Bukkit.getWorld(mineWorld) == null) {
                    throw new IllegalArgumentException("World " + mineWorld + " does not exist.");
                }
                // Infomessage
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("ftm.notify") && sender != p) {
                        p.sendMessage(ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                                + "] " + sender.getName() + " startet refilling for mine " + args[1] + "."); // informs
                        // player
                        // with
                        // permission
                        // ftm.notify
                    }
                }
                sender
                        .sendMessage(ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                                + "] startet refilling for mine " + args[1] + "."); // player msg
                FillTheMinesPlugin.getPlugin().getLogger()
                        .info(sender.getName() + " startet refilling for mine " + args[1] + ".");// console msg
                // set blocks
                BlockEdit.fillRegion(areaCoords, Bukkit.getWorld(mineWorld), mineTemplate, args[1]);
            } else {
                throw new IllegalArgumentException("Wrong number of arguments.");
            }
        } catch (IllegalArgumentException e) {
            // Send errormessage to player
            sender.sendMessage(ChatColor.RED + "[" + FillTheMinesPlugin.getPlugin().getName()
                    + "] " + e.getMessage());
            if ((sender instanceof Player)) {
                sender
                        .sendMessage(ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                                + "] Click on the mine you wanted to fill.");
                for (String mine : config.getConfigurationSection("mines").getKeys(false)) {
                    SpigotUtils.sendTellraw((Player) sender,
                            ChatColor.GOLD + "  - " + ChatColor.WHITE + mine, "/ftm fill " + mine,
                            "Fill mine: " + mine);
                }
            }
            return false;
        }
        return true;
    }
}

