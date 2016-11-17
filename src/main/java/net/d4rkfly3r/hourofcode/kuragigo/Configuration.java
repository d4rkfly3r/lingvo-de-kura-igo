package net.d4rkfly3r.hourofcode.kuragigo;

import java.io.File;
import java.nio.file.Paths;

public class Configuration {

    public static final boolean DEBUG = true;
    public static final File SCRIPTS_FOLDER_FILE = Paths.get("scripts").toFile();
    public static final boolean CLEANUP_BUILD_FOLDER = false;

}
