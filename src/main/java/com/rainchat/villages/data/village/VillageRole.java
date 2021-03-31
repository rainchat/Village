package com.rainchat.villages.data.village;


import com.rainchat.villages.data.enums.VillagePermission;

import java.util.HashSet;
import java.util.Set;

public class VillageRole {
    private final String name;
    private final Set<VillagePermission> islandPermissions;

    public VillageRole(String name) {
        this.name = name;
        this.islandPermissions = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void add(VillagePermission villagePermission) {
        islandPermissions.add(villagePermission);
    }


    public void remove(VillagePermission villagePermission) {
        islandPermissions.remove(villagePermission);
    }

    public boolean hasPermission(VillagePermission villagePermission) {
        return islandPermissions.contains(villagePermission);
    }

}
