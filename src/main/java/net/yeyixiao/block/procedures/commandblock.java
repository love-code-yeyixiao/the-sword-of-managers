package net.yeyixiao.block.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.CommandEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;

//import net.mcreator.test.init.TestModItems;

import javax.annotation.Nullable;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import net.yeyixiao.block.init.BlockModItems;

@Mod.EventBusSubscriber
public class commandblock {
    /*@SubscribeEvent
    public static void onCommand(CommandEvent event) {
        Entity entity = event.getParseResults().getContext().getSource().getEntity();
        if (entity != null) {
            execute(event, event.getParseResults().getContext().build(event.getParseResults().getReader().getString()), entity,
                    event.getParseResults().getReader().getString());
        }
    }*/

    public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity, String command) {
        execute(null, arguments, entity, command);
    }

    private static void execute(@Nullable Event event, CommandContext<CommandSourceStack> arguments, Entity entity, String command) {
        if (entity == null || command == null)
            return;
        if ((command).contains("kill") || (command).contains("kick") || (command).contains("ban") || (command).contains("banip")) {
            if (((new Object() {
                public Entity getEntity() {
                    try {
                        return EntityArgument.getEntity(arguments, "name");
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }.getEntity()) instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == BlockModItems.CHAO_CHANG_DE_JIAN.get()) {
                if (entity instanceof Player _player && !_player.level.isClientSide())
                    _player.displayClientMessage(
                            new TextComponent(
                                    "\u60F3\u7528\u7BA1\u7406\u8005\u7279\u6743\u5BF9\u4ED8\u7BA1\u7406\u8005\uFF1F\u505A\u68A6\u53BB\u5427\uFF01"),
                            (false));
                if (event != null && event.isCancelable()) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
