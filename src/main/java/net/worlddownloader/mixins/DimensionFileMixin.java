package net.worlddownloader.mixins;

import net.minecraft.client.level.ClientLevel;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.DimensionFile;
import net.minecraft.level.dimension.Nether;
import net.minecraft.level.storage.RegionLoader;
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
import java.util.HashMap;
import java.util.Map;

@Mixin(DimensionFile.class)
public class DimensionFileMixin implements DimFileInterface {
    @Shadow @Final private File parentFolder;
    @Override
    public File getParentFileWD() {
        return this.parentFolder;
    }
}
