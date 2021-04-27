package net.worlddownloader.mixins;

import net.minecraft.level.dimension.DimensionFile;
import net.worlddownloader.DimFileInterface;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;

@Mixin(DimensionFile.class)
public class DimensionFileMixin implements DimFileInterface {
    @Shadow @Final private File parentFolder;
    @Override
    public File getParentFileWD() {
        return this.parentFolder;
    }
}
