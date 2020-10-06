package com.lilypuree.spawnermod;

import commoble.databuddy.config.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfigs {

    public ConfigHelper.ConfigValueListener<Integer> mobAwakenDistance;

    public ServerConfigs(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber){
        builder.push("General Category");
        this.mobAwakenDistance = subscriber.subscribe(builder
                .comment("Mob Awakening Distance")
        .translation("spawnermod.mobawakendistance")
        .define("mobawakendistance", 2));
        builder.pop();
    }
}
