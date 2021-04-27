package net.worlddownloader.mixins;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.ingame.Pause;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.level.dimension.DimensionFile;
import net.worlddownloader.ClientLevelInterface;
import net.worlddownloader.DimFileInterface;
import net.worlddownloader.MultiplayerChunkCacheInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(Pause.class)
public class PauseMixin extends ScreenBase {
    private int stopDownloadIn = -1;

    @Inject(at = @At(value = "RETURN"), method = "init()V")
    public void init(CallbackInfo ci){
        if(this.minecraft.hasLevel()){
            byte byte0 = -16;
            ((Button)this.buttons.get(0)).y = height / 4 + 144 + byte0;
            if(!((ClientLevelInterface)minecraft.level).isDownloadThisWorld())
                this.buttons.add(new Button(7, width / 2 - 100, height / 4 + 120 + byte0, "Download this world"));
            else
                this.buttons.add(new Button(7, width / 2 - 100, height / 4 + 120 + byte0, "Stop downloading this world"));
        }
    }

    @Inject(at = @At("RETURN"), method = "buttonClicked")
    public void buttonClicked(Button button, CallbackInfo ci){
        if(button.id == 7)
        {
            if(((ClientLevelInterface)minecraft.level).isDownloadThisWorld())
            {
                ((Button)this.buttons.get(7)).text = "Saving a shitload of data...";
                stopDownloadIn = 2;
            }
            else
            {
                startDownload();
                minecraft.openScreen(null);
                minecraft.lockCursor();
            }
        }
    }

    @Inject(at = @At(value = "JUMP", ordinal = 2), method = "buttonClicked")
    public void disconnect(Button button, CallbackInfo ci){
        if(this.minecraft.hasLevel()) {
            if (((ClientLevelInterface) minecraft.level).isDownloadThisWorld())
                stopDownload();
        }
    }

    @Inject(at = @At("RETURN"), method = "render")
    public void render(int mouseX, int mouseY, float delta, CallbackInfo ci){
        if(stopDownloadIn == 0) {
            stopDownload();
            minecraft.openScreen(null);
            minecraft.lockCursor();
        }
        else if(stopDownloadIn > 0)
            stopDownloadIn--;
    }

    private void startDownload()
    {
        String worldName = minecraft.options.lastServer;
        if(worldName.isEmpty()) worldName = "Downloaded World";
        ClientLevel wc = (ClientLevel)minecraft.level;
        wc.getProperties().setName(worldName);
        ((ClientLevelInterface)wc).setDownloadSaveHandler((DimensionFile) minecraft.getLevelStorage().createDimensionFile(worldName, false));
        ((ClientLevelInterface)wc).setDownloadChunkLoader(((ClientLevelInterface)wc).getDownloadSaveHandler().getChunkIO(wc.dimension));
        wc.getProperties().setSizeOnDisk(getFileSizeRecursive(((DimFileInterface)((ClientLevelInterface)wc).getDownloadSaveHandler()).getParentFileWD()));
        ((MultiplayerChunkCacheInterface)((ClientLevelInterface)wc).getChunkCacheS()).importOldTileEntities();
        ((ClientLevelInterface)wc).setDownloadThisWorld(true);
        minecraft.overlay.addChatMessage("§c[WorldDL] §cDownloading everything you can see...");
        minecraft.overlay.addChatMessage("§c[WorldDL] §6You can increase that area by travelling around.");
    }

    private void stopDownload()
    {
        stopDownloadIn = -1;
        ClientLevel wc = (ClientLevel)minecraft.level;
        wc.saveLevel(true, null);
        ((ClientLevelInterface)wc).setDownloadThisWorld(false);
        ((ClientLevelInterface)wc).setDownloadChunkLoader(null);
        ((ClientLevelInterface)wc).setDownloadSaveHandler(null);
        minecraft.overlay.addChatMessage("§c[WorldDL] §cDownload stopped.");
    }

    private long getFileSizeRecursive(File f)
    {
        long size = 0;
        File[] list = f.listFiles();
        for(File nf : list)
        {
            if( nf.isDirectory() )
                size += getFileSizeRecursive(nf);
            else if( nf.isFile() )
                size += nf.length();
        }
        return size;
    }

}
