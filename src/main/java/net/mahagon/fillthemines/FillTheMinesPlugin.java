package net.mahagon.fillthemines;

import net.mahagon.fillthemines.commands.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FillTheMinesPlugin extends JavaPlugin {
    private static Plugin plugin;
    private FileConfiguration config = getConfig();

    /**
     * Called on Command.
     */
    public static Plugin getPlugin() {
        return plugin;
    }

    /**
     * Called on plugin enable.
     */
    @Override
    public void onEnable() {
        plugin = this;
        // Config -> create default values if not exist
        config.addDefault("blockset.waittimeperrow", 20);
        config.addDefault("refillonrestart.starttime", 600);
        config.addDefault("refill.waittime", 600);
        config.addDefault("refill.warnmsg",
                "&6Please leave the mining areas, they will be filled in %WAITTIME% seconds");
        if (config.getConfigurationSection("mines") == null) {
            config.addDefault("mines.mine1.refillonrestart", false);
            config.addDefault("mines.mine1.rundaily", false);
            config.addDefault("mines.mine1.lastrun", 00000000);
            config.addDefault("mines.mine1.corners.first", "5,70,5");
            config.addDefault("mines.mine1.corners.second", "10,80,10");
            config.addDefault("mines.mine1.world", "world");
            config.addDefault("mines.mine1.template", "normal");
        }
        config.addDefault("templates.normal",
                "GOLD_BLOCK%0.0001,EMERALD_ORE%0.002,WEB%0.005,MOSSY_COBBLESTONE%0.007,OBSIDIAN%0.008,COBBLESTONE%0.026,DIAMOND_ORE%0.021,LAPIS_ORE%0.022,GOLD_ORE%0.058,REDSTONE_ORE%0.169,IRON_ORE%0.582,SAND%0.804,SANDSTONE%0.824,COAL_ORE%1.030,GRAVEL%1.387,DIRT%3.877,STONE%82.748");
        config.options().copyDefaults(true);
        saveConfig();
        // Create CommandExecutors
        this.getCommand("ftm").setExecutor(new FtmCommandExecutor());
        this.getCommand("ftm create").setExecutor(new FtmCreateCommandExecutor());
        this.getCommand("ftm delete").setExecutor(new FtmDeleteCommandExecutor());
        this.getCommand("ftm fill").setExecutor(new FtmFillCommandExecutor());
        this.getCommand("ftm list").setExecutor(new FtmListCommandExecutor());
        this.getCommand("ftm info").setExecutor(new FtmInfoCommandExecutor());
        this.getCommand("ftm template").setExecutor(new FtmTemplateCommandExecutor());
        this.getCommand("ftm reload").setExecutor(new FtmReloadCommandExecutor());
        FillTheMinesStartupTask.fillAllMines();
    }

    /**
     * Called on plugin disable.
     */
    @Override
    public void onDisable() {
        plugin = null;
    }

}
