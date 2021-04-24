package net.worlddownloader.mixins;

import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.dimension.Nether;
import net.minecraft.level.storage.RegionLoader;
import net.minecraft.packet.Id15Packet;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.minecraft.util.io.NBTIO;
import net.minecraft.util.maths.TilePos;
import net.worlddownloader.ChunkInterface;
import net.worlddownloader.ClientLevelInterface;
import net.worlddownloader.ClientLevelS;
import net.worlddownloader.DimFileInterface;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mixin(Chunk.class)
public class ChunkMixin implements ChunkInterface {

    @Shadow public Level level;
    @Shadow @Final public int x;
    @Shadow @Final public int z;
    public boolean isFilled = false;
    public Map newChunkTileEntityMap;
    @Override
    public boolean isFilled() {
        return isFilled;
    }
    @Override
    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }
    @Override
    public Map getNewChunkTileEntityMap() {
        return newChunkTileEntityMap;
    }
    @Override
    public void setNewChunkTileEntityMap(Map newChunkTileEntityMap) {
        this.newChunkTileEntityMap = newChunkTileEntityMap;
    }

    @Override
    public void setNewChunkBlockTileEntity(int i, int j, int k, TileEntityBase tileentity) {
        TilePos chunkposition = new TilePos(i, j, k);
        tileentity.level = level;
        tileentity.x = this.x * 16 + i;
        tileentity.y = j;
        tileentity.z = this.z * 16 + k;
        newChunkTileEntityMap.put(chunkposition, tileentity);
    }

    @Override
    public void importOldChunkTileEntities() {
        ClientLevel wc = ClientLevelS.clientLevel;
        File file = ((DimFileInterface)((ClientLevelInterface)wc).getDownloadSaveHandler()).getParentFileWD();
        if(wc.dimension instanceof Nether)
        {
            file = new File(file, "DIM-1");
            file.mkdirs();
        }

        java.io.DataInputStream datainputstream = RegionLoader.method_1215(file, this.x, this.z);
        CompoundTag nbttagcompound;
        if(datainputstream != null)
        {
            nbttagcompound = NBTIO.readTag(datainputstream);
        }
        else return;

        if(!nbttagcompound.containsKey("Level"))
            return;

        ListTag nbttaglist1 = nbttagcompound.getCompoundTag("Level").getListTag("TileEntities");
        if(nbttaglist1 != null)
        {
            for(int l = 0; l < nbttaglist1.size(); l++)
            {
                CompoundTag nbttagcompound2 = (CompoundTag)nbttaglist1.get(l);
                TileEntityBase te = TileEntityBase.tileEntityFromNBT(nbttagcompound2);
                if(te != null )
                {
                    TilePos cp = new TilePos(te.x & 0xf, te.y, te.z & 0xf);
                    newChunkTileEntityMap.put(cp, te);
                }
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "method_891")
    public void method_891(byte[] bs, int i, int j, int k, int i1, int i2, int i3, int i4, CallbackInfoReturnable<Integer> cir){
        isFilled = true;
    }

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/level/chunk/Chunk;<init>(Lnet/minecraft/level/Level;II)V")
    public void init(Level level, int x, int z, CallbackInfo ci){
        newChunkTileEntityMap = new HashMap();
    }
}
