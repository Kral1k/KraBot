package com.kral1k.krabot.command;

import com.kral1k.krabot.permission.PermissionRole;
import net.dv8tion.jda.api.Permission;

public abstract class CommandSource {
    public boolean hasPermission(Permission permission) {
        return false;
    }

    public boolean hasPermission(PermissionRole permissionRole) {
        return false;
    }
}
