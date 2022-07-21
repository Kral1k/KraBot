package com.kral1k.krabot.guild;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kral1k.krabot.Bot;
import com.kral1k.krabot.user.User;
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

    public Guild remove(String guildId) {
        return cache.asMap().remove(guildId);
    }

    public boolean contains(String guildId) {
        return cache.asMap().containsKey(guildId);
    }

    public Guild load(Guild guild) {
        if (this.contains(guild.getId())) return this.get(guild.getId());
        cache.put(guild.getId(), guild);
        return guild;
    }

    public Guild load(Bot bot, net.dv8tion.jda.api.entities.Guild jdaGuild) {
        if (this.contains(jdaGuild.getId())) return this.get(jdaGuild.getId());
        Guild guild = new Guild(bot, jdaGuild);
        cache.put(guild.getId(), guild);
        return guild;
    }
}
