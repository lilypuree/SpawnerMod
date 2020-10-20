package com.lilypuree.msms;

import com.google.gson.internal.$Gson$Preconditions;
import commoble.databuddy.config.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Map;

public class ServerConfigs {

    private ConfigHelper.ConfigValueListener<Integer> mobAwakenDistance;
    private ConfigHelper.ConfigValueListener<Integer> aggroInterval;
    private ConfigHelper.ConfigValueListener<Integer> boneDepositVerticalRange;
    private ConfigHelper.ConfigValueListener<Integer> boneDepositRadius;
    private ConfigHelper.ConfigValueListener<Boolean> removeSpiderSpawns;

    private ConfigHelper.ConfigValueListener<Integer> minSpidersFromEggSack;
    private ConfigHelper.ConfigValueListener<Integer> maxSpidersFromEggSack;
    private ConfigHelper.ConfigValueListener<Integer> eggSackClusterSize;
    private ConfigHelper.ConfigValueListener<Integer> eggSackCount;
    private ConfigHelper.ConfigValueListener<Integer> spiderNestsSpawnAttempts;

    public ServerConfigs(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {
        builder.push("General Category");
        this.mobAwakenDistance = subscriber.subscribe(builder
                .comment("Mob Awakening Distance")
                .translation("msms.mobawakendistance")
                .define("mobawakendistance", 2));

        this.aggroInterval = subscriber.subscribe(builder
                .comment("Aggro Interval")
                .translation("msms.aggro_interval")
                .define("aggro_interval", 10));
        builder.pop();

//        builder.push("Bone Deposit Configs");
//
//        this.boneDepositVerticalRange = subscriber.subscribe(builder
//                .comment("Bone Deposit Vertical Search Range")
//                .translation("msms.deposit_vertical_range")
//                .define("deposit_vertical_range", 4));
//
//        this.boneDepositRadius = subscriber.subscribe(builder
//                .comment("Bone Deposit Search Radius")
//                .translation("msms.deposit_radius")
//                .define("deposit_radius", 3));

//        builder.pop();

        builder.push("Spider Configs");
        this.removeSpiderSpawns = subscriber.subscribe(builder
                .comment("Disable all natural spider spawns")
                .translation("msms.remove_spider_spawns")
                .define("remove_spider_spawns", true));


        this.minSpidersFromEggSack = subscriber.subscribe(builder
                .comment("Minimun # of baby spiders from egg sack")
                .translation("msms.egg_sack_spider_min")
                .define("egg_sack_spider_min", 4));
        this.maxSpidersFromEggSack = subscriber.subscribe(builder
                .comment("Maximum # of baby spiders from egg sack")
                .translation("msms.egg_sack_spider_max")
                .define("egg_sack_spider_max", 6));
        this.eggSackClusterSize = subscriber.subscribe(builder
                .comment("max # of egg sack blocks per cluster")
                .translation("msms.egg_sack_cluster_size")
                .define("egg_sack_cluster_size", 5));

        this.eggSackCount = subscriber.subscribe(builder
                .comment("egg sacks per chunk")
                .translation("msms.egg_sack_count")
                .define("egg_sack_count", 10));
        this.spiderNestsSpawnAttempts = subscriber.subscribe(builder
                .comment("Spider Nest Spawn Attempts Per Chunk")
                .translation("msms.spider_nest_spawn_attempts")
                .define("spider_nest_spawn_attempts", 10));

        builder.pop();
    }

    public int getAggroInterval() {
        return aggroInterval.get();
    }

    public int getBoneDepositVerticalRange() {
        return boneDepositVerticalRange.get();
    }

    public int getBoneDepositRadius() {
        return boneDepositRadius.get();
    }

    public boolean isSpiderSpawnDisabled() {
        return removeSpiderSpawns.get();
    }

    public int getMinSpidersFromEggSack() {
        return minSpidersFromEggSack.get();
    }

    public int getMaxSpidersFromEggSack() {
        return maxSpidersFromEggSack.get();
    }

    public int getEggSackClusterSize() {
        return eggSackClusterSize.get();
    }

    public int getEggSackCount() {
        return eggSackCount.get();
    }

    public int getSpiderNestsSpawnAttempts() {
        return spiderNestsSpawnAttempts.get();
    }
}