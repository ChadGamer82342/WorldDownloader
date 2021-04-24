package net.worlddownloader.mixins;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.ingame.Pause;
import net.minecraft.client.gui.widgets.Button;
import net.worlddownloader.ClientLevelInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Pause.class)
public class PauseMixin extends ScreenBase {
    @Inject(at = @At(value = "HEAD"), method = "init()V")
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
}
