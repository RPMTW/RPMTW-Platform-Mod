package siongsng.rpmtwupdatemod.gui;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ping {
    public static boolean isConnect() {
        try {
            final URL url = new URL("https://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}