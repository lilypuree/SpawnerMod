package com.lilypuree.msms.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpawnLocationProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(ISpawnLocation.class)
    public static final Capability<ISpawnLocation> SPAWN_LOCATION_CAP = null;

    private LazyOptional<ISpawnLocation> instance = LazyOptional.of(SPAWN_LOCATION_CAP::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == SPAWN_LOCATION_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return SPAWN_LOCATION_CAP.getStorage().writeNBT(SPAWN_LOCATION_CAP, this.instance.orElseThrow(()->new IllegalArgumentException()), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        SPAWN_LOCATION_CAP.getStorage().readNBT(SPAWN_LOCATION_CAP,this.instance.orElseThrow(()->new IllegalArgumentException()), null,nbt);
    }

    public static class SpawnLocationStorage implements Capability.IStorage<ISpawnLocation> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<ISpawnLocation> capability, ISpawnLocation instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            return NBTUtil.writeBlockPos(instance.getSpawnPos());
        }

        @Override
        public void readNBT(Capability<ISpawnLocation> capability, ISpawnLocation instance, Direction side, INBT nbt) {
            instance.setSpawnPos(NBTUtil.readBlockPos((CompoundNBT) nbt));
        }
    }
}