package renue.fts.gateway.admin.autotest.loader;


import java.io.*;

/**
 * YML loader.
 * Load file TO and FROM directories.
 */
public final class YMLApplicationFileLoader {

    private YMLApplicationFileLoader() {}

    /**
     * Load file from FileSystem.
     *
     * @param ymlDir Directory where yml is situated.
     * @return information about loading.
     */
    public static String loadYMLAppFileFromDir(final File ymlDir) throws FileNotFoundException {
        String[] ymlFiles = ymlDir.list((dir, name) -> name.contains("application.yml"));
        if (ymlFiles.length == 0) {
            return "File not found";
        }
        return ymlDir + "\\" + ymlFiles[0];
    }

}
