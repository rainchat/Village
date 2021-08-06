package com.rainchat.villages.managers;


import com.rainchat.villages.data.VillageExtension;
import com.rainchat.villages.utilities.general.ServerLog;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public final class ExtensionLoader {
    private final ClassLoader loader;
    private final File folder;
    private final Map<ClassLoader, List<VillageExtension>> classLoaders = new HashMap<>();

    public ExtensionLoader(ClassLoader loader, File folder) {
        this.loader = loader;
        this.folder = folder;
    }

    private URL urlOf(File file) {
        try {
            return file.toURI().toURL();
        } catch (Exception e) {
            return null;
        }
    }

    public List<VillageExtension> loadLocal() {
        // This is as much as bukkit checks, good enough for me!
        File[] extensionFiles = folder.listFiles((file, name) -> name.endsWith(".jar"));

        List<VillageExtension> extensions = new ArrayList<>();
        // Not a directory or unable to list files for some reason
        if (extensionFiles != null)
            for (File f : extensionFiles)
                extensions.addAll(load(f));

        return extensions;
    }

    public List<VillageExtension> load(File extensionFile) {
        //ServerLog.log(Level.INFO, "Loader - Reading file: " + extensionFile.getName());

        JarFile jar;
        try {
            jar = new JarFile(extensionFile);
        } catch (Exception e) {
            ServerLog.log(Level.WARNING, "Failed to load \"" + extensionFile + "\": is it a valid jar file?");
            e.printStackTrace();
            return Collections.emptyList();
        }

        URL[] jarURLs = {urlOf(extensionFile)};

        URLClassLoader newLoader = AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {
            @Override
            public URLClassLoader run() {
                return new URLClassLoader(jarURLs, loader);
            }
        });

        Enumeration<JarEntry> entries = jar.entries();
        ArrayList<Class<?>> extensionClasses = new ArrayList<>();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory() || !entry.getName().endsWith(".class"))
                continue;

            String className = entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.');
            //ServerLog.log(Level.INFO, "Loader - Loading class: " + className);
            Class<?> clazz;
            try {
                clazz = newLoader.loadClass(className);
            } catch (Throwable e) {
                // Hide these exceptions because extension may not be enabled
                ServerLog.log(Level.WARNING, "Could not load class \"" + className + "\"");
                // e.printStackTrace();
                continue;
            }

            if (VillageExtension.class.isAssignableFrom(clazz)) {
                //ServerLog.log(Level.WARNING, "Loader - Found extension class: " + className);
                extensionClasses.add(clazz);
            }
        }

        List<VillageExtension> extensions = new ArrayList<>();

        for (Class<?> extensionClass : extensionClasses) {
            //ServerLog.log(Level.INFO, "Loader - Constructing: " + extensionClass.getName());
            VillageExtension extension;
            try {
                extension = (VillageExtension) extensionClass.getConstructor().newInstance();
            } catch (Throwable e) {
                ServerLog.log(Level.WARNING, "Exception while constructing extension class \"" + extensionClass + "\"!");
                ServerLog.log(Level.WARNING, "Is it missing a default constructor?");
                e.printStackTrace();
                continue;
            }

            extensions.add(extension);
        }

        classLoaders.put(newLoader, extensions);

        try {
            jar.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return extensions;
    }

    void unload() {

    }

    public Map<ClassLoader, List<VillageExtension>> getClassLoaders(){
        return classLoaders;
    }
}
