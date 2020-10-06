package com.lilypuree.spawnermod.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChunkSpawnPointProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IChunkSpawnPoints.class)
    public static final Capability<IChunkSpawnPoints> CHUNK_SPAWN_POINTS = null;

    private LazyOptional<IChunkSpawnPoints> instance = LazyOptional.of(CHUNK_SPAWN_POINTS::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CHUNK_SPAWN_POINTS ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return CHUNK_SPAWN_POINTS.getStorage().writeNBT(CHUNK_SPAWN_POINTS, this.instance.orElseThrow(() -> new IllegalArgumentException()), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CHUNK_SPAWN_POINTS.getStorage().readNBT(CHUNK_SPAWN_POINTS, this.instance.orElseThrow(() -> new IllegalArgumentException()), null, nbt);
    }

    public static class ChunkSpawnPointStorage implements Capability.IStorage<IChunkSpawnPoints> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IChunkSpawnPoints> capability, IChunkSpawnPoints instance, Direction side) {
            ListNBT nbt = new ListNBT();
            for (BlockPos pos : instance.getSpawnPoints()) {
                nbt.add(NBTUtil.writeBlockPos(pos));
            }
            return nbt;
        }

        @Override
        public void readNBT(Capability<IChunkSpawnPoints> capability, IChunkSpawnPoints instance, Direction side, INBT nbt) {
            ListNBT listNBT = (ListNBT) nbt;
            instance.reset();
            listNBT.forEach(inbt -> {
                instance.addSpawnPoint(NBTUtil.readBlockPos((CompoundNBT) inbt));
            });
        }
    }
}
