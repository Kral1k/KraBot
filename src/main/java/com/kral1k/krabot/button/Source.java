package com.kral1k.krabot.button;

import com.kral1k.krabot.permission.PermissionRole;
import net.dv8tion.jda.api.Permission;

public abstract class Source {
    public boolean hasPermission(Permission permission) {
        return false;
    }

    public boolean hasPermission(PermissionRole permissionRole) {
        return false;
    }

    public abstract boolean has(String userId);
}
