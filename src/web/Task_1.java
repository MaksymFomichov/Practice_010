package web;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Task_1 {
    static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void load(String links) throws IOException {
        FileReader reader = new FileReader(links);
        BufferedReader br = new BufferedReader(reader);
        ArrayList<String> strings = new ArrayList<>();
        String temp = br.readLine();
        while (temp != null) {
            strings.add(temp);
            temp = br.readLine();
        }

        for (String link : strings) {
            Future future = threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    startDownload(link.substring(link.lastIndexOf("/") + 1), link);
                }
            });
        }
    }

    private static void startDownload(String nameFile, String link) {
        new Thread(() -> {
            long endTime;
            long startTime;
            startTime = System.currentTimeMillis();
            BufferedInputStream input = null;
            try {
                input = new BufferedInputStream(new URL(link).openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedOutputStream outputStream = null;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(new File("cache" + File.separator + nameFile)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int b = 0;
            while (b != -1) {
                try {
                    b = input.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (b != -1) {
                    try {
                        outputStream.write(b);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            endTime = System.currentTimeMillis();
            System.out.println(nameFile + " скачан за " + (endTime - startTime));
        }).start();
    }
}
