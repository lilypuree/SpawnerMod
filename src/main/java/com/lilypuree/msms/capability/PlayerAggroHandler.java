package com.lilypuree.msms.capability;

public class PlayerAggroHandler implements IPlayerAggroHandler {

    private long lastSpawnTick;

    private int noise;

    public PlayerAggroHandler() {
        lastSpawnTick = 0;
    }

    @Override
    public long getLastSpawnTick() {
        return lastSpawnTick;
    }

    @Override
    public void setLastSpawnTick(long lastSpawnTick) {
        this.lastSpawnTick = lastSpawnTick;
    }

    @Override
    public int getNoise() {
        return noise;
    }

    @Override
    public void setNoise(int noise) {
        this.noise = Math.max(noise, 200);
    }

    @Override
    public void tick() {
        if (noise > 0) {
            this.noise = Math.max(this.noise - 1, 0);
        }

    }
}