package com.lilypuree.msms.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerAggroHandlerProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IPlayerAggroHandler.class)
    public static final Capability<IPlayerAggroHandler> AGGRO_HANDLER_CAP = null;

    private LazyOptional<IPlayerAggroHandler> instance = LazyOptional.of(AGGRO_HANDLER_CAP::getDefaultInstance);


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AGGRO_HANDLER_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return AGGRO_HANDLER_CAP.getStorage().writeNBT(AGGRO_HANDLER_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException()), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        AGGRO_HANDLER_CAP.getStorage().readNBT(AGGRO_HANDLER_CAP, this.instance.orElseThrow(() -> new IllegalArgumentException()), null, nbt);
    }

    public static class PlayerAggroTimerStorage implements Capability.IStorage<IPlayerAggroHandler> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IPlayerAggroHandler> capability, IPlayerAggroHandler instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putLong("lastSpawnTick", instance.getLastSpawnTick());
            nbt.putInt("noise", instance.getNoise());
            return nbt;
        }

        @Override
        public void readNBT(Capability<IPlayerAggroHandler> capability, IPlayerAggroHandler instance, Direction side, INBT nbt) {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            instance.setLastSpawnTick(compoundNBT.getLong("lastSpawnTick"));
            instance.setNoise(compoundNBT.getInt("noise"));
        }
    }
}