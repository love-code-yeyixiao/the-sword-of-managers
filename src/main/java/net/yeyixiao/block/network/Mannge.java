package net.yeyixiao.block.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Mannge {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("block", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static void register(){
        int id = 0;
        INSTANCE.registerMessage(id++,BlueScreenPackge.class,BlueScreenPackge::encode,BlueScreenPackge::decode,BlueScreenPackge::handle);
    }
}
