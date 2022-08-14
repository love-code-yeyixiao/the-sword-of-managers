
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.yeyixiao.block.init;

import net.yeyixiao.block.item.ChaoChangDeJianItem;
import net.yeyixiao.block.BlockMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;

public class BlockModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BlockMod.MODID);
	public static final RegistryObject<Item> SHENG_CAO_DE_FANG_KUAI = block(BlockModBlocks.SHENG_CAO_DE_FANG_KUAI,
			CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Item> CHAO_CHANG_DE_JIAN = REGISTRY.register("chao_chang_de_jian", () -> new ChaoChangDeJianItem());

	private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
}
