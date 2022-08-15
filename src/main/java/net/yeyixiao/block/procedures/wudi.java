package net.yeyixiao.block.procedures;

import com.google.common.eventbus.DeadEvent;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.yeyixiao.block.init.BlockModItems;
import net.yeyixiao.block.item.ChaoChangDeJianItem;
import org.lwjgl.glfw.GLFW;


import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static net.minecraftforge.client.gui.ForgeIngameGui.PLAYER_HEALTH_ELEMENT;


@Mod.EventBusSubscriber
public class wudi {
    public static Map<Player, Vec3> map = new HashMap<Player, Vec3>();

    private static boolean check(Player player) {
        //if(player.isLocalPlayer()){return false;}
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == BlockModItems.CHAO_CHANG_DE_JIAN.get()) {
            return true;
        } else {
            return false;
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.player.isLocalPlayer()) {
            return;
        }
        if (check(event.player)) {
            if (event.player instanceof ServerPlayer serverPlayer) {
                PlayerList list = serverPlayer.server.getPlayerList();
                if (!list.isOp(serverPlayer.getGameProfile())) {
                    serverPlayer.getInventory().clearOrCountMatchingItems(p -> BlockModItems.CHAO_CHANG_DE_JIAN.get() == p.getItem(), 999999999,
                            serverPlayer.inventoryMenu.getCraftSlots());

                }
            }
            event.player.setInvulnerable(true);
            Player player = event.player;
            player.setHealth(player.getMaxHealth());
            player.getAbilities().invulnerable = true;
            player.getAbilities().mayBuild = true;
            player.getAbilities().instabuild = true;
            player.heal(Float.MAX_VALUE);
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
            player.dead = false;
            player.setInvisible(true);
            player.setCustomNameVisible(false);

            //Minecraft.getInstance().options.hideGui=true;
            //PLAYER_HEALTH_ELEMENT
        }

    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (check(player)) {
                event.setCanceled(true);
                map.put(player, new Vec3(player.getX(), player.getY(), player.getZ()));
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (check(player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (check(player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (check(player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onKnockBack(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (check(player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingSetAttackTargetEvent event) {
        if (event.getTarget() instanceof Player player) {
            if (check(player)) {
                //event.setCanceled(true);
                if (event.getEntityLiving() instanceof Mob) {
                    Mob liv = (Mob) event.getEntityLiving();
                    liv.setTarget(liv);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (check(player)) {
                event.setCanceled(true);
            }
        }
    }



    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("clearban").requires(s -> s.hasPermission(1)).executes(arguments -> {
            ServerLevel world = arguments.getSource().getLevel();
            double x = arguments.getSource().getPosition().x();
            double y = arguments.getSource().getPosition().y();
            double z = arguments.getSource().getPosition().z();
            Entity entity = arguments.getSource().getEntity();
            if (entity == null)
                entity = FakePlayerFactory.getMinecraft(world);
            Direction direction = entity.getDirection();
            ChaoChangDeJianItem.list.clear();
            return 0;
        }));

    }

    @SubscribeEvent
    public static void onSpawn(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            event.getEntity().sendMessage(new TextComponent("生成实体"), event.getEntity().getUUID());

            event.getEntity().sendMessage(new TextComponent(event.getEntity().getType().getRegistryName().toString()), event.getEntity().getUUID());
            List<String> list = ChaoChangDeJianItem.list;
            for (String type : list) {
                if (type.equalsIgnoreCase(event.getEntity().getType().getRegistryName().toString()) || type == event.getEntity().getType().getRegistryName().toString()) {
                    event.getEntity().sendMessage(new TextComponent("禁止生成"), event.getEntity().getUUID());
                    event.setCanceled(true);
                    break;
                }
            }

        }
    }

    @SubscribeEvent
    public static void onDeadScrren(ScreenEvent.InitScreenEvent.Pre event) {
        if (event.getScreen() instanceof DeathScreen screen) {
            if (check(screen.getMinecraft().player)) {
                event.setCanceled(true);
                screen.getMinecraft().player.respawn();
                screen.getMinecraft().setScreen((Screen) null);
            }
        }
    }

    @SubscribeEvent
    public static void onReSpawn(PlayerEvent.PlayerRespawnEvent event) {
        if (map.containsKey(event.getPlayer())) {
            Vec3 pos = map.get(event.getPlayer());
            event.getPlayer().setPos(pos);
        }

    }

    @SubscribeEvent
    public static void onToss(ItemTossEvent event) {
        if (event.getEntityItem().getItem().getItem() instanceof ChaoChangDeJianItem) {
            event.setCanceled(true);
            event.getPlayer().getInventory().add(new ItemStack(BlockModItems.CHAO_CHANG_DE_JIAN.get()));
        }
    }

    //@SubscribeEvent
    //public static void onWaste(InputEvent.)
    @SubscribeEvent
    public static void onItem(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ItemEntity itemEntity) {
            if (itemEntity.getItem().getItem() instanceof ChaoChangDeJianItem) {
                itemEntity.discard();
            }
        }
    }
    @SubscribeEvent
    public static void onUse(LivingEntityUseItemEvent.Start event){
        if (event.getEntity() instanceof Player player) {
            //if (check(player)) {
            EntityHitResult result=emit(player);
            if(result.getEntity()!=null) {
                if(result.getEntity() instanceof Player player1) {
                    if (check(player1)) {
                        event.setCanceled(true);
                    }
                }
            }
            //}
        }
    }
    @SubscribeEvent
    public static void onUse(LivingEntityUseItemEvent.Tick event){
        if (event.getEntity() instanceof Player player) {
            //if (check(player)) {
            EntityHitResult result=emit(player);
            if(result.getEntity()!=null) {
                if(result.getEntity() instanceof Player player1) {
                    if (check(player1)) {
                        event.setCanceled(true);
                    }
                }
            }
            //}
        }
    }
    @SubscribeEvent
    public static void onUse(LivingEntityUseItemEvent.Stop event){
        if (event.getEntity() instanceof Player player) {
            //if (check(player)) {
            EntityHitResult result=emit(player);
            if(result.getEntity()!=null) {
                if(result.getEntity() instanceof Player player1) {
                    if (check(player1)) {
                        event.setCanceled(true);
                    }
                }
            }
            //}
        }
    }



    @SubscribeEvent
    public static void on(PlayerEvent.ItemPickupEvent event) {
        if (event.getStack().getItem() instanceof ChaoChangDeJianItem) {
            event.getStack().setCount(0);
        }
    }

    @SubscribeEvent
    public static void on(PlayerEvent.ItemCraftedEvent event) {
        if (event.getCrafting().getItem() instanceof ChaoChangDeJianItem) {
            event.getCrafting().setCount(0);
        }
    }

    @SubscribeEvent
    public static void warn(ScreenEvent.InitScreenEvent.Post event) {
        if (event.getScreen() instanceof TitleScreen screen) {

            screen.splash = "本模组中部分功能可能让你的设备蓝屏！";
            screen.COPYRIGHT_TEXT = new TextComponent("本模组中部分功能可能让你的设备蓝屏！Copyright Mojang AB. Do not distribute!");

            screen.getMinecraft().getWindow().setTitle("Minecraft-模组中部分功能可能让你的设备蓝屏！");
        }

    }

    @SubscribeEvent
    public static void onRight(PlayerInteractEvent.RightClickBlock event) {
        if (emit(event.getPlayer()) != null) {
            EntityHitResult result = emit(event.getPlayer());
            if (result.getEntity() instanceof Player player) {
                if (check(player)) {
                    event.setCanceled(true);
                }
            }
        }
    }


    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getMainHandItem().getItem() == BlockModItems.CHAO_CHANG_DE_JIAN.get() || player.getMainHandItem().getItem() == BlockModItems.CHAO_CHANG_DE_JIAN.get()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onRenderItemInFrame(RenderItemInFrameEvent event) {
        if (event.getItemStack().getItem() instanceof ChaoChangDeJianItem) {
            //if(player.getMainHandItem().getItem()==BlockModItems.CHAO_CHANG_DE_JIAN.get() || player.getMainHandItem().getItem()==BlockModItems.CHAO_CHANG_DE_JIAN.get()){
            //event.getItemFrameEntity()
            event.setCanceled(true);
            //}
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        if (event.getItemStack().getItem() instanceof ChaoChangDeJianItem) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRight(PlayerInteractEvent.RightClickItem event) {
        if (emit(event.getPlayer()) != null) {
            EntityHitResult result = emit(event.getPlayer());
            if (result.getEntity() instanceof Player player) {
                if (check(player)) {
                    event.setCanceled(true);
                }
            }
        }
    }//EntityInteractSpecific

    @SubscribeEvent
    public static void onRight(PlayerInteractEvent.EntityInteractSpecific event) {
        if (emit(event.getPlayer()) != null) {
            EntityHitResult result = emit(event.getPlayer());
            if (result.getEntity() instanceof Player player) {
                if (check(player)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClick(InputEvent.RawMouseEvent event) {
        //if (event.getButton() == 0 || event.getButton() == 1 ){
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.level != null) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (emit(player) != null) {
                EntityHitResult result = emit(player);
                if (result.getEntity() instanceof Player player1) {
                    if (player1.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ChaoChangDeJianItem || player1.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ChaoChangDeJianItem) {
                        if (result.getEntity() != null) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
        //}

    }

    @SubscribeEvent
    public static void onClick(InputEvent.ClickInputEvent event) {
        //if (event.isAttack() || event.isUseItem()){
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.level != null) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (emit(player) != null) {
                EntityHitResult result = emit(player);
                if (result.getEntity() instanceof Player player1) {
                    if (player1.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ChaoChangDeJianItem || player1.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ChaoChangDeJianItem) {
                        if (result.getEntity() != null) {
                            event.setCanceled(true);
                            event.setSwingHand(false);
                        }
                    }
                }
            }
        }
        //}

    }

    @Nullable
    public static EntityHitResult getEntityHitResult2(Entity p_37288_, Vec3 p_37289_, Vec3 p_37290_, AABB p_37291_, Predicate<Entity> p_37292_, double p_37293_) {
        Level level = p_37288_.level;
        double d0 = p_37293_;
        Entity entity = null;
        Vec3 vec3 = null;

        for (Entity entity1 : level.getEntities(p_37288_, p_37291_, p_37292_)) {
            AABB aabb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius() + 2d);
            Optional<Vec3> optional = aabb.clip(p_37289_, p_37290_);
            if (aabb.contains(p_37289_)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    vec3 = optional.orElse(p_37289_);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = p_37289_.distanceToSqr(vec31);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == p_37288_.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            vec3 = vec31;
                        }
                    } else {
                        entity = entity1;
                        vec3 = vec31;
                        d0 = d1;
                    }
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity, vec3);
    }

    public static EntityHitResult emit(Player player) {
        Vec3 viewVec = player.getViewVector(1);
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(viewVec.x() * 150, viewVec.y() * 150, viewVec.z() * 150);
        return getEntityHitResult2(player, start, end, new AABB(start, end), EntitySelector.ENTITY_STILL_ALIVE, 0f);
    }
}
