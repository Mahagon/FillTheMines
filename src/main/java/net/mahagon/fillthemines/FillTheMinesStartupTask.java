package net.mahagon.fillthemines;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

class FillTheMinesStartupTask {
    private static boolean state = false;

    /**
     * Fills a region with blocks async -> runs setBlocks -> sync
     */
    static void fillAllMines() {
        final FileConfiguration config = FillTheMinesPlugin.getPlugin().getConfig();
        final int waittimeStart = config.getInt("refillonrestart.starttime") * 1000 / 20; // ticks to ms
        final String broadcast = config.getString("refill.warnmsg"); // warningmsg
        final int waittimeRefill = config.getInt("refill.waittime") * 1000 / 20; // ticks to ms
        final Set<String> allMines = config.getConfigurationSection("mines").getKeys(false);
        final Date dt = new Date();
        final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Bukkit.getServer().getScheduler().runTaskAsynchronously(FillTheMinesPlugin.getPlugin(),
                () -> {
                    // waittime after start
                    try {
                        Thread.sleep(waittimeStart);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // inform players
                    Bukkit.getServer().getScheduler()
                            .scheduleSyncDelayedTask(FillTheMinesPlugin.getPlugin(), new Runnable() {
                                @Override
                                public void run() {
                                    Bukkit.getServer()
                                            .broadcastMessage(ChatColor.GOLD + "["
                                                    + FillTheMinesPlugin.getPlugin().getName() + "] "
                                                    + broadcast
                                                    .replaceAll("%WAITTIME%", Integer.toString(waittimeRefill / 1000))
                                                    .replaceAll("&([0-9a-f])", "\u00A7$1"));
                                }
                            });
                    // waittime after warning
                    try {
                        Thread.sleep(waittimeRefill);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (String singleMine : allMines) {
                        // check: mine exists in config
                        final String mineName = config.getString("mines." + singleMine.toLowerCase());
                        if (mineName == null || mineName.isEmpty()) {
                            throw new IllegalArgumentException("Mine does not exist.");
                        }
                        // check: mine should be filled on startup
                        Boolean mineStartup =
                                config.getBoolean("mines." + singleMine.toLowerCase() + ".refillonrestart");
                        if (!mineStartup) {
                            Bukkit.getLogger().info(singleMine + " wont be filled on startup.");
                            continue;
                        }
                        // check: mine should be filled on startup once a day
                        Boolean mineStartupDaily =
                                config.getBoolean("mines." + singleMine.toLowerCase() + ".rundaily");
                        if (mineStartupDaily) {
                            if (config.getInt("mines." + singleMine.toLowerCase() + ".lastrun") >= Integer
                                    .parseInt(df.format(dt))) {
                                Bukkit.getLogger().info(singleMine + " has already been filled today.");
                                continue;
                            }

                        }
                        // check: mine has template set
                        String mineTemplateName =
                                config.getString("mines." + singleMine.toLowerCase() + ".template");
                        if (mineTemplateName == null || mineTemplateName.isEmpty()) {
                            throw new IllegalArgumentException("No template defined for " + mineName + ".");
                        }
                        // check: template exists
                        final String mineTemplate =
                                config.getString("templates." + mineTemplateName.toLowerCase());
                        if (mineTemplate == null || mineTemplate.isEmpty()) {
                            throw new IllegalArgumentException(
                                    "Template " + mineTemplateName + " does not exist.");
                        }
                        // check: mine has 1st corner set
                        String mineCorner1st =
                                config.getString("mines." + singleMine.toLowerCase() + ".corners.first");
                        if (mineCorner1st == null || mineCorner1st.isEmpty()) {
                            throw new IllegalArgumentException("No first corner defined for " + mineName + ".");
                        }
                        // check: mine has 2nd corner set
                        String mineCorner2nd =
                                config.getString("mines." + singleMine.toLowerCase() + ".corners.second");
                        if (mineCorner2nd == null || mineCorner2nd.isEmpty()) {
                            throw new IllegalArgumentException(
                                    "No second corner defined for " + mineName + ".");
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
                        final int[] areaCoords = new int[6];
                        // X coords
                        if (Integer.parseInt(mineCorner1stArray[0]) < Integer
                                .parseInt(mineCorner2ndArray[0])) {
                            areaCoords[0] = Integer.parseInt(mineCorner1stArray[0]);
                            areaCoords[3] = Integer.parseInt(mineCorner2ndArray[0]);
                        } else {
                            areaCoords[3] = Integer.parseInt(mineCorner1stArray[0]);
                            areaCoords[0] = Integer.parseInt(mineCorner2ndArray[0]);
                        }
                        // Y coords
                        if (Integer.parseInt(mineCorner1stArray[1]) < Integer
                                .parseInt(mineCorner2ndArray[1])) {
                            areaCoords[1] = Integer.parseInt(mineCorner1stArray[1]);
                            areaCoords[4] = Integer.parseInt(mineCorner2ndArray[1]);
                        } else {
                            areaCoords[4] = Integer.parseInt(mineCorner1stArray[1]);
                            areaCoords[1] = Integer.parseInt(mineCorner2ndArray[1]);
                        }
                        // Z coords
                        if (Integer.parseInt(mineCorner1stArray[2]) < Integer
                                .parseInt(mineCorner2ndArray[2])) {
                            areaCoords[2] = Integer.parseInt(mineCorner1stArray[2]);
                            areaCoords[5] = Integer.parseInt(mineCorner2ndArray[2]);
                        } else {
                            areaCoords[5] = Integer.parseInt(mineCorner1stArray[2]);
                            areaCoords[2] = Integer.parseInt(mineCorner2ndArray[2]);
                        }
                        // check: mine has world set
                        final String mineWorld =
                                config.getString("mines." + singleMine.toLowerCase() + ".world");
                        if (mineWorld.isEmpty()) {
                            throw new IllegalArgumentException("No world defined for " + mineName + ".");
                        } else if (Bukkit.getWorld(mineWorld) == null) {
                            throw new IllegalArgumentException("World " + mineWorld + " does not exist.");
                        }
                        BlockEdit.fillRegion(areaCoords, Bukkit.getWorld(mineWorld), mineTemplate,
                                singleMine);
                        // writes the last rundate to the config
                        config.options().configuration().set("mines." + singleMine.toLowerCase() + ".lastrun",
                                Integer.parseInt(df.format(dt)));
                        // save config

                        // filling one after another
                        setRunningState(true);
                        do {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                FillTheMinesStartupTask.setRunningState(false);
                                e.printStackTrace();
                            }
                        } while (getRunningState());
                    }
                    FillTheMinesPlugin.getPlugin().saveConfig();
                });
    }

    /**
     * get filling state
     */
    private static boolean getRunningState() {
        return state;
    }

    /**
     * set filling state
     */
    static void setRunningState(Boolean setState) {
        state = setState;
    }
}
