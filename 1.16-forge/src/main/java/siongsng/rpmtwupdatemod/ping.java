package siongsng.rpmtwupdatemod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ping {
    public static boolean isConnect(){
        String ipPath = "www.rpmtw.ga";
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec("ping " + ipPath);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is,"GBK");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            isr.close();
            br.close();
            if (!sb.toString().equals("")) {
                connect = sb.toString().indexOf("TTL") > 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }
}