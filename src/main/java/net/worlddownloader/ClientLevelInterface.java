package net.worlddownloader;

import net.minecraft.client.level.ClientLevel;
import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.dimension.DimensionFile;
import net.minecraft.packet.Id15Packet;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.ProgressListener;

public interface ClientLevelInterface {
    public void saveWorld(boolean flag, ProgressListener iprogressupdate);
    public void setNewBlockTileEntity(int i, int j, int k, TileEntityBase tileentity);

    public void setDownloadThisWorld(boolean downloadThisWorld);
    public void setDownloadChunkLoader(ChunkIO downloadChunkLoader);
    public void setDownloadSaveHandler(DimensionFile downloadSaveHandler);
    public void setOpenContainerPacket(Id15Packet openContainerPacket);
    public boolean isDownloadThisWorld();
    public ChunkIO getDownloadChunkLoader();
    public DimensionFile getDownloadSaveHandler();
    public Id15Packet getOpenContainerPacket();
}
