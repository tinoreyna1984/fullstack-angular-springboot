package com.tinexlab.backend.util;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Role {

    USER(
            Arrays.asList(
            Permission.READ_ALL_USERS
            ),
            Arrays.asList(
                    Route.HOME,
                    Route.ABOUT
            )
    ),
    ADMINISTRATOR(
            Arrays.asList(
            Permission.SAVE_ONE_USER,
            Permission.MODIFY_ONE_USER,
            Permission.DELETE_ONE_USER,
            Permission.READ_ALL_USERS
            ),
            Arrays.asList(
                    Route.HOME,
                    Route.USERS,
                    Route.ABOUT
            )
    );

    private List<Permission> permissions;
    private List<Route> routes;

    Role(List<Permission> permissions, List<Route> routes) {
        this.permissions = permissions;
        this.routes = routes;
    }

    /*public List<Permission> getPermissions() {
        return permissions;
    }*/

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
