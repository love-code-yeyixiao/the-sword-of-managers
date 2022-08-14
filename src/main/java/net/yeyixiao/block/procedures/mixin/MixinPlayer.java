package net.yeyixiao.block.procedures.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class MixinPlayer{


    //@Shadow public abstract void die(DamageSource p_21014_);

    /*@Shadow
    public abstract ItemStack getItemInHand(InteractionHand hand);*/
}
