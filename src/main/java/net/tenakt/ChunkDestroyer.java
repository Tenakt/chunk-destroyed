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
                                        // Используем ключ перевода для воздуха
                                        context.getSource().sendError(Text.translatable("command.chunkdestroyer.error.air"));
                                        return 0;
                                    }

                                    ServerWorld world = context.getSource().getWorld();

                                    ChunkPos chunkPos = new ChunkPos(player.getBlockPos());

                                    int minX = chunkPos.getStartX();
                                    int maxX = chunkPos.getEndX();
                                    int minZ = chunkPos.getStartZ();
                                    int maxZ = chunkPos.getEndZ();

                                    int bottomWorldY = world.getBottomY();
                                    int startY = bottomWorldY + world.getHeight() - 1;

                                    AtomicInteger removedCount = new AtomicInteger(0);

                                    BlockPos.Mutable mutablePos = new BlockPos.Mutable();

                                    for (int x = minX; x <= maxX; x++) {
                                        for (int z = minZ; z <= maxZ; z++) {
                                            for (int y = startY; y >= bottomWorldY; y--) {
                                                mutablePos.set(x, y, z);

                                                if (world.getBlockState(mutablePos).isOf(targetBlock)) {
                                                    world.setBlockState(mutablePos, Blocks.AIR.getDefaultState());
                                                    removedCount.incrementAndGet();
                                                }
                                            }
                                        }
                                    }

                                    // Передаем количество удаленных блоков в качестве аргумента %d
                                    context.getSource().sendFeedback(() -> Text.translatable("command.chunkdestroyer.success", removedCount.get()), false);
                                    return 1;
                                } else {
                                    // Передаем ненайденный ID в качестве аргумента %s
                                    context.getSource().sendError(Text.translatable("command.chunkdestroyer.error.not_found", text));
                                    return 0;
                                }
                            })));
        });
    }
}