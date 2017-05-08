package net.mingfei.java.realestate.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mingfei.net@gmail.com
 *         2017/5/7 09:49
 *         https://github.com/thu/RealEstate
 */
public class RegExp {
    /**
     *
     * ,
     * @param args
     */
    public static void main(String[] args) { //
        String regExp = "region\">([\u4e00-\u9fbb]+) </a> ([0-9\u4e00-\u9fbb|\\s.]+)</div>[a-zA-Z0-9\u4e00-\u9fbb\\s=\"<>/_:().-]+totalPrice\"><span>(\\d+)[a-zA-Z0-9\u4e07\\s=\"<>/-]+data-price=\"(\\d+)";
        Pattern pattern = Pattern.compile(regExp);
        int counter = 0;
        try {
            URL url = new URL("http://127.0.0.1:8080/index.jsp");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        System.out.println(matcher.group(1) + "@" + matcher.group(2) + "@" + matcher.group(3) + "@" + matcher.group(4));
                        counter++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println("counter:@" + counter);
    }
}
