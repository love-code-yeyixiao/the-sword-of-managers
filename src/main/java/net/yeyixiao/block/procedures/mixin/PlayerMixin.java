package net.yeyixiao.block.procedures.mixin;

import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.yeyixiao.block.init.BlockModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Player.class)
public abstract class PlayerMixin extends MixinPlayer{

    /**
     * @author
     * @reason
     */
    /*
    @Overwrite
    public void die(DamageSource p_36152_) {
        // 检查位置是否设置到原点，并执行自定义逻辑
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, p_36152_)) return;
        super.die(p_36152_);
        this.reapplyPosition();
        if (!this.isSpectator()) {
            this.dropAllDeathLoot(p_36152_);
        }

        if (p_36152_ != null) {
            this.setDeltaMovement((double)(-Mth.cos((this.hurtDir + this.getYRot()) * ((float)Math.PI / 180F)) * 0.1F), (double)0.1F, (double)(-Mth.sin((this.hurtDir + this.getYRot()) * ((float)Math.PI / 180F)) * 0.1F));
        } else {
            this.setDeltaMovement(0.0D, 0.1D, 0.0D);
        }

        this.awardStat(Stats.DEATHS);
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        this.clearFire();
        this.setSharedFlagOnFire(false);

        // 此时正常执行，不执行自定义处理
    }
    @Shadow
    public abstract boolean isLocalPlayer();
    /*@Shadow
    public abstract ItemStack getItemInHand(InteractionHand hand);*/
}
