package net.worlddownloader;

import net.minecraft.level.chunk.Chunk;

public interface MultiplayerChunkCacheInterface {
    public void saveChunk(Chunk chunk);
    public Chunk loadChunk(int i, int j);
    public void importOldTileEntities();
}
