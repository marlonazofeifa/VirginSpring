package application.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class util {
    static public String readFile(String path, String encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
