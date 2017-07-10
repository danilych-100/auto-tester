package renue.fts.gateway.admin.autotest.loader;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Danil on 07.07.2017.
 */
public interface IFileLoader {
    /**
     * Load file from FileSystem.
     */
    String loadFileFromDir() throws FileNotFoundException;
}
