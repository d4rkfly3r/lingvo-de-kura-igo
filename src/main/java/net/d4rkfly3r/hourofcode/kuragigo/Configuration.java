package net.d4rkfly3r.hourofcode.kuragigo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.nio.file.Paths;

public class Configuration {

    public static final boolean DEBUG = false;
    public static final File SCRIPTS_FOLDER_FILE = Paths.get("scripts").toFile();
    public static final boolean CLEANUP_BUILD_FOLDER = false;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}
