package net.mahagon.fillthemines;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BlockEdit {
    /**
     * Fills a region with blocks async -> runs setBlocks -> sync
     */
    public static void fillRegion(final int[] weCoordSel, final World world, final String template,
                                  final String mineName) {
        final FileConfiguration config = FillTheMinesPlugin.getPlugin().getConfig();
        final int waittime = config.getInt("blockset.waittimeperrow") * 50; // ticks to ms
        Bukkit.getServer().getScheduler().runTaskAsynchronously(FillTheMinesPlugin.getPlugin(),
                () -> {
                    final String[] templateBlocks = template.replaceAll("\\s+", "").split(",");
                    // get max value for random > 1 = above 100%
                    double lastNumber = 0;
                    // Material , chance
                    final String[][] blockList = new String[templateBlocks.length][2];
                    // translate template string
                    int i = 0;
                    for (String templateBlock : templateBlocks) {
                        String[] block = templateBlock.split("%");
                        lastNumber += (Double.parseDouble(block[1]) / 100);
                        blockList[i][0] = block[0];
                        blockList[i][1] = Double.toString(lastNumber);
                        i++;
                    }
                    final double fLastNumber = lastNumber; // addition of percentage
                    for (int x = weCoordSel[0]; x <= weCoordSel[3]; x++) { // x of the cuboid
                        final int fCurrentX = x; // current x
                        final int fMaxX = weCoordSel[3]; // max X
                        Bukkit.getServer().getScheduler()
                                .scheduleSyncDelayedTask(FillTheMinesPlugin.getPlugin(), new Runnable() {
                                    @Override
                                    public void run() {
                                        setBlocks(weCoordSel, world, template, fCurrentX, fMaxX, fLastNumber,
                                                templateBlocks.length, blockList, mineName);
                                    }
                                });
                        // waittime for less lagg
                        try {
                            Thread.sleep(waittime);
                        } catch (InterruptedException e) {
                            FillTheMinesStartupTask.setRunningState(false);
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Fills a region with blocks sync
     */
    private static void setBlocks(int[] weCoordSel, World world, String template, int x, int lastX,
                                  double lastNumber, int templateBlocksCount, String[][] blockList, String mineName) {
        // set blocks in cuboid (one row)
        for (int y = weCoordSel[1]; y <= weCoordSel[4]; y++) {
            for (int z = weCoordSel[2]; z <= weCoordSel[5]; z++) {
                double random = (Math.random() * lastNumber);
                for (int i = 0; i <= templateBlocksCount; i++) {
                    if (random <= Double.parseDouble(blockList[i][1])) {
                        world.getBlockAt(x, y, z).setType(Material.matchMaterial(blockList[i][0]), false);
                        break;
                    }
                }
            }
        }
        // if last row done -> infomsg
        if (x == lastX) {
            for (Player p : FillTheMinesPlugin.getPlugin().getServer().getOnlinePlayers()) {
                if (p.hasPermission("ftm.notify")) {
                    p.sendMessage(ChatColor.GOLD + "[" + FillTheMinesPlugin.getPlugin().getName()
                            + "] " + mineName + " has been successfully filled.");// informs player with
                    // permission ftm.notify
                }
            }
            FillTheMinesPlugin.getPlugin().getLogger().info(mineName + " has been successfully filled.");// console
            // msg
            FillTheMinesStartupTask.setRunningState(false);
        }
    }
}
