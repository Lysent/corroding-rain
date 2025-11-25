package net.reversteam.weathering.system;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reversteam.weathering.WeatheringMod;
import net.reversteam.weathering.registry.ModBlocks;

@Mod.EventBusSubscriber(modid = WeatheringMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RustingSystem {
    private static final TagKey<Block> MACHINES = TagKey.create(Registries.BLOCK, new ResourceLocation(WeatheringMod.MOD_ID, "machines"));
    private static final TagKey<Block> LIGHT_MACHINES = TagKey.create(Registries.BLOCK, new ResourceLocation(WeatheringMod.MOD_ID, "machine_tier/light"));
    private static final TagKey<Block> HEAVY_MACHINES = TagKey.create(Registries.BLOCK, new ResourceLocation(WeatheringMod.MOD_ID, "machine_tier/heavy"));

    // this is set to be lightweight, do not change it please, or else it will cause un needed problems :s
    private static final int CHECKS_PER_PLAYER = 6;
    private static final float BASE_RUST_CHANCE = 0.015F;
    private static final float RAIN_RUST_BONUS = 0.05F;

    private RustingSystem() {
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide()) {
            return;
        }

        ServerLevel level = (ServerLevel) event.level;
        for (ServerPlayer player : level.players()) {
            runExposureChecks(level, player);
        }
    }

    private static void runExposureChecks(ServerLevel level, ServerPlayer player) {
        RandomSource random = level.getRandom();
        BlockPos origin = player.blockPosition();

        for (int i = 0; i < CHECKS_PER_PLAYER; i++) {
            BlockPos target = sampleNearby(origin, random);
            BlockState state = level.getBlockState(target);
            if (!state.is(MACHINES)) {
                continue;
            }

            if (!isExposed(level, target)) {
                continue;
            }

            float chance = BASE_RUST_CHANCE;
            if (level.isRainingAt(target.above())) {
                chance += RAIN_RUST_BONUS;
            } else {
                int skylight = level.getBrightness(LightLayer.SKY, target);
                chance += Mth.clamp((skylight - 10) * 0.01F, 0.0F, 0.07F);
            }

            if (random.nextFloat() < chance) {
                rustBlock(level, target, state, random);
            } else if (random.nextInt(12) == 0) {
                bleedParticles(level, target, random);
            }
        }
    }

    private static BlockPos sampleNearby(BlockPos origin, RandomSource random) {
        int dx = random.nextIntBetweenInclusive(-10, 10);
        int dy = random.nextIntBetweenInclusive(-2, 3);
        int dz = random.nextIntBetweenInclusive(-10, 10);
        return origin.offset(dx, dy, dz);
    }

    private static boolean isExposed(ServerLevel level, BlockPos pos) {
        BlockPos above = pos.above();
        return level.canSeeSky(above) && level.getBlockState(above).isAir();
    }

    private static void rustBlock(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        BlockState rustedState = ModBlocks.RUSTED_MACHINE.get().defaultBlockState();
        if (state.is(HEAVY_MACHINES)) {
            rustedState = ModBlocks.RUSTED_MACHINE_HEAVY.get().defaultBlockState();
        } else if (state.is(LIGHT_MACHINES)) {
            rustedState = ModBlocks.RUSTED_MACHINE_LIGHT.get().defaultBlockState();
        }

        level.setBlockAndUpdate(pos, rustedState);
        level.levelEvent(null, 2001, pos, Block.getId(state));
        bleedParticles(level, pos, random);
    }

    private static void bleedParticles(ServerLevel level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 6; i++) {
            double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
            double y = pos.getY() + 0.2 + random.nextDouble() * 0.6;
            double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
            double speedX = (random.nextDouble() - 0.5) * 0.03;
            double speedY = random.nextDouble() * 0.02;
            double speedZ = (random.nextDouble() - 0.5) * 0.03;
            level.sendParticles(ParticleTypes.ASH, x, y, z, 1, speedX, speedY, speedZ, 0.01);
        }

        if (random.nextBoolean()) {
            Direction face = Direction.getRandom(random);
            BlockPos facePos = pos.relative(face);
            if (level.isEmptyBlock(facePos)) {
                double x = pos.getX() + 0.5 + face.getStepX() * 0.52;
                double y = pos.getY() + 0.5 + face.getStepY() * 0.52;
                double z = pos.getZ() + 0.5 + face.getStepZ() * 0.52;
                level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 2, 0.02, 0.01, 0.02, 0.0);
            }
        }
    }
}
