package com.lilypuree.spawnermod.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerAggroTimerProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IPlayerAggroTimer.class)
    public static final Capability<IPlayerAggroTimer> AGGRO_TIMER_CAP = null;

    private LazyOptional<IPlayerAggroTimer> instance = LazyOptional.of(AGGRO_TIMER_CAP::getDefaultInstance);


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AGGRO_TIMER_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return AGGRO_TIMER_CAP.getStorage().writeNBT(AGGRO_TIMER_CAP, this.instance.orElseThrow(()->new IllegalArgumentException()), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        AGGRO_TIMER_CAP.getStorage().readNBT(AGGRO_TIMER_CAP,this.instance.orElseThrow(()->new IllegalArgumentException()), null,nbt);
    }

    public static class PlayerAggroTimerStorage implements Capability.IStorage<IPlayerAggroTimer> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IPlayerAggroTimer> capability, IPlayerAggroTimer instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            return IntNBT.valueOf(instance.getLastSpawnTick());
        }

        @Override
        public void readNBT(Capability<IPlayerAggroTimer> capability, IPlayerAggroTimer instance, Direction side, INBT nbt) {
            instance.setLastSpawnTick(((IntNBT)nbt).getInt());
        }
    }
}
