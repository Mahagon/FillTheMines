package net.mahagon.fillthemines.commands;

import net.mahagon.fillthemines.FillTheMinesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class FtmTemplateCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (args.length > 1) {
            FileConfiguration config = FillTheMinesPlugin.getPlugin().getConfig();
            switch (args[1].toLowerCase()) {
                case "create":
                    try {
                        if (args.length == 4) {
                            String name = args[2].toLowerCase();
                            String template = args[3].toLowerCase();
                            // check: template exists in config
                            String mineTemplate = config.getString("templates." + name);
                            if (!(mineTemplate == null || mineTemplate.isEmpty())) {
                                throw new IllegalArgumentException("Template " + name + " already exists.");
                            }
                            // validate template
                            try {
                                String[] templateBlocks = template.replaceAll("\\s+", "").split(",");
                                // Material , chance
                                String[][] blockList = new String[templateBlocks.length][2];
                                // translate template string
                                int i = 0;
                                for (String templateBlock : templateBlocks) {
                                    String[] block = templateBlock.split("%");
                                    blockList[i][0] = block[0];
                                    blockList[i][1] = block[1];
                                    i++;
                                }
                            } catch (Exception e) {
                                throw new IllegalArgumentException(
                                        "Template Syntax is wrong. It should be ITEM%PERCENTAGE,NEXTITEM%PERCENTAGE etc.");
                            }
                            // writing to config
                            config.options().configuration().set("templates." + name, template);
                            FillTheMinesPlugin.getPlugin().saveConfig();
                            // sending infos to player
                            sender.sendMessage(
                                    ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                                            + "] Template " + name + " has been saved.");
                        } else {
                            throw new IllegalArgumentException("Wrong number of arguments.");
                        }
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(ChatColor.RED + e.getMessage());
                        return false;
                    }
                    break;
                case "delete":
                    try {
                        if (args.length == 3) {
                            String name = args[2].toLowerCase();
                            // check: template exists in config
                            String templateName = config.getString("templates." + name);
                            if (templateName == null || templateName.isEmpty()) {
                                throw new IllegalArgumentException("Template " + name + " does not exist.");
                            } else {
                                config.set("templates." + name, null);
                                FillTheMinesPlugin.getPlugin().saveConfig();
                                sender.sendMessage(
                                        ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                                                + "] Template " + name + " has been deleted.");
                            }
                        } else {
                            throw new IllegalArgumentException("Wrong number of arguments.");
                        }
                    } catch (IllegalArgumentException e) {
                        // Send errormessage to player
                        sender.sendMessage(ChatColor.RED + "["
                                + FillTheMinesPlugin.getPlugin().getName() + "] " + e.getMessage());
                        return false;
                    }
                    break;
                default:
                    sender
                            .sendMessage(ChatColor.RED + "[" + FillTheMinesPlugin.getPlugin().getName()
                                    + "] Wrong argument. Use create or delete.");
                    break;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "[" + FillTheMinesPlugin.getPlugin().getName()
                    + "] Wrong number of arguments.");
            return false;
        }
        return true;
    }
}
