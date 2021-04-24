package net.worlddownloader.mixins;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Chest;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.item.ItemInstance;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.Id15Packet;
import net.worlddownloader.ClientLevelInterface;
import net.worlddownloader.ClientLevelS;
import net.worlddownloader.Id15PacketInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;

@Mixin(Id15Packet.class)
public class Id15PacketMixin extends AbstractPacket implements Id15PacketInterface {
    @Shadow public int field_543;
    @Shadow public int field_544;
    @Shadow public int field_542;

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/packet/Id15Packet;<init>(IIIILnet/minecraft/item/ItemInstance;)V")
    public void placeInject(int i, int j, int k, int i1, ItemInstance arg, CallbackInfo ci){
        if(((ClientLevelInterface)ClientLevelS.clientLevel).isDownloadThisWorld())
        {
            if(isID(BlockBase.CHEST.id) || isID(BlockBase.FURNACE.id) || isID(BlockBase.FURNACE_LIT.id) || isID(BlockBase.DISPENSER.id))
                ((ClientLevelInterface)ClientLevelS.clientLevel).setOpenContainerPacket(new Id15Packet(i, j, k, i1, arg));
            else
                ((ClientLevelInterface)ClientLevelS.clientLevel).setOpenContainerPacket(null);
        }
    }

    @Override
    public boolean isID(int id) {
        int ID = ClientLevelS.clientLevel.getTileId(this.field_542, this.field_543, this.field_544);
        if(ID == id)
            return true;
        else
            return false;
    }

    @Override
    public void read(DataInputStream in) {

    }
    @Override
    public void write(DataOutputStream out) {

    }
    @Override
    public void handle(PacketHandler handler) { }
    @Override
    public int length() {
        return 0;
    }
}
