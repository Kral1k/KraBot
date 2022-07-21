package com.kral1k.krabot.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.guild.member.Member;
import org.jetbrains.annotations.Nullable;

public class UserCache {
    private final Cache<String, User> cache;

    public UserCache() {
        this.cache = CacheBuilder.newBuilder().build();
    }

    @Nullable
    public User get(String userId) {
        return cache.getIfPresent(userId);
    }

    public void put(User user) {
        cache.put(user.getId(), user);
    }

    public User remove(String userId) {
        return cache.asMap().remove(userId);
    }
}
