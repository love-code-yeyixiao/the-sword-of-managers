package net.yeyixiao.block.procedures;

import net.yeyixiao.block.init.BlockModGameRules;
import net.yeyixiao.block.BlockMod;

import net.minecraft.world.level.LevelAccessor;

public class JianCeGuiZeProcedure {
	public static void execute(LevelAccessor world) {
		BlockMod.LOGGER.info(world.getLevelData().getGameRules().getBoolean(BlockModGameRules.KILL));
	}
}
