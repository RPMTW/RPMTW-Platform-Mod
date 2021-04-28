package siongsng.rpmtwupdatemod.function;

import java.io.IOException;

public class File_Writer {
    public static void Writer(String text, String FileName) throws IOException {
        java.io.FileWriter fw = new java.io.FileWriter(FileName);
        fw.write(text);
        fw.flush();
        fw.close();
    }
}
