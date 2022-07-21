package com.kral1k.krabot.permission;

public enum PermissionRole {
    DJ,
    DEVELOPER;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
