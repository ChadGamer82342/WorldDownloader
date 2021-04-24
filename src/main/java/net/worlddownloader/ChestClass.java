package net.worlddownloader;

import net.minecraft.client.level.ClientLevel;
import net.minecraft.inventory.DoubleChest;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.tileentity.TileEntityChest;


public class ChestClass {
    public static InventoryBase buildEntity(Level level, int i, int j, int k, int size)  {
        TileEntityBase te = new TileEntityChest();
        ((ClientLevelInterface) level).setNewBlockTileEntity(i, j, k, te);

        InventoryBase obj = (InventoryBase) te;
        if (size <= 27)
            return obj;

        te = new TileEntityChest();

        if (level.getTileId(i - 1, j, k) == 54) {
            ((ClientLevelInterface) level).setNewBlockTileEntity(i - 1, j, k, te);
            obj = new DoubleChest("Large chest", (InventoryBase) te, obj);
        }
        if (level.getTileId(i + 1, j, k) == 54) {
            ((ClientLevelInterface) level).setNewBlockTileEntity(i + 1, j, k, te);
            obj = new DoubleChest("Large chest", obj, (InventoryBase) te);
        }
        if (level.getTileId(i, j, k - 1) == 54) {
            ((ClientLevelInterface) level).setNewBlockTileEntity(i, j, k - 1, te);
            obj = new DoubleChest("Large chest", (InventoryBase) te, obj);
        }
        if (level.getTileId(i, j, k + 1) == 54) {
            ((ClientLevelInterface) level).setNewBlockTileEntity(i, j, k + 1, te);
            obj = new DoubleChest("Large chest", obj, (InventoryBase) te);
        }
        return (InventoryBase) obj;
    }
}
