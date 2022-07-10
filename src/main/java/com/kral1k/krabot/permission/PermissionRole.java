package com.kral1k.krabot.permission;

public enum PermissionRole {
    DJ("971668959805988875"),
    DEVELOPER("971664669330788403");

    private final String id;

    PermissionRole(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
