package net.worlddownloader.mixins;

import net.minecraft.block.*;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.chunk.MultiplayerChunkCache;
import net.minecraft.level.source.LevelSource;
import net.minecraft.tileentity.TileEntityBase;
import net.worlddownloader.ChunkInterface;
import net.worlddownloader.ClientLevelInterface;
import net.worlddownloader.MultiplayerChunkCacheInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Map;

@Mixin(MultiplayerChunkCache.class)
public abstract class MultiplayerChunkCacheMixin implements MultiplayerChunkCacheInterface, LevelSource {

    @Shadow private Level level;

    @Shadow
    public abstract Chunk getChunk(int chunkX, int chunkZ);

    @Shadow private Map multiplayerChunkCache;

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;"), method = "method_1954")
    public void unloadChunk(int x, int z, CallbackInfo ci){
        Chunk chunk = this.getChunk(x, z);
        if(((ClientLevelInterface)level).isDownloadThisWorld() && !chunk.field_968 && ((ChunkInterface)chunk).isFilled())
        {
            this.saveChunk(chunk);
            ((ClientLevelInterface)level).getDownloadChunkLoader().iDoNothingToo(level, chunk);
        }
    }

    @Override
    public void saveChunk(Chunk chunk) {
        if(!((ClientLevelInterface)level).isDownloadThisWorld())
            return;
        chunk.lastUpdate = level.getLevelTime();
        chunk.decorated = true;
        for( Object ob : ((ChunkInterface)chunk).getNewChunkTileEntityMap().keySet())
        {
            TileEntityBase te = (TileEntityBase)((ChunkInterface)chunk).getNewChunkTileEntityMap().get(ob);
            if(te != null)
            {
                BlockBase block = BlockBase.BY_ID[level.getTileId(te.x, te.y, te.z)];
                if(block instanceof Chest || block instanceof Dispenser || block instanceof Furnace || block instanceof Noteblock)
                    chunk.field_964.put(ob, te);
            }
        }
        ((ClientLevelInterface)level).getDownloadChunkLoader().saveChunk(level, chunk);
    }

    @Override
    public Chunk loadChunk(int chunkX, int chunkZ) {
        return ((ClientLevelInterface)level).getDownloadChunkLoader().getChunk(level, chunkX, chunkZ);
    }

    @Override
    public void importOldTileEntities() {
        for(Object ccip : multiplayerChunkCache.keySet())
        {
            Chunk c = (Chunk)multiplayerChunkCache.get(ccip);
            if(c != null && c.decorated)
            {
                ((ChunkInterface)c).importOldChunkTileEntities();
            }
        }
    }
}
