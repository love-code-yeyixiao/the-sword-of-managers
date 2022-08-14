
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.yeyixiao.block.init;

import net.yeyixiao.block.block.ShengCaoDeFangKuaiBlock;
import net.yeyixiao.block.BlockMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.block.Block;

public class BlockModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, BlockMod.MODID);
	public static final RegistryObject<Block> SHENG_CAO_DE_FANG_KUAI = REGISTRY.register("sheng_cao_de_fang_kuai",
			() -> new ShengCaoDeFangKuaiBlock());

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientSideHandler {
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event) {
			ShengCaoDeFangKuaiBlock.registerRenderLayer();
		}
	}
}
