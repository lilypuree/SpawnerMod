package com.lilypuree.msms.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

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
            CompoundNBT nbt = new CompoundNBT();
            ListNBT listNBT = new ListNBT();
            Set<BlockPos> list = instance.getSpawnPoints();
            if (list != null) {
                for (BlockPos pos : instance.getSpawnPoints()) {
                    listNBT.add(NBTUtil.writeBlockPos(pos));
                }
            }
            nbt.put("list", listNBT);
            return nbt;
        }

        @Override
        public void readNBT(Capability<IChunkSpawnPoints> capability, IChunkSpawnPoints instance, Direction side, INBT nbt) {
            CompoundNBT compound = (CompoundNBT) nbt;
            ListNBT listNBT = ((CompoundNBT) nbt).getList("list", Constants.NBT.TAG_COMPOUND);
            instance.reset();
            listNBT.forEach(inbt -> {
                instance.addSpawnPoint(NBTUtil.readBlockPos((CompoundNBT) inbt));
            });
        }
    }
}