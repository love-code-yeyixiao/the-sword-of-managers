
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.yeyixiao.block.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockModGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> KILL = GameRules.register("kill", GameRules.Category.PLAYER,
			GameRules.BooleanValue.create(false));
}
