package com.rainchat.villages.data;


import com.rainchat.villages.Villages;
import com.rainchat.villages.api.inter.Reloadable;
import com.rainchat.villages.utilities.annotation.Control;
import com.rainchat.villages.utilities.general.ResourceLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

// Only abstract because nobody should make a raw instance of this.
public abstract class VillageExtension implements Reloadable {
    private boolean initialized = false;
    private int remaining;
    private final String[] requirements;
    private final Plugin[] found;
    private final ResourceLoader loader;

    /**
     * Performs setup for this extension. Do not assume plugin dependencies have
     * been loaded at this point.
     */
    public VillageExtension(String... requirements) {
        this.requirements = requirements.clone();
        remaining = requirements.length;
        found = new Plugin[remaining];
        loader = new ResourceLoader(getClass().getClassLoader(), Villages.getInstance().getDataFolder());
    }


    /**
     * Supplies the extension name, primarily used for printing status info.
     * <p>
     * The default value is the simple name of the class.
     *
     * @return The extension name
     */
    @Control
    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * This must return a list of all plugins that the hook depends on.
     *
     * @return A list of all names of plugin dependencies
     */
    public final String[] getDepends() {
        return requirements.clone();
    }

    public final FileConfiguration getConfiguration(String path) {
        return loader.loadConfigNoexcept(path, true);
    }

    public final ResourceLoader getResourceLoader() {
        return loader;
    }

    @Control
    @Override
    public void onReload() {
    }

    @Control
    @Override
    public void onSave() {
    }

    @Control
    @Override
    public void onDiscard() {
    }

    @Control
    public void initialize(Plugin parent) {
    }

    @Control
    protected void disable() {
    }

    /**
     * Initializes the extension after all dependencies have been located. This is
     * an internal function and should not be called directly.
     *
     * @param parent The plugin loading extensions (QuestWorld)
     * @throws Throwable Any (likely unchecked) exception raised by
     *                   {@link VillageExtension#initialize initialize} will be passed up
     *                   the stack
     */
    public final void init(Plugin parent) {
        if (initialized || !isReady())
            return;

        // Never trust user code, this may throw anything
        initialize(parent);

        initialized = true;
    }

    /**
     * Only use this if you know what you are doing! Assigns a plugin to a specific
     * index in the resolved dependencies.
     *
     * @param plugin The plugin matching a dependency
     * @param index  The index of the match
     */
    public final boolean directEnablePlugin(Plugin plugin, int index) {
        if (!plugin.getName().equals(requirements[index]) || found[index] != null)
            return false;

        found[index] = plugin;
        --remaining;
        return true;
    }

    /**
     * Only use this if you know what you are doing! Tries to add a plugin to the
     * resolved dependencies. If the plugin name matches a dependency, the plugin is
     * added to resolved dependencies
     *
     * @param plugin The plugin we are attempting to match
     */
    public final boolean enablePlugin(Plugin plugin) {
        for (int i = 0; i < requirements.length; ++i)
            if (directEnablePlugin(plugin, i))
                return true;

        return false;
    }

    /**
     * Checks if all dependencies have been loaded.
     *
     * @return true if all dependencies have been loaded, false otherwise
     */
    public final boolean isReady() {
        return remaining <= 0;
    }

    public final boolean isInitialized() {
        return initialized;
    }

    /**
     * Gets the resolved dependencies. The order matches the order of
     * {@link #getDepends}.
     *
     * @return The list of resolved dependencies
     */
    public final Plugin[] getPlugins() {
        if (!isReady())
            throw new IllegalStateException("Attempted to get plugin references before they were resolved");

        return found.clone();
    }
}
