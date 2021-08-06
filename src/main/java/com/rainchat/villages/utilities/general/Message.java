package com.rainchat.villages.utilities.general;

import com.rainchat.rainlib.utils.Color;
import com.rainchat.villages.api.placeholder.replacer.CustomPlaceholderReplacements;
import com.rainchat.villages.managers.FileManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public enum Message {
    PREFIX("Messages.prefix", "&e&lVillages: &7"),
    DISBAND("Messages.disband", "&7Oh no! &a&l{0} &7fell into ruins..."),
    HELP("Messages.help", "Type &b/villages help [page] &7to look at the help pages."),
    RELOAD("Messages.reload", "&aconfigurations were successfully reloaded."),
    NO_COMMAND_PERMISSION("Messages.no-command-permission", "You do not have permissions for that command."),
    NO_PERMISSION("Messages.no-permission", "You do not have permissions for &b{0}&7."),
    PLAYER_OFFLINE("Messages.player-offline", "The player &b{0} &7does not seem to be online."),


    PAGE_FORMAT("Messages.page-format", "&b{0}. &7{1}"),
    PAGE_HELP("Messages.page-help", "&e&lHelp: &7[{0}/{1}]"),
    PAGE_LIMIT("Messages.page-limit", "There are only &b{0} &7help pages."),
    PAGE_NEXT("Messages.page-next", "Type &b/villages help {0} &7for the next page."),
    PAGE_PREVIOUS("Messages.page-previous", "Type &b/villages help {0} &7for the previous page."),
    CLAIM_PAGE("Messages.claim-page", "&7========= &e&lClaim: &7[{0}/{1}] &7========="),
    CLAIM_LIST_EMPTY("Messages.claim-list-empty", "&7Village list is empty"),

    REQUEST_ACCEPT("Messages.request-accept", "&a[Accept]"),
    REQUEST_DENY("Messages.request-deny", "&c[Deny]"),
    REQUEST_DENIED("Messages.request-denied", "You have decided to deny the request."),
    REQUEST_DISBAND("Messages.request-disband", "Are you sure you want to disband the village? Members of your village may not be happy!"),
    REQUEST_INVITE("Messages.request-invite", "You have sent an invite request to &b{0}&7."),
    REQUEST_INVITE_SELF("Messages.request-invite-self", "You can't invite yourself."),
    REQUEST_INVITE_TARGET("Messages.request-invite-target", "You have been invited to join &b{0}&7. Do you wish to join?"),
    REQUEST_INVITE_TARGET_NOT_NULL("Messages.request-invite-target-not-null", "The player &b{0} &7already belongs to a village."),
    REQUEST_JOIN("Messages.request-join", "&b{0} &7has joined the village."),
    REQUEST_JOIN_TARGET("Messages.request-join-target", "&7You have joined the &b{0} &7village."),
    REQUEST_KICK("Messages.request-kick", "Are you sure you want to kick &b{0} &7 from your village?"),
    REQUEST_KICK_SELF("Messages.request-kick-self", "You can't kick yourself from the village."),
    REQUEST_KICK_TARGET("Messages.request-kick-target", "You have been kicked from the &b{0} &7village."),
    REQUEST_NULL("Messages.request-null", "You do not have any pending requests."),
    REQUEST_PENDING("Messages.request-pending", "You already have a pending request."),

    TITLE_HEADER("Title.header", "&e&l{0}!"),
    TITLE_FOOTER("Title.footer", "&7Welcome to &b{0}'s &7village."),
    TITLE_WILDERNESS_HEADER("Title.wilderness-header", "&a&lWilderness!"),
    TITLE_WILDERNESS_FOOTER("Title.wilderness-footer", "&7Fresh new land awaits you."),
    TOOLTIP("Messages.tooltip", "&7Click to select."),
    USAGE("Messages.usage", "Usage: &b{0}"),

    ECONOMY_CREATE_VILLAGE("Economy.create-village", "&7You don't have enough money to &ecreate &7the &evillage &8(need money &c%econ_need%&8)"),
    ECONOMY_CLAIM_VILLAGE("Economy.claim-village", "&7You don't have enough money to &eclaim &7the chunk &8(need money &c%econ_need%&8)"),

    VILLAGE_INFO("Messages.info-village", Arrays.asList(
            "&7&o%village_target_name%",
            "",
            "&7Owner: &b%target_owner%",
            "&7Members: &e%target_members%",
            "&7Claims: &c%target_claims%"
    )),
    VILLAGE_NULL_CHECK("Messages.info-village-null", "&eNo village was found on this chunk!"),
    VILLAGE_NO_EXISTS("Messages.village-no-exists", "A village named &b%arg_2% &7does not exist."),
    VILLAGE_ADMIN_UNCLAIM_ONE("Messages.village-admin-unclaim-one", "'&7You can''t unclaim because this village have only one land.'"),
    VILLAGE_ADMIN_ENABLED("Messages.village-admin-enabled", "Admin mode: &aenabled"),
    VILLAGE_ADMIN_ADD_CLAIMS("Messages.village-admin-add-claims", "You have successfully added a claims to village"),
    VILLAGE_ADMIN_NULL("Messages.village-admin-null", "No village exists at your current location."),
    VILLAGE_ADMIN_UNCLAIM("Messages.village-admin-unclaim", "Successfully unclaimed land at &b{0} &7from &b{1}&7."),
    VILLAGE_ADMIN_DISABLED("Messages.village-admin-disabled", "Admin mode: &cdisabled"),
    VILLAGE_ALREADY_OWNER("Messages.village-already-owner", "You can't set the new owner to yourself."),
    VILLAGE_CLAIM("Messages.village-claim", "You have claimed new land for your village: &b{0}"),
    VILLAGE_CLAIM_OWNED("Messages.village-claim-owned", "The land you are trying to claim already belongs to your village."),
    VILLAGE_CLAIM_OTHER("Messages.village-claim-other", "The land you are trying to claim belongs to another village."),
    VILLAGE_CLAIM_TELEPORT("Messages.village-teleport", "You have been teleported to claim: &b{0}&7"),
    VILLAGE_COOLDOWN("Messages.village-cooldown", "You can teleport home again in &b{0} &7seconds."),
    VILLAGE_CREATE("Messages.village-create", "You have established a new village named &b{0}&7."),
    VILLAGE_CREATE_LIMIT("Messages.village-create", "You have established a new village named &b{0}&7."),
    VILLAGE_CREATE_OTHER("Messages.village-create-other", "The name of your village can't be longer than &b32 &7characters."),
    VILLAGE_DESCRIPTION_LIMIT("Messages.village-description-limit", "The description of your village can't be longer than &b32&7 characters."),
    VILLAGE_EXISTS("Messages.village-exists", "A village named &b{0} &7has already been established."),
    VILLAGE_HOME("Messages.village-home", "You have been teleported to your village home."),
    VILLAGE_LEAVE("Messages.village-leave", "You have left the &b{0} &7village."),
    VILLAGE_LEAVE_OWNER("Messages.village-leave-owner", "You can't leave your current village because you are the owner."),
    VILLAGE_MAX_CLAIMS("Messages.village-max-claims", "You can't claim more than &b{0} &7land for you village."),
    VILLAGE_MEMBER_NULL("Messages.village-member-null", "&b{0} &7does not seem to belong to your village."),
    VILLAGE_NOT_NULL("Messages.village-not-null", "You already belong to a village."),
    VILLAGE_NULL("Messages.village-null", "You do not belong to a village."),
    VILLAGE_OWNER("Messages.village-owner", "You must be the owner of the village to do that action."),
    VILLAGE_RENAME("Messages.village-rename", "You have renamed the village to: &b{0}"),
    VILLAGE_SET_DESCRIPTION("Messages.village-set-description", "You have set the village description to: &b{0}"),
    VILLAGE_SET_HOME("Messages.village-set-home", "You have set the home for your village."),
    VILLAGE_SET_OWNER("Messages.village-set-owner", "You have set the new village owner to &b{0}&7."),
    VILLAGE_UNCLAIM("Messages.village-unclaim", "You have unclaimed land for your village."),
    VILLAGE_UNCLAIM_ONE("Messages.village-unclaim-one", "You can't unclaim because your village only owns one land."),
    VILLAGE_UNCLAIM_OTHER("Messages.village-unclaim-other", "The land you are trying to unclaim does not belong to your village."),

    VILLAGE_SUB_CLAIM_NULL("Messages.village-sub-claim-null", "&7Selected points are not in the village"),
    VILLAGE_SUB_CLAIM_CUT("Messages.village-sub-claim-cut", "&7subclaims cannot &7overlap"),
    VILLAGE_MAX_SUB_CLAIMS("Messages.village-sub-claim-cut", "&7You can't sub-claim more than &b{0} &7land for you village"),
    VILLAGE_SUB_CLAIM_NAME("Messages.village-sub-claim-name", "&7The name for the &esubclaim &7must be unique"),
    VILLAGE_SUB_CLAIM_NAME_NULL("Messages.village-sub-claim-name-null", "&7There is no &esubclaim &7with this name."),
    VILLAGE_SUB_CLAIM_UNSELECTED("Messages.village-sub-claim-unselected", "&7pos for &esubclaim &7are not selected"),
    VILLAGE_SUB_CLAIM_REMOVE("Messages.village-sub-claim-remove", "&7Subclaim &e%subclaim_name% &7successfully deleted "),
    VILLAGE_SUB_CLAIM_ADD_MEMBER("Messages.village-sub-claim-add-member", "&7The player was successfully added to the subclaim"),
    VILLAGE_SUB_CLAIM("Messages.village-sub-claim", "You have claimed new subclaim whith name &b%subclaim_name%"),
    VILLAGE_NEARBY_CHUNKS("Messages.village-nearby-chunks", "Your village is too far from this &7chunk"),


    WORLDGUARD_CLAIM("Messages.world-guard", "You can't claim in a worldguard region."),
    WORLDGUARD_CREATE("Messages.world-guard", "You can't create land in a worldguard region."),
    WORLD_NOT_ENABLED("Messages.world-not-enabled", "Villages is not enabled in this world.");

    private static FileManager.CustomFile configuration;
    private final String path;
    private String def;
    private List<String> list;

    Message(String path, String def) {
        this.path = path;
        this.def = def;
    }

    Message(String path, List<String> list) {
        this.path = path;
        this.list = list;
    }

    public static int addMissingMessages() {
        FileConfiguration file = configuration.getFile();
        int index = 0;

        boolean saveFile = false;
        for (Message message : values()) {
            index++;
            if (!file.contains(message.getPath())) {
                saveFile = true;
                if (message.getDefaultMessage() != null) {
                    file.set(message.getPath(), message.getDefaultMessage());
                } else {
                    file.set(message.getPath(), message.getDefaultListMessage());
                }
            }
        }
        if (saveFile) {
            configuration.saveFile();
        }


        return index;
    }

    public static String convertList(List<String> list) {
        String message = "";
        for (String line : list) {
            message += line + "\n";
        }
        return message;
    }

    public static void setConfiguration(FileManager.CustomFile configuration) {
        Message.configuration = configuration;
    }

    public String getDef() {
        return configuration.getFile().getString(path, def);
    }

    @Override
    public String toString() {
        String message;
        boolean isList = isList();
        boolean exists = exists();
        if (isList) {
            if (exists) {
                message = convertList(configuration.getFile().getStringList(path));
            } else {
                message = convertList(getDefaultListMessage());
            }
        } else {
            if (exists) {
                message = configuration.getFile().getString(path);
            } else {
                message = getDefaultMessage();
            }
        }

        return Color.parseHexString(Chat.translateRaw(message,new CustomPlaceholderReplacements()));
    }

    public String getMessage() {
        return Color.parseHexString(configuration.getFile().getString(path, def));
    }

    private boolean exists() {
        return configuration.getFile().contains(path);
    }

    private boolean isList() {
        if (configuration.getFile().contains(path)) {
            return !configuration.getFile().getStringList(path).isEmpty();
        } else {
            return def == null;
        }
    }

    public List<String> toList() {
        return configuration.getFile().getStringList(path);
    }

    public String getPath() {
        return path;
    }

    public List<String> getList() {
        return list;
    }

    private String getDefaultMessage() {
        return def;
    }

    private List<String> getDefaultListMessage() {
        return list;
    }
}
