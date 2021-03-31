package com.rainchat.villages.data.village;

import com.rainchat.villages.data.enums.VillageGlobalPermission;
import com.rainchat.villages.data.enums.VillagePermission;
import org.bukkit.Location;

import java.util.*;

public class Village {

    private String name, description;
    private UUID owner;
    private long lastActive;


    private final Set<VillageMember> villageMembers;
    private final Set<VillageClaim> villageClaims;
    private final Set<VillageRole> villageRoles;
    private final Set<VillageGlobalPermission> villagePermissions;

    private VillageLocation villageLocation;

    public Village(String name, String description, UUID owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;


        this.villageMembers = new HashSet<>();
        this.villageClaims = new HashSet<>();
        this.villagePermissions = new HashSet<>();
        this.villageRoles = new HashSet<>();
        this.lastActive = System.currentTimeMillis();

    }

    public void add(String role, VillagePermission villagePermission) {
        for (VillageRole roles : villageRoles) {
            if (roles.getName().equals(role)) {
                roles.add(villagePermission);
            }
        }
    }

    public void addRole(VillageRole villageRole) {
        this.villageRoles.add(villageRole);
    }

    public void add(VillageMember villageMember) {
        villageMembers.add(villageMember);
    }

    public void add(VillageClaim villageClaim) {
        villageClaims.add(villageClaim);
    }

    public void add(VillageGlobalPermission villageGlobalPermission) {
        villagePermissions.add(villageGlobalPermission);
    }

    public void add(long time) {
        lastActive = time;
    }

    public void remove(VillageMember villageMember) {
        villageMembers.remove(villageMember);
    }

    public void remove(String role, VillagePermission islandPermission) {
        for (VillageRole roles : villageRoles) {
            if (roles.getName().equals(role)) {
                roles.remove(islandPermission);
            }
        }
    }

    public void remove(VillageClaim villageClaim) {
        villageClaims.remove(villageClaim);
    }

    public void remove(VillageGlobalPermission villageGlobalPermission) {
        villagePermissions.remove(villageGlobalPermission);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }


    public void setLocation(Location location) {
        this.villageLocation = new VillageLocation(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                Objects.requireNonNull(location.getWorld()).getName()
        );
    }

    public VillageMember getMember(UUID uuid) {
        for (VillageMember villageMember : villageMembers) {
            if (villageMember.getUniqueId().equals(uuid)) {
                return villageMember;
            }
        }
        return null;
    }

    public boolean hasPermission(VillagePermission villagePermission, UUID uuid) {
        VillageMember villageMember = getMember(uuid);
        if (villageMember != null) {
            String villageRole = villageMember.getRole();
            if (villageMember.getRole() != null) {
                return hasPermission(villageMember.getRole(), villagePermission);
            }
        }
        return false;
    }

    public boolean hasRole(String role) {
        for (VillageRole villageRole : villageRoles) {
            if (villageRole.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPermission(String role, VillagePermission villagePermission) {
        for (VillageRole roles : villageRoles) {
            if (roles.getName().equals(role)) {
                return roles.hasPermission(villagePermission);
            }
        }
        return false;
    }


    public boolean hasPermission(VillageGlobalPermission villageGlobalPermission) {
        return villagePermissions.contains(villageGlobalPermission);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public UUID getOwner() {
        return owner;
    }

    public Set<VillageMember> getVillageMembers() {
        return Collections.unmodifiableSet(villageMembers);
    }

    public Set<VillageClaim> getVillageClaims() {
        return Collections.unmodifiableSet(villageClaims);
    }

    public VillageLocation getVillageLocation() {
        return villageLocation;
    }

    public VillageRole getRole(String role) {
        for (VillageRole villageRole : villageRoles) {
            if (villageRole.getName().equals(role)) {
                return villageRole;
            }
        }
        return null;
    }

    public Set<VillageRole> getRoles() {
        return villageRoles;
    }

    public Long getLastActive() {
        return lastActive;
    }
}
