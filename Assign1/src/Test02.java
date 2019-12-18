package src;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Test02 {
    public static void main(String[] args) throws IOException {

        //解析url
        String threadUrl = "http://github.com/zhamri?before=Y3Vyc29yOnYyOpK5MjAxOS0wMi0yMFQxMTo1NDozNSswODowMM4Cfdf-&tab=followers";
        Document doc = (Document) Jsoup.connect(threadUrl).get();
        System.out.println(doc);
        //解析html文档
        //File input = new File("D:\\text.txt");
        //Document doc = Jsoup.parse(input, "UTF-8");

        for(Element ele : doc.getElementsByClass("tab-bbs-list").get(0).select("tbody > tr")){
            if(!ele.select("td").toString().equals("")){
                String url = ele.select("td").get(0).select("a").attr("href");
                String text = ele.select("td").get(0).select("a").text();
                String author = ele.select("td").get(1).select("a").text();
                Integer click = Integer.valueOf(ele.select("td").get(2).text());
                Integer reply = Integer.valueOf(ele.select("td").get(3).text());
                Timestamp date = Timestamp.valueOf(ele.select("td").get(4).attr("title") + ":00");
            }
        }
    }
}