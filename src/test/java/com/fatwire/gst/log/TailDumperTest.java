package com.fatwire.gst.log;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import junit.framework.TestCase;

public class TailDumperTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMain() throws Exception {
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    Writer writer = new FileWriter("out.log", true);
                    for (int i = 1; i < 200; i++) {

                        Thread.sleep(20);
                        writer.write((i % 25 == 0) ? "aab\r\n" : i + " hello\r\n");

                    }
                    writer.close();
                    Thread.sleep(1500);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        });
        t.start();
        Thread t2 = new Thread(new Runnable() {

            public void run() {
                try {
                    TailDumper.main(new String[] { "out.log", "^a.b", "cmd /c dir >> tailed" });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });
        t2.start();

        t.join();
        System.exit(0);
    }

}
