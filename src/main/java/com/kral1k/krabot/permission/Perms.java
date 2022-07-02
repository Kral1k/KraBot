package com.kral1k.krabot.permission;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

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

    public boolean has(Member jdaMember) {
        return switch (type) {
            case ROLE -> jdaMember.getRoles().stream().anyMatch(role1 -> role1.getId().equals(role.getId()));
            case USER -> jdaMember.getId().equals(userId);
            case DS -> jdaMember.hasPermission(perm);
        };
    }
}
