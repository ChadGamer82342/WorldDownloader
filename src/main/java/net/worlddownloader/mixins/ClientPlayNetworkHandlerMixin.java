package net.worlddownloader.mixins;

import net.minecraft.block.BlockBase;
import net.minecraft.client.ClientInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.Id15Packet;
import net.minecraft.packet.play.OpenScreenS2C;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.worlddownloader.ChestClass;
import net.worlddownloader.ClientLevelInterface;
import net.worlddownloader.Id15PacketInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin extends PacketHandler {
    @Shadow private ClientLevel level;
    @Shadow private Minecraft minecraft;

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/AbstractClientPlayer;openChestScreen(Lnet/minecraft/inventory/InventoryBase;)V",
            shift = At.Shift.AFTER),
            method = "handleScreenOpen")
    public void downloadPlayerInventory(OpenScreenS2C packet, CallbackInfo ci){
        Id15Packet ocp = ((ClientLevelInterface)level).getOpenContainerPacket();
        InventoryBase inventory;
        if(((ClientLevelInterface)level).isDownloadThisWorld() && ocp != null && ((Id15PacketInterface)ocp).isID(BlockBase.CHEST.id))
        {
            inventory = ChestClass.buildEntity(level, ocp.field_542, ocp.field_543, ocp.field_544, packet.field_744);
        }
        else
        {
            inventory = new ClientInventory(packet.field_743, packet.field_744);
        }
        minecraft.player.openChestScreen(inventory);
    }

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/AbstractClientPlayer;openFurnaceScreen(Lnet/minecraft/tileentity/TileEntityFurnace;)V",
            shift = At.Shift.AFTER),
            method = "handleScreenOpen")
    public void downloadFurnaceInventory(OpenScreenS2C packet, CallbackInfo ci){
        Id15Packet ocp = ((ClientLevelInterface)level).getOpenContainerPacket();
        TileEntityFurnace tileentityfurnace = new TileEntityFurnace();
        if(((ClientLevelInterface)level).isDownloadThisWorld() && ocp != null && (((Id15PacketInterface)ocp).isID(BlockBase.FURNACE.id) ||
                ((Id15PacketInterface)ocp).isID(BlockBase.FURNACE_LIT.id)))
        {
            ((ClientLevelInterface)level).setNewBlockTileEntity(ocp.field_542, ocp.field_543, ocp.field_544, tileentityfurnace);
        }
    }

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/AbstractClientPlayer;openDispenserScreen(Lnet/minecraft/tileentity/TileEntityDispenser;)V",
            shift = At.Shift.AFTER),
            method = "handleScreenOpen")
    public void downloadDispenserInventory(OpenScreenS2C packet, CallbackInfo ci){
        Id15Packet ocp = ((ClientLevelInterface)level).getOpenContainerPacket();
        TileEntityDispenser tileentitydispenser = new TileEntityDispenser();
        if(((ClientLevelInterface)level).isDownloadThisWorld() && ocp != null && (((Id15PacketInterface)ocp).isID(BlockBase.DISPENSER.id)))
        {
            ((ClientLevelInterface)level).setNewBlockTileEntity(ocp.field_542, ocp.field_543, ocp.field_544, tileentitydispenser);
        }
    }

    @Override
    public boolean method_1474() {
        return false;
    }
}
