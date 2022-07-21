package com.kral1k.krabot.permission;

import com.kral1k.krabot.guild.member.Member;
import net.dv8tion.jda.api.Permission;

public class Perms {
    private final PermissionType type;
    private Permission perm;
    private PermissionRole role;
    private String userId;

    public Perms(Permission jdaPermission) {
        this.type = PermissionType.DS;
        this.perm = jdaPermission;
    }
    public Perms(PermissionRole role) {
        this.type = PermissionType.ROLE;
        this.role = role;
    }
    public Perms(String userId) {
        this.type = PermissionType.USER;
        this.userId = userId;
    }

    public PermissionType getType() {
        return type;
    }

    public Permission getJdaPermission() {
        return perm;
    }

    public PermissionRole getRole() {
        return role;
    }

    public String getUserId() {
        return userId;
    }

    public boolean has(Member member) {
        return switch (type) {
            case ROLE -> member.hasPermission(role);
            case USER -> member.getId().equals(userId);
            case DS -> member.hasPermission(perm);
        };
    }
}
