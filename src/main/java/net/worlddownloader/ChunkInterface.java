package net.worlddownloader;

import net.minecraft.client.level.ClientLevel;
import net.minecraft.tileentity.TileEntityBase;

import java.util.Map;

public interface ChunkInterface {
    public boolean isFilled();
    public void setFilled(boolean filled);
    public Map getNewChunkTileEntityMap();
    public void setNewChunkTileEntityMap(Map newChunkTileEntityMap);
    public void setNewChunkBlockTileEntity(int i, int j, int k, TileEntityBase tileentity);
    public void importOldChunkTileEntities();
}
