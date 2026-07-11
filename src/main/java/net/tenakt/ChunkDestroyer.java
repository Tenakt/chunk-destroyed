package net.tenakt;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.concurrent.atomic.AtomicInteger;

public class ChunkDestroyer implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("destroy")
                    .then(CommandManager.argument("blockname", StringArgumentType.greedyString())
                            .suggests((context, builder) -> {
                                String remaining = builder.getRemaining().toLowerCase();

                                for (Identifier id : Registries.BLOCK.getIds()) {
                                    String fullId = id.toString();

                                    if (fullId.contains(remaining)) {
                                        builder.suggest(fullId);
                                    }
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;

                                String text = StringArgumentType.getString(context, "blockname").trim().toLowerCase();
                                if (!text.contains(":")) {
                                    text = "minecraft:" + text;
                                }

                                Identifier id = Identifier.tryParse(text);

                                if (id != null && Registries.BLOCK.containsId(id)) {
                                    Block targetBlock = Registries.BLOCK.get(id);

                                    if (targetBlock == Blocks.AIR) {
                                        context.getSource().sendError(Text.translatable("command.chunkdestroyer.error.air"));
                                        return 0;
                                    }

                                    ServerWorld world = context.getSource().getWorld();

                                    BlockPos playerPos = player.getBlockPos();

                                    int radius = MyModInitializer.CONFIG.destroyRadius();

                                    int halfRadius = radius / 2;

                                    int minX = playerPos.getX() - halfRadius;
                                    int maxX = playerPos.getX() + (radius - halfRadius - 1);

                                    int minZ = playerPos.getZ() - halfRadius;
                                    int maxZ = playerPos.getZ() + (radius - halfRadius - 1);

                                    int heightUp = MyModInitializer.CONFIG.HeightUp();
                                    int heightDown = MyModInitializer.CONFIG.HeightDown();

                                    int minY = playerPos.getY() - heightDown;
                                    int maxY = playerPos.getY() + heightUp;

                                    int worldMinY = world.getBottomY();
                                    int worldMaxY = world.getBottomY() + world.getHeight() - 1;

                                    if (minY < worldMinY) minY = worldMinY;
                                    if (maxY > worldMaxY) maxY = worldMaxY;

                                    AtomicInteger removedCount = new AtomicInteger(0);
                                    BlockPos.Mutable mutablePos = new BlockPos.Mutable();

                                    for (int x = minX; x <= maxX; x++) {
                                        for (int z = minZ; z <= maxZ; z++) {
                                            for (int y = maxY; y >= minY; y--) {
                                                mutablePos.set(x, y, z);

                                                if (world.getBlockState(mutablePos).isOf(targetBlock)) {
                                                    world.setBlockState(mutablePos, Blocks.AIR.getDefaultState());
                                                    removedCount.incrementAndGet();
                                                }
                                            }
                                        }
                                    }

                                    context.getSource().sendFeedback(() -> Text.translatable("command.chunkdestroyer.success", removedCount.get()), false);
                                    return 1;
                                } else {
                                    context.getSource().sendError(Text.translatable("command.chunkdestroyer.error.not_found", text));
                                    return 0;
                                }
                            })));
        });
    }
}