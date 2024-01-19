package com.tinexlab.backend.util;

import lombok.Getter;

@Getter
public enum Route {
    HOME("Inicio",""),
    USERS("Usuarios","users"),
    ABOUT("Acerca de","about");

    private String name;
    private String path;

    Route(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
