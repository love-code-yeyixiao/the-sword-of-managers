package net.yeyixiao.block.procedures;

import net.minecraft.Util;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.yeyixiao.block.init.BlockModItems;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult;

@Mod.EventBusSubscriber
public class arrow {
    private static boolean check(Player player) {
        //if(player.isLocalPlayer()){return false;}
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == BlockModItems.CHAO_CHANG_DE_JIAN.get()) {
            return true;
        } else {
            return false;
        }
    }

    @SubscribeEvent
    public static void onTickPlayerTick(LivingEvent.LivingUpdateEvent event) {
        //ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastMessage(new TextComponent("触发"), ChatType.SYSTEM, Util.NIL_UUID);
        //System.out.println("触发");
        if (event.getEntity() instanceof Player player) {
            //ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastMessage(new TextComponent("触发"), ChatType.SYSTEM, Util.NIL_UUID);
            //System.out.println("触发");

            if (check(player)) {
                //player.sendMessage(new TextComponent("触发"), player.getUUID());
                //System.out.println("触发");
                final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
                Level world = player.level;
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3), e -> true).stream()
                        .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
                for (Entity entityiterator : _entfound) {
                    if (entityiterator instanceof Projectile projectile) {
                        //player.sendMessage(new TextComponent("检测！"), player.getUUID());
                        if (world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2f), e -> true).contains(entityiterator)) {
                            entityiterator.discard();
                        } else {
                            projectile.setDeltaMovement(new Vec3(-(projectile.getDeltaMovement().x), -(projectile.getDeltaMovement().y), -(projectile.getDeltaMovement().z)));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onTickPlayerTick1(LivingEvent.LivingUpdateEvent event) {
        //ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastMessage(new TextComponent("触发"), ChatType.SYSTEM, Util.NIL_UUID);
        //System.out.println("触发");
        if (event.getEntity() instanceof Player player) {
            //ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastMessage(new TextComponent("触发"), ChatType.SYSTEM, Util.NIL_UUID);
            //System.out.println("触发");

            if (check(player)) {
                //player.sendMessage(new TextComponent("触发"), player.getUUID());
                //System.out.println("触发");
                final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
                Level world = player.level;
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4), e -> true).stream()
                        .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
                for (Entity entityiterator : _entfound) {
                    if (entityiterator instanceof Monster projectile) {
                        //player.sendMessage(new TextComponent("检测！"), player.getUUID());
                        if (world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3f), e -> true).contains(entityiterator)) {
                            //entityiterator.discard();
                        } else {
                            projectile.setDeltaMovement(new Vec3(-(projectile.getDeltaMovement().x + 0.7), -(projectile.getDeltaMovement().y), -(projectile.getDeltaMovement().z + 0.7)));
                        }

                    }
                    else if (entityiterator instanceof Mob projectile) {
                        if (!world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3f), e -> true).contains(entityiterator)) {
                            if (((Monster) entityiterator).getTarget() == player) {
                                projectile.setDeltaMovement(new Vec3(-(projectile.getDeltaMovement().x + 0.7), -(projectile.getDeltaMovement().y), -(projectile.getDeltaMovement().z + 0.7)));
                            }
                        }
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public static void onTickPlayerTick2(LivingEvent.LivingUpdateEvent event) {
        //if (event.getEntity() instanceof Player player) {
        Entity player=event.getEntity();
            final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
            Level world = player.level;
            List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4), e -> true).stream()
                    .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
            for (Entity entityiterator : _entfound) {
                if(entityiterator instanceof Player player1) {
                    if(emit(player1)!=null) {
                        if (emit(player1).getEntity() == player && player1 != player) {
                            Vec3 vec = new Vec3(player1.getLookAngle().x, player1.getLookAngle().y + 10, player1.getLookAngle().z);
                            player1.lookAt(EntityAnchorArgument.Anchor.EYES, vec);
                            //player1.setYHeadRot(player1.getYHeadRot()+10);

                        }
                    }
                }
            }
        //}
    }

    public static EntityHitResult emit(Player player) {
        Vec3 viewVec = player.getViewVector(1);
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(viewVec.x() * 150, viewVec.y() * 150, viewVec.z() * 150);
        return getEntityHitResult(player, start, end, new AABB(start, end), EntitySelector.ENTITY_STILL_ALIVE, 0f);
    }
}
