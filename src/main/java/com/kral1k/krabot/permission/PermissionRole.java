package com.kral1k.krabot.permission;

public enum PermissionRole {
    DJ("859062414180089896"),
    DEVELOPER("875511344646995988");

    private final String id;

    PermissionRole(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
