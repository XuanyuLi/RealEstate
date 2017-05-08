package net.mingfei.java.realestate.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mingfei.net@gmail.com
 * 5/8/17 18:00
 * https://github.com/thu/RealEstate
 */
public class Analytics {
    private static final String[] AREA_FILE = {
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

    public static void main(String[] args) {
        for (String areaFileName : AREA_FILE) {
            getData(areaFileName);
        }
    }

    private static void getData(String areaFileName) {
        List<Integer> totalPriceList = new ArrayList<>();
        List<Integer> unitPriceList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/" + areaFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strings = line.split("@");
                int totalPrice = Integer.parseInt(strings[2]);
                totalPriceList.add(totalPrice);
                int unitPrice = Integer.parseInt(strings[3]);
                unitPriceList.add(unitPrice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(totalPriceList);
        Collections.sort(unitPriceList);

        System.out.println("---------------");
        System.out.println(totalPriceList.get(0));
        System.out.println(totalPriceList.get(totalPriceList.size() - 1));
        System.out.println(totalPriceList.stream().mapToInt(Integer::intValue).sum() / totalPriceList.size());

        System.out.println(unitPriceList.get(0));
        System.out.println(unitPriceList.get(unitPriceList.size() - 1));
        System.out.println(unitPriceList.stream().mapToInt(Integer::intValue).sum() / unitPriceList.size());

    }
}