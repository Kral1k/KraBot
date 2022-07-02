package com.kral1k.krabot.guild;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.Nullable;

public class GuildCache {
    private final Cache<String, Guild> cache;

    public GuildCache() {
        this.cache = CacheBuilder.newBuilder().build();
    }

    @Nullable
    public Guild get(String guildId) {
        return cache.getIfPresent(guildId);
    }

    public void put(Guild guild) {
        cache.put(guild.getId(), guild);
    }
}
