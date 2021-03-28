package com.rainchat.villages.managers;


import java.util.Arrays;
import java.util.List;

public enum ConfigSettings {


    LANGUAGE("Settings-Global.default-language", "en_En"),
    CLAIM_EXPIRATION_CLAIM_TIME("Settings-Claim.expiration.time", "5"),
    CLAIM_GLOBAL_PROTECT_MODE("Settings-Claim.protect_mode", "protect"),
    CLAIM_DEFAULT_CLAIM_LIMIT("Settings-Claim.default-claim-limit", "6"),
    CLAIM_ENABLED_WORLDS_LIST("Settings-Claim.enabled-worlds", Arrays.asList(
            "world",
            "test"));

    String path;
    String settings;
    private List<String> settingsList;


    ConfigSettings(String path, String settings) {
        this.path = path;
        this.settings = settings;
    }

    ConfigSettings(String path, List<String> settingsList) {
        this.path = path;
        this.settingsList = settingsList;
    }

    public String getString() {
        return FileManager.Files.CONFIG.getFile().getString(path, settings);
    }

    public Long getLong() {
        return FileManager.Files.CONFIG.getFile().getLong(path, Long.parseLong(settings));
    }

    public int getInt() {
        return FileManager.Files.CONFIG.getFile().getInt(path, Integer.parseInt(settings));
    }


    public List<String> getList() {
        boolean exists = exists();
        if (exists) {
            return FileManager.Files.CONFIG.getFile().getStringList(path);
        } else {
            return settingsList;
        }

    }

    public double getDouble() {
        return FileManager.Files.CONFIG.getFile().getDouble(path, Double.parseDouble(settings));
    }

    public boolean getBoolen() {
        return FileManager.Files.CONFIG.getFile().getBoolean(path, Boolean.parseBoolean(settings));
    }


    private boolean exists() {
        return FileManager.Files.CONFIG.getFile().contains(path);
    }

}
