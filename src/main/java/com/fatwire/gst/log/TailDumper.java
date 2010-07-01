package com.fatwire.gst.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

public class TailDumper {

    /**
     * Tool that puts a UNIX like tail on a log file, watches for a pattern
     * match and then execute a command
     * <p/>
     * 
     * Takes 3 arguments
     * <ul>
     * <li>filename to tail</li>
     * <li>regular expression, java style</li>
     * <li>command to execute when match with the regualr expression is made</li>
     * </ul>
     * <p/>
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {

        String logfile = args[0];
        Pattern pattern = Pattern.compile(args[1]);
        String cmd = args[2];
        File f = new File(logfile);

        RandomAccessFile raf = new RandomAccessFile(f, "r");
        long size = f.length();

        try {
            raf.seek(size);
            String line = null;

            while (true) {
                line = raf.readLine();
                size = raf.getFilePointer();
                if (line != null) {
                    if (pattern.matcher(line).find()) {
                        Process p = Runtime.getRuntime().exec(cmd,new String[]{line},null);
                        p.waitFor();
                    }

                } else {
                    Thread.sleep(1000L);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
