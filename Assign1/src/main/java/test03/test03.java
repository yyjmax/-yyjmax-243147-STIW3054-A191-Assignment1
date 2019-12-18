
package test03;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

class Test extends Getinformaton {

    public static String getContentByJsoup(String url) {
        String content = "";
        try {
            Document doc = Jsoup.connect(url)
                    .data("jquery", "java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(50000)
                    .get();
            content = doc.toString();//get HTML information from web
            System.out.println(doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void main(String[] args) throws IOException {


        String url = "https://github.com/STIW3054-A191/Main-Issues/issues/1";
        String url2 = "https://github.com/STIW3054-A191/Assignments/wiki/List_of_Student";
        String HtmlContent = getContentByJsoup(url);
        String HtmlContent2 = getContentByJsoup(url2);
        String divContent = getDivContentByJsoup(HtmlContent);
        String divContent2 = getTableByJsoup(HtmlContent2);
        TableList(divContent2);
        getLinksByJsoup(divContent);
        getNamesByJsoup(divContent);
        getMatricNumberByJsoup(divContent);
        getDifferent(TableList(divContent2), getMatricNumberByJsoup(divContent));
        SaveExcel(divContent);
    }

}





