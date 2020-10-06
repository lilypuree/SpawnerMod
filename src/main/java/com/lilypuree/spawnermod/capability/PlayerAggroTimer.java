package com.lilypuree.spawnermod.capability;

public class PlayerAggroTimer implements IPlayerAggroTimer {

    private int lastSpawnTick;

    @Override
    public int getLastSpawnTick() {
        return lastSpawnTick;
    }

    @Override
    public void setLastSpawnTick(int lastSpawnTick) {
        this.lastSpawnTick = lastSpawnTick;
    }


}
