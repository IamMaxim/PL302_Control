package ru.iammaxim.PL302_Control;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by maxim on 5/1/17 at 10:32 AM.
 */
public class Net {
    public static String ip;
    private static final String delimiter = "__";

    public static JSONObject read(String... values) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append("http://").append(ip).append("/index.html?read=");
        url.append(String.join(delimiter, values));

        HttpURLConnection connection = (HttpURLConnection) new URL(url.toString()).openConnection();
        Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("\\A");
        String response = scanner.next();
        scanner.close();
        return new JSONObject(response);
    }

    public static void write(String... KaVs) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append("http://").append(ip).append("/index.html?write=");
        url.append(String.join(delimiter, KaVs));
        HttpURLConnection connection = (HttpURLConnection) new URL(url.toString()).openConnection();
        Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("\\A");
        if (scanner.hasNext())
            scanner.next();
        scanner.close();
    }
}
