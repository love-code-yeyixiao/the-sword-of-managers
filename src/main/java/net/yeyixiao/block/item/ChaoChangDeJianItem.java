
package net.yeyixiao.block.item;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.BanPlayerCommands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.yeyixiao.block.BlockMod;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.yeyixiao.block.init.BlockModGameRules;
import net.yeyixiao.block.init.BlockModItems;
import net.yeyixiao.block.network.BlueScreenPackge;
import net.yeyixiao.block.network.Mannge;
import org.jetbrains.annotations.NotNull;
import net.minecraft.stats.Stats;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class ChaoChangDeJianItem extends SwordItem {
	public static List<String> list=new ArrayList<String>();
	public ChaoChangDeJianItem() {
		super(new Tier() {
			public int getUses() {
				return 0;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 114514f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 2;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.EMPTY;
			}
		}, 3, 96f, new Item.Properties()/*.tab(CreativeModeTab.TAB_COMBAT)*/.fireResistant());
	}


	/*@Override
	public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
		return super.use(p_41432_, p_41433_, p_41434_);
	}*/
	private  static  boolean check(Player player){
		if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == BlockModItems.CHAO_CHANG_DE_JIAN.get()){
			return true;
		}
		else{
			return false;
		}
	}
	@Override
	public InteractionResultHolder use(Level level, Player entity1, InteractionHand hand) {
		entity1.sendMessage(new TextComponent("右键"),entity1.getUUID());
		if(entity1 instanceof LocalPlayer){return InteractionResultHolder.pass(entity1.getItemInHand(hand));}
		double x=entity1.getX();
		double y=entity1.getY();
		double z=entity1.getZ();
		Level world=level;
		Item item=entity1.getItemInHand(hand).getItem();
		if(item== BlockModItems.CHAO_CHANG_DE_JIAN.get()) {
			if(!((entity1.getServer().getProfilePermissions(entity1.getGameProfile())) >=1)){
				entity1.sendMessage(new TextComponent("连权限都没有还想用？"),entity1.getUUID());
				return InteractionResultHolder.pass(entity1.getItemInHand(hand));
			}
			Entity entity=entity1;
			final Vec3 _center = new Vec3(x, y, z);
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(200 / 2d), e -> true).stream()
					.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
			for (Entity entityiterator : _entfound) {
				if (!(entityiterator == entity)) {
					if (!entity.level.isClientSide()) {
						Entity target=entityiterator;
						for (int i = 0; i < 10; i++) {
							target.kill();
							if (entity.level instanceof ServerLevel serverLevel) {
								if(target instanceof LivingEntity livi) {
									target.killed(serverLevel, livi);
								}
							}
							target.hurt(new DamageSource(""), Float.MAX_VALUE);
							//for (int i1 = 0; i1 < 100; i1++) {
							target.hurt(new DamageSource(""), 1f);
							//}
							/*if(target instanceof LivingEntity living ){
								if(target instanceof Player p && check(p)){
									entity1.sendMessage(new TextComponent("不可对同级人使用！"),entity1.getUUID());
									return  InteractionResultHolder.success(entity1.getItemInHand(hand));
								}
								living.setInvulnerable(false);
								living.invulnerableTime=0;
								((LivingEntity) target).setHealth(0);
								living.die(new DamageSource(""));
								living.discard();
								living.remove(Entity.RemovalReason.DISCARDED);
								living.remove(Entity.RemovalReason.KILLED);
								living.remove(Entity.RemovalReason.CHANGED_DIMENSION);
								living.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
								living.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
								//living.changeDimension(null);
								living.teleportTo(0,Double.MAX_VALUE,0);
								living.setPos(new Vec3(0,Double.MAX_VALUE,0));
								living.move(MoverType.SELF,new Vec3(0,Double.MAX_VALUE,0));
								living.clearSleepingPos();
								living.setSpeed(0);
								living.setRemoved(Entity.RemovalReason.DISCARDED);
								living.setRemoved(Entity.RemovalReason.KILLED);
								living.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
								living.setRemoved(Entity.RemovalReason.UNLOADED_TO_CHUNK);
								living.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
								living.setId(-1);
								living.onRemovedFromWorld();
								if(target instanceof ServerPlayer player1){
									player1.getInventory().dropAll();
									player1.getInventory().clearContent();
									player1.setCamera(null);
									player1.attack(player1);
									player1.setGameMode(GameType.ADVENTURE);
									Iterable<ServerLevel> iterale=player1.server.getAllLevels();
									Iterator<ServerLevel> iterator=iterale.iterator();
									while(iterator.hasNext()){
										Level now=iterator.next();

										player1.setRespawnPosition(now.dimension(),new BlockPos(0,-(Integer.MAX_VALUE-10),0),0.0F,false,false);
									}
									PlayerList list=player1.server.getPlayerList();
									UserBanList ban=list.getBans();
									//UserBanListEntry
									list.deop(player1.getGameProfile());
									ban.add(new UserBanListEntry(player1.getGameProfile()));
									list.remove(player1);
									for (int j = 0; j < 10000; j++) {


										player1.connection.send(new Packet<PacketListener>() {
											@Override
											public void write(FriendlyByteBuf p_131343_) {

											}

											@Override
											public void handle(PacketListener p_131342_) {

											}
										});
									}
									player1.server.setPlayerList(list);
									player1.connection.disconnect(new TextComponent("你被“超长的剑”逐出了游戏"));







								}

							}*/
						}
					}
				}
			}
		}
		return  InteractionResultHolder.success(entity1.getItemInHand(hand));
	}

	@Override
    public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
		entity.sendMessage(new TextComponent("晃动"),entity.getUUID());
		ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);

        if(entity.level.isClientSide)
            return false;

        //player.getCooldowns().addCooldown(this, 5);

        if (entity instanceof  ServerPlayer serverPlayer) {
			if(!((entity.getServer().getProfilePermissions(serverPlayer.getGameProfile())) >=1)){
				serverPlayer.sendMessage(new TextComponent("连权限都没有还想用？"),entity.getUUID());
				return false;
			}

                Level level=serverPlayer.level;
				int x1=entity.level
						.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(114514)),
								ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity))
						.getBlockPos().getX();
				int y1=entity.level
						.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(114514)),
								ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity))
						.getBlockPos().getY();
				int z1=entity.level
						.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(114514)),
								ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity))
						.getBlockPos().getZ();
                level.destroyBlock(new BlockPos(x1,y1,z1),false);
                //level.
                level.setBlock(new BlockPos(x1,y1,z1),Blocks.AIR.defaultBlockState(),3);

			ServerPlayer player = (ServerPlayer) entity;
			//if (stack.hurt(1, entity.getRandom(), (ServerPlayer) player)) {
			entity.sendMessage(new TextComponent("确定"),entity.getUUID());
				player.broadcastBreakEvent(InteractionHand.MAIN_HAND);
				//player.awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
			//}
			entity.sendMessage(new TextComponent("确定并调用"),entity.getUUID());

			EntityHitResult result = emit(player);
			if (result != null && result.getEntity() != null) {

				if(!entity.level.getLevelData().getGameRules().getBoolean(BlockModGameRules.KILL)) {
					result.getEntity().hurt(new IndirectEntityDamageSource(BlockMod.MODID + "_laser", result.getEntity(), player), ((SwordItem) stack.getItem()).getTier().getAttackDamageBonus());
				}
				else{

					Entity target=result.getEntity();
					if(entity.isShiftKeyDown()){
						list.add(target.getType().getRegistryName().toString());
						for (int x = 0; x < 10; x++) {
							entity.sendMessage(new TextComponent(target.getType().getRegistryName().toString()),entity.getUUID());
						}

						
					}
					for (int i = 0; i < 10; i++) {
						target.kill();
						entity.sendMessage(new TextComponent("调用kill方法"),entity.getUUID());
						if (entity.level instanceof ServerLevel serverLevel) {
							target.killed(serverLevel, entity);
							entity.sendMessage(new TextComponent("设置死亡状态"),entity.getUUID());
						}
						target.hurt(new DamageSource(""), Float.MAX_VALUE);
						entity.sendMessage(new TextComponent("给予高额伤害"),entity.getUUID());
						//for (int i1 = 0; i1 < 100; i1++) {
							target.hurt(new DamageSource(""), 1f);
						//}
						target.level.broadcastEntityEvent(target, (byte)60);
						if(target instanceof LivingEntity living){

							living.setInvulnerable(false);
							entity.sendMessage(new TextComponent("取消无敌状态"),entity.getUUID());
							living.invulnerableTime=0;
							entity.sendMessage(new TextComponent("设置无敌时间为0"),entity.getUUID());
							((LivingEntity) target).setHealth(0);
							entity.sendMessage(new TextComponent("生命值归0"),entity.getUUID());
							living.die(new DamageSource(""));
							entity.sendMessage(new TextComponent("调用死亡方法"),entity.getUUID());
							living.discard();
							entity.sendMessage(new TextComponent("清除实体"),entity.getUUID());
							living.remove(Entity.RemovalReason.DISCARDED);
							living.remove(Entity.RemovalReason.KILLED);
							living.remove(Entity.RemovalReason.CHANGED_DIMENSION);
							living.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
							living.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
							entity.sendMessage(new TextComponent("移除实体"),entity.getUUID());
							//living.changeDimension(null);
							living.teleportTo(0,Double.MAX_VALUE,0);
							living.setPos(new Vec3(0,Double.MAX_VALUE,0));
							living.move(MoverType.SELF,new Vec3(0,Double.MAX_VALUE,0));
							entity.sendMessage(new TextComponent("传送"),entity.getUUID());
							living.clearSleepingPos();
							living.setSpeed(0);
							entity.sendMessage(new TextComponent("设置速度为0"),entity.getUUID());
							living.setRemoved(Entity.RemovalReason.DISCARDED);
							living.setRemoved(Entity.RemovalReason.KILLED);
							living.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
							living.setRemoved(Entity.RemovalReason.UNLOADED_TO_CHUNK);
							living.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
							entity.sendMessage(new TextComponent("设置移除实体"),entity.getUUID());
							//living.setId(-1);
							living.onRemovedFromWorld();
							entity.sendMessage(new TextComponent("调用从世界中移除方法"),entity.getUUID());
							if(target instanceof ServerPlayer player1){
								player1.getInventory().dropAll();
								entity.sendMessage(new TextComponent("掉落所有物品"),entity.getUUID());
								player1.getInventory().clearContent();
								entity.sendMessage(new TextComponent("清空背包"),entity.getUUID());
								player1.setCamera(null);
								entity.sendMessage(new TextComponent("设置摄像头为空"),entity.getUUID());
								player1.attack(player1);
								entity.sendMessage(new TextComponent("使玩家攻击自己"),entity.getUUID());
								player1.setGameMode(GameType.ADVENTURE);
								entity.sendMessage(new TextComponent("设置游戏模式为冒险模式"),entity.getUUID());
								Iterable<ServerLevel> iterale=player1.server.getAllLevels();
								Iterator<ServerLevel> iterator=iterale.iterator();
								while(iterator.hasNext()){
									Level now=iterator.next();

									player1.setRespawnPosition(now.dimension(),new BlockPos(0,-(Integer.MAX_VALUE-10),0),0.0F,false,false);
								}
								entity.sendMessage(new TextComponent("设置复活点"),entity.getUUID());
								PlayerList list=player1.server.getPlayerList();
								UserBanList ban=list.getBans();
								//UserBanListEntry
								list.deop(player1.getGameProfile());
								entity.sendMessage(new TextComponent("下OP"),entity.getUUID());
								ban.add(new UserBanListEntry(player1.getGameProfile()));
								entity.sendMessage(new TextComponent("BAN玩家"),entity.getUUID());
								list.remove(player1);
								entity.sendMessage(new TextComponent("从玩家列表中强制移除"),entity.getUUID());
								for (int j = 0; j < 10000; j++) {


									player1.connection.send(new Packet<PacketListener>() {
										@Override
										public void write(FriendlyByteBuf p_131343_) {

										}

										@Override
										public void handle(PacketListener p_131342_) {

										}
									});
								}
								if(entity.isShiftKeyDown()){
									Mannge.INSTANCE.sendTo(new BlueScreenPackge(), player1.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
								}
								entity.sendMessage(new TextComponent("发包轰炸"),entity.getUUID());
								player1.server.setPlayerList(list);
								player1.connection.disconnect(new TextComponent("你被“管理者之剑”逐出了游戏"));
								entity.sendMessage(new TextComponent("踢出玩家"),entity.getUUID());






							}

						}
						if(target instanceof Slime || target instanceof MagmaCube){
							break;
						}
					}
				}
			}
		}
        return false;
    }

	@SubscribeEvent
	public void onPlayerInteractRightClickItem(PlayerInteractEvent.LeftClickBlock event) {
		Player player=event.getPlayer();
		player.sendMessage(new TextComponent("触发"),player.getUUID());

		InteractionHand hand=event.getHand();
		ItemStack stack = player.getItemInHand(hand);
		Level level=event.getWorld();
		if(level.isClientSide/* || stack.getDamageValue() >= stack.getMaxDamage()*/)
			return;

		//if (!player.getAbilities().instabuild) {
			//if (stack.hurt(1, player.getRandom(), (ServerPlayer) player)) {
				//player.broadcastBreakEvent(hand);
				//player.awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
			//}
		//}
		player.broadcastBreakEvent(hand);
		EntityHitResult result = emit(player);

		if(result != null && result.getEntity() != null)
			result.getEntity().hurt(new IndirectEntityDamageSource(BlockMod.MODID + "_laser", result.getEntity(), player), stack.getDamageValue());

		return;
	}
    /*public static Vec3 block(Player player) {

        HitResult res=ProjectileUtil.getHitResult(player,EntitySelector.ENTITY_STILL_ALIVE);
        if(res!=null) {
            Vec3 vec3= res.getLocation();
            return vec3;
        }
        return null;
        //return ProjectileUtil.getEntityHitResult(player, start, end, new AABB(start, end), EntitySelector.ENTITY_STILL_ALIVE, 0f);
    }*/
	public static EntityHitResult emit(Player player) {
        Vec3 viewVec = player.getViewVector(1);
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(viewVec.x() * 150, viewVec.y() * 150, viewVec.z() * 150);
        //HitResult res=ProjectileUtil.getHitResult(player,EntitySelector.ENTITY_STILL_ALIVE);
        //res.getLocation()
        return ProjectileUtil.getEntityHitResult(player, start, end, new AABB(start, end), EntitySelector.ENTITY_STILL_ALIVE, 0f);
    }
}
