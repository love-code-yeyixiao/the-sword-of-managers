package net.yeyixiao.block.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BlueScreenPackge {
    static public BlueScreenPackge decode(FriendlyByteBuf buf) {
        BlueScreenPackge msg = new BlueScreenPackge();
        return msg;
    }
    static public void encode(BlueScreenPackge msg, FriendlyByteBuf buf) {
    }
    public static void handle(BlueScreenPackge msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlerClass.handlePacket(msg, ctx))
        );
        ctx.get().setPacketHandled(true);
    }
}
