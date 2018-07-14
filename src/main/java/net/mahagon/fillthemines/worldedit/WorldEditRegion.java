package net.mahagon.fillthemines.worldedit;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldEditRegion {
    private Selection selection;

    private WorldEditRegion(Selection sel) {
        this.selection = sel;
    }

    private static WorldEditRegion fromPlayerSelection(Player player, Plugin plugin) {
        final Selection selection = ((WorldEditPlugin) plugin).getSelection(player);
        if (selection == null) {
            throw new IllegalArgumentException("Make a selection first!");
        }
        if (!(selection instanceof CuboidSelection)) {
            throw new IllegalArgumentException("You have to define a cuboid selection");
        }
        return new WorldEditRegion(selection);
    }

    /**
     * Returning the corner coords and the world of a player.
     */
    public static int[] getPlayerSelectionCoord(Player player, Plugin plugin) {
        int[] selectionCoords = new int[7];
        try {
            Selection playerSelection =
                    WorldEditRegion.fromPlayerSelection(player, plugin).getSelection();
            Vector playerSelectionMin = playerSelection.getNativeMinimumPoint();
            Vector playerSelectionMax = playerSelection.getNativeMaximumPoint();
            selectionCoords[0] = playerSelectionMin.getBlockX();
            selectionCoords[1] = playerSelectionMin.getBlockY();
            selectionCoords[2] = playerSelectionMin.getBlockZ();
            selectionCoords[3] = playerSelectionMax.getBlockX();
            selectionCoords[4] = playerSelectionMax.getBlockY();
            selectionCoords[5] = playerSelectionMax.getBlockZ();
            selectionCoords[6] = playerSelection.getArea();
            return selectionCoords;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Selection getSelection() {
        return selection;
    }
}
