package net.mingfei.java.realestate.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by mingfei.net@gmail.com
 * 5/9/17 11:48
 * https://github.com/thu/RealEstate
 */
public class Spider {
    public static void main(String[] args) throws IOException {
        String url = "http://bj.lianjia.com/ershoufang/dongcheng/";
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("li[class=clear]");
        for (Element element : elements) {
            String region = element.select("a[data-el=region]").get(0).text();
//            System.out.println(region);
            String totalPrice = element.select("div[class=totalPrice]").first().text();
            System.out.println("小区:" + region + "总价:" + totalPrice);
        }
    }
}
