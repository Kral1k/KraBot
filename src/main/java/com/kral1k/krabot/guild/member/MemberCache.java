package com.kral1k.krabot.guild.member;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kral1k.krabot.guild.Guild;
import org.jetbrains.annotations.Nullable;

public class MemberCache {

    private final Cache<String, Member> cache;

    public MemberCache() {
        this.cache = CacheBuilder.newBuilder().build();
    }

    @Nullable
    public Member get(String memberId) {
        return cache.getIfPresent(memberId);
    }

    public void put(Member member) {
        cache.put(member.getId(), member);
    }
}
