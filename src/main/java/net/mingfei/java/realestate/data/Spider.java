package net.mingfei.java.realestate.data;

import sun.net.ConnectionResetException;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mingfei.net@gmail.com
 *         2017/5/6 20:45
 *         https://github.com/thu/RealEstate
 */
public class Spider {
    private static final String[] AREA = {
            "dongcheng",
            "xicheng",
            "chaoyang",
            "haidian",
            "fengtai",
            "shijingshan",
            "tongzhou",
            "changping",
            "daxing",
            "yizhuangkaifaqu",
            "shunyi",
            "fangshan",
            "mentougou",
            "pinggu",
//            "huairou",
            "miyun",
            "yanqing",
            "yanjiao"
    };
    private static final int[] PAGES = {
            35,
            35,
            100,
            100,
            67,
            21,
            46,
            94,
            42,
            6,
            29,
            17,
            14,
            1,
//            0,
            1,
            1,
            100
    };

    private static final String proxy_ip = "52.58.249.129";
    private static final int proxy_port = 8083;
    private static final String LIANJIA_URL = "http://bj.lianjia.com/ershoufang/";
    private static final String REGEXP = "region\">([\u4e00-\u9fbb]+) </a> ([0-9\u4e00-\u9fbb|\\s.]+)</div>[a-zA-Z0-9\u4e00-\u9fbb\\s=\"<>/_:().-]+totalPrice\"><span>(\\d+)[a-zA-Z0-9\u4e07\\s=\"<>/-]+data-price=\"(\\d+)";
    ;
    private static int counter;

    public static void main(String[] args) {
        for (int i = 0; i < AREA.length; i++) {
            counter = 0;
            System.out.println("area:" + AREA[i]);
            for (int j = 0; j < PAGES[i]; j++) {
                System.out.println("\t page: " + (j + 1));
                try {
                    getPage(j + 1, AREA[i]);
                    Thread.sleep((int) (Math.random() * 1000 * 10));
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getPage(int page, String area) throws IOException {
        URL url = new URL(LIANJIA_URL + area + "/pg" + page);

        // proxy begin
//        InetSocketAddress inetSocketAddress = new InetSocketAddress(proxy_ip, proxy_port);
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, inetSocketAddress);
        // proxy end

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("data/" + area, true))
        ) {

            String line;
            Pattern pattern = Pattern.compile(REGEXP);
            Matcher matcher;
            while ((line = bufferedReader.readLine()) != null) {
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    bufferedWriter.write(matcher.group(1) + "\n");
                    System.out.println("\t\t counter: " + (++counter));
                }
            }
        } catch (ConnectionResetException e) {
            System.out.println("connection reset...");
            getPage(page, area);
        } catch (ConnectException e) {
            System.out.println("time out...");
            getPage(page, area);
        } catch (IOException e) {
            System.out.println("io...");
            getPage(page, area);
        }
    }
}
