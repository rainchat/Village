package com.rainchat.villages.data.village;

import java.util.UUID;

public class VillageMember {

    private long cooldown = 0;
    private final UUID uuid;

    private String role;

    public VillageMember(UUID uuid) {
        this.uuid = uuid;
    }


    public String getRole() {
        return role;
    }


    public void setCooldown(long time) {
        this.cooldown = System.currentTimeMillis() + (time * 1000);
    }

    public boolean hasCooldown() {
        return (cooldown > System.currentTimeMillis());
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UUID getUniqueId() {
        return uuid;
    }
}
