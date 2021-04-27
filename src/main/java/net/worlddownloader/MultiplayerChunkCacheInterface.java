package net.worlddownloader;

import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.chunk.MultiplayerChunkCache;

public interface MultiplayerChunkCacheInterface {
    public void saveChunk(Chunk chunk);
    public Chunk loadChunkS(int i, int j);
    public void importOldTileEntities();
}
