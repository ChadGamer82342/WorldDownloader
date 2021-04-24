package net.worlddownloader.mixins;

import net.minecraft.block.BlockBase;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.dimension.DimensionFile;
import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.Id15Packet;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.tileentity.TileEntityNoteblock;
import net.worlddownloader.ChunkInterface;
import net.worlddownloader.ClientLevelInterface;
import net.worlddownloader.ClientLevelS;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.util.ProgressListener;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin extends Level implements ClientLevelInterface {
    public boolean downloadThisWorld = false;
    public ChunkIO downloadChunkLoader;
    public DimensionFile downloadSaveHandler;
    public Id15Packet openContainerPacket;

    public ClientLevelMixin(DimensionData dimensionData, String name, Dimension dimension, long seed) { super(dimensionData, name, dimension, seed); }

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/client/level/ClientLevel;<init>(Lnet/minecraft/network/ClientPlayNetworkHandler;JI)V")
    public void init(ClientPlayNetworkHandler arg, long l, int i, CallbackInfo ci){
        ClientLevelS.clientLevel = (ClientLevel)(Level)this;
        System.out.println("TEST!");
    }

    public void saveWorld(boolean flag, ProgressListener iprogressupdate) {
        if(this.downloadThisWorld)
        {
            downloadSaveHandler.saveLevelDataOnMP(this.properties, this.entities);
            cache.deleteCacheCauseClientCantHandleThis(flag, iprogressupdate);
        }
        super.saveLevel(flag, iprogressupdate);
    }

    @Override
    public void method_224(int i, int j, int k, int l, int i1) {
        super.method_224(i, j, k, l, i1);
        if(!this.downloadThisWorld)
            return;
        if(this.getTileId(i, j, k) == BlockBase.NOTEBLOCK.id)
        {
            TileEntityNoteblock tileentitynote = (TileEntityNoteblock)getTileEntity(i, j, k);
            if(tileentitynote == null)
                setTileEntity(i, j, k, new TileEntityNoteblock());
            if(tileentitynote != null){
                tileentitynote.note = (byte)(i1 % 25);
                tileentitynote.markDirty();
                setNewBlockTileEntity(i, j, k, tileentitynote);
            }
        }
    }

    @Override
    public void setNewBlockTileEntity(int i, int j, int k, TileEntityBase tileentity) {
        Chunk chunk = this.getChunkFromCache(i >> 4, k >> 4);
        if(chunk != null)
        {
            ((ChunkInterface)chunk).setNewChunkBlockTileEntity(i & 0xf, j, k & 0xf, tileentity);
        }
    }

    @Override
    public void setDownloadThisWorld(boolean downloadThisWorld) {
        this.downloadThisWorld = downloadThisWorld;
    }

    @Override
    public void setDownloadChunkLoader(ChunkIO downloadChunkLoader) {
        this.downloadChunkLoader = downloadChunkLoader;
    }

    @Override
    public void setDownloadSaveHandler(DimensionFile downloadSaveHandler) {

    }

    @Override
    public void setOpenContainerPacket(Id15Packet openContainerPacket) {
        this.openContainerPacket = openContainerPacket;
    }

    @Override
    public boolean isDownloadThisWorld() {
        return this.downloadThisWorld;
    }

    @Override
    public ChunkIO getDownloadChunkLoader() {
        return this.downloadChunkLoader;
    }

    @Override
    public DimensionFile getDownloadSaveHandler() {
        return this.downloadSaveHandler;
    }

    @Override
    public Id15Packet getOpenContainerPacket() {
        return this.openContainerPacket;
    }
}
