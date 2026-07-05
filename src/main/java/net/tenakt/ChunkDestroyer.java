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
                                        context.getSource().sendError(Text.literal("Нельзя удалить воздух!"));
                                        return 0;
                                    }

                                    ServerWorld world = player.getServerWorld();

                                    // Получаем текущий чанк
                                    ChunkPos chunkPos = new ChunkPos(player.getBlockPos());

                                    int minX = chunkPos.getStartX();
                                    int maxX = chunkPos.getEndX();
                                    int minZ = chunkPos.getStartZ();
                                    int maxZ = chunkPos.getEndZ();

                                    int startY = world.getTopY();
                                    int bottomWorldY = world.getBottomY();

                                    // Используем AtomicInteger вместо обычного int, чтобы Java не ругалась в лямбде
                                    AtomicInteger removedCount = new AtomicInteger(0);

                                    for (int x = minX; x <= maxX; x++) {
                                        for (int z = minZ; z <= maxZ; z++) {
                                            for (int y = startY; y >= bottomWorldY; y--) {
                                                BlockPos pos = new BlockPos(x, y, z);

                                                if (world.getBlockState(pos).isOf(targetBlock)) {
                                                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                                                    removedCount.incrementAndGet(); // Безопасно увеличиваем счётчик
                                                }
                                            }
                                        }
                                    }

                                    // Теперь здесь всё скомпилируется идеально!
                                    context.getSource().sendFeedback(() -> Text.literal("В текущем чанке удалено блоков: " + removedCount.get()), false);
                                    return 1;
                                } else {
                                    context.getSource().sendError(Text.literal("Блок с ID " + text + " не найден!"));
                                    return 0;
                                }
                            })));
        });
    }
}