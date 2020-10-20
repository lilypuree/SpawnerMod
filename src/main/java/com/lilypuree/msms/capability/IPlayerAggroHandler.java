package com.lilypuree.msms.capability;

public interface IPlayerAggroHandler {

    int getNoise();

    void setNoise(int noise);

    default void addNoise(int noise){
        setNoise(this.getNoise() + noise);
    }

    void tick();

    long getLastSpawnTick();

    void setLastSpawnTick(long lastSpawnTick);

}