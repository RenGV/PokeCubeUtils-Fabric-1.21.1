package com.rengv.pokecubeutils.config;

public class PlayerData {
    private String name;
    private boolean manager;

    public PlayerData() {
    }

    public PlayerData(String name) {
        this.name = name;
    }

    public PlayerData(String name, boolean manager) {
        this.name = name;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public boolean isManager() {
        return manager;
    }
}
