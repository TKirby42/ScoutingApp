package com.jimg.scoutingapp.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Jim on 2/16/14.
 */
public class UrlHelpers {
    public static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                builder.append(chars, 0, read);
            }

            return builder.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
