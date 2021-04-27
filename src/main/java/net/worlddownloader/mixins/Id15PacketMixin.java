package net.worlddownloader.mixins;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.Id15Packet;
import net.worlddownloader.ClientLevelInterface;
import net.worlddownloader.ClientLevelS;
import net.worlddownloader.Id15PacketInterface;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Id15Packet.class)
public abstract class Id15PacketMixin extends AbstractPacket implements Id15PacketInterface {
    @Shadow public int field_542;
    @Shadow public int field_543;
    @Shadow public int field_544;

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/packet/Id15Packet;field_546:Lnet/minecraft/item/ItemInstance;",
            opcode = Opcodes.PUTFIELD), method = "Lnet/minecraft/packet/Id15Packet;<init>(IIIILnet/minecraft/item/ItemInstance;)V")
    public void placeInject(Id15Packet id15Packet, ItemInstance value){
        id15Packet.field_546 = value;
        if(((ClientLevelInterface)ClientLevelS.clientLevel).isDownloadThisWorld())
        {
            if(isID(BlockBase.CHEST.id) || isID(BlockBase.FURNACE.id) || isID(BlockBase.FURNACE_LIT.id) || isID(BlockBase.DISPENSER.id))
                ((ClientLevelInterface)ClientLevelS.clientLevel).setOpenContainerPacket(id15Packet);
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
}
