package src;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Getinformaton {

    public static String getDivContentByJsoup(String content){
        String divContent="";
        Document doc= Jsoup.parse(content);
        Elements divs=doc.getElementsByClass("js-timeline-item js-timeline-progressive-focus-container");
        divContent=divs.toString();
        return divContent;
    }



    public static List<String> getLinksByJsoup(String divContent) {
        String abs = "https://github.com/STIW3054-A191/Main-Issues/issues/1";
        Document doc = Jsoup.parse(divContent, abs);
        List<String> linkList = new ArrayList<String>();
        Elements linkStrs = doc.getElementsByClass("d-block comment-body markdown-body  js-comment-body");
        for (Element linkStr : linkStrs) {
            String url = linkStr.getElementsByTag("a").attr("abs:href");

            linkList.add(url);
        }
        return linkList;
    }



    public static List<String> getNamesByJsoup(String divContent) {
        String abs = "https://github.com/STIW3054-A191/Main-Issues/issues/1";
        String name = "";
        List<String> nameList = new ArrayList<String>();
        Document doc = Jsoup.parse(divContent, abs);
        Elements matricNums = doc.getElementsByClass("d-block comment-body markdown-body  js-comment-body");
        for (Element matricNum : matricNums) {
            Elements url = matricNum.getElementsByTag("p");
            String transfer = url.select("p").toString();
            Pattern pattern = Pattern.compile("Name: (.*?)<br>|Name :(.*?)<br>|:\\s(U.*)<br>|Name:(.*?)<br>|name :( .*?)<br> |Name (.*?)<br>");
            Matcher m = pattern.matcher(transfer);
            while (m.find()) {
                name = m.group(0);
            }

            nameList.add(name);
        }
        return nameList;
    }



    public static List<String> getMatricNumberByJsoup(String divContent) throws FileNotFoundException, IOException {
        String abs = "https://github.com/STIW3054-A191/Main-Issues/issues/1";
        String matric = "";
        List<String> matricList = new ArrayList<String>();
        Document doc = Jsoup.parse(divContent, abs);
        Elements matricNums = doc.getElementsByClass("d-block comment-body markdown-body  js-comment-body");
//        System.out.println("name==="+matricNums.size());

        for (Element matricNum : matricNums) {
            Elements url = matricNum.getElementsByTag("p");
            String transfer = url.select("p").toString();
            Pattern pattern = Pattern.compile("\\d{5,6}");
            Matcher m = pattern.matcher(transfer);
            while (m.find()) {
                matric = m.group(0);
            }
            matricList.add(matric);
        }
        return matricList;
    }



    public static String getTableByJsoup(String content){
        String divContent="";
        Document doc=Jsoup.parse(content);
        Elements divs=doc.getElementsByClass("markdown-body");
        divContent=divs.toString();
        return divContent;
    }



    public static List<String> TableList(String divContent) throws IOException{
        String matric = "";
        List<String>matricList = new ArrayList<String>();
        //Website
        String url = "https://github.com/STIW3054-A191/Assignments/wiki/List_of_Student";
        Document doc = Jsoup.parse(divContent,url);
        //get first table
        Element element = doc.select("table").first();
        Elements els = element.select("tr");
        for(Element el:els){
            Elements ele = el.select("td");
            String context = ele.select("td").toString();
            Pattern pattern = Pattern.compile("(\\b2.*?)</td>");
            Matcher m = pattern.matcher(context);
            while(m.find()){
                matric = m.group(1);
            }
            matricList.add(matric);
            //    System.out.println(matric);
        }
        return matricList;
    }



    public static void getDifferent(List<String> list1, List<String> list2){
        for(String str1 : list1){
            if(!list2.contains(str1)){

                System.out.println(str1+" have not submitted the GitHub account.");
            }
        }
        for(String str2 : list2){
            if(list1.contains(str2)){

                System.out.println(str2+" have submitted the GitHub account.");
            }
        }
    }



    public static void SaveExcel(String divContent) throws IOException{
        List<String> matricList=new ArrayList<String>();
        List<String> nameList=new ArrayList<String>();
        List<String> linkList=new ArrayList<String>();
        String abs = "https://github.com/STIW3054-A191/Main-Issues/issues/1";
        Document doc = Jsoup.parse(divContent,abs);
        Elements matricNums = doc.getElementsByClass("d-block comment-body markdown-body  js-comment-body");
        HSSFWorkbook wb = new HSSFWorkbook();
        // The second step is to add a sheet to the webbook, corresponding to the sheet in the Excel file.
        HSSFSheet sheet = wb.createSheet("行业统计");
        // The third step, add the header line 0 in the sheet
        HSSFRow row = sheet.createRow((int) 0);
        // The fourth step is to create a cell and set the header to set the header to center.
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // Create a centered format
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("No.");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("Matric_No.");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("Name");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("Git_Link");
        cell.setCellStyle(style);
        matricList.addAll(getMatricNumberByJsoup(divContent));
        nameList.addAll(getNamesByJsoup(divContent));
        linkList.addAll(getLinksByJsoup(divContent));
        for (int i = 0; i < matricNums.size(); i++)
        {
            row = sheet.createRow((int) i + 1);
            // The fourth step is to create a cell and set the value
            String id = Integer.toString(i+1);
            row.createCell((short) 0).setCellValue(new HSSFRichTextString(id));
            row.createCell((short) 1).setCellValue(new HSSFRichTextString(matricList.get(i)));
            row.createCell((short) 2).setCellValue(new HSSFRichTextString(nameList.get(i)));
            row.createCell((short) 3).setCellValue(new HSSFRichTextString(linkList.get(i)));
        }
        FileOutputStream fout = new FileOutputStream("D:/info.xls");
        wb.write(fout);
        fout.close();
        System.out.println("The MS Excel file was created in D:/info.xls");
    }}