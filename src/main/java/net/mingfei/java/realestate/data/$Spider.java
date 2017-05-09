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
public class $Spider {
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
            37,
            100,
            100,
            70,
            21,
            47,
            94,
            42,
            6,
            19,
            14,
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
    private static final String UA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
    private static final String COOKIE = "lianjia_uuid=40ea38f9-0e0f-4934-afd2-9f98a8e3cc6d; all-lj=492291e11daf53bf34d39f84cc442d11; UM_distinctid=15bddd196085ea-00e1e40a0bace7-1d3a6853-13c680-15bddd196094c5; select_city=110000; CNZZDATA1253477573=221890453-1494075057-%7C1494166864; _smt_uid=590dc70f.2052aa72; CNZZDATA1254525948=1071354148-1494070831-%7C1494168038; CNZZDATA1255633284=1884340771-1494072722-%7C1494169924; CNZZDATA1255604082=203650138-1494072925-%7C1494170141; _gat=1; _gat_past=1; _gat_global=1; _gat_new_global=1; _ga=GA1.2.1356314706.1494075153; _gid=GA1.2.188229283.1494171617; _gat_dianpu_agent=1; lianjia_ssid=32635c63-c90c-4da2-9981-85f953c353c9";
    private static int counter;

    public static void main(String[] args) {
        for (int i = 0; i < AREA.length; i++) {
            counter = 0;
            System.out.println("area:" + AREA[i]);
            for (int j = 0; j < PAGES[i]; j++) {
                System.out.println("\t page: " + (j + 1));
                try {
                    getPage(j + 1, AREA[i]);
                    Thread.sleep((int) (Math.random() * 1000 * 10) + 1000 * 10);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000 * 60 * 3);
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

        System.setProperty("http.agent", UA);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Cookie", COOKIE);
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
                    bufferedWriter.write(matcher.group(1) + "@" + matcher.group(2) + "@" + matcher.group(3) + "@" + matcher.group(4) + "\n");
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
