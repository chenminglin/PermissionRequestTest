package com.bethena.permissionrequesttest;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        System.out.println(Math.sqrt(48 * 48 - ((48 - 44) ^ 2)));
        System.out.println(Math.sqrt(4));
        System.out.println(Math.pow(3,3));
        System.out.println(3 ^ 3);
        System.out.println(4%4);

        File file = new File("C:\\Users\\win10\\Desktop\\SVG.svg");
        System.out.println(FileUtils.getFileType(file.getAbsolutePath()));
        System.out.println(FileUtils.getFileHeader(file.getAbsolutePath()));

        File file2 = new File("C:\\Users\\win10\\Desktop\\a.xml");
        System.out.println(FileUtils.getFileType(file2.getAbsolutePath()));
        System.out.println(FileUtils.getFileHeader(file2.getAbsolutePath()));


        long current = 210l;
        long total = 400l;
        float perf = (float)current/total;
        System.out.println((int) (perf * 100));

        System.out.println("-----------------------");
        System.out.println(downloadCountToCn(1910223344));
    }



    public String downloadCountToCn(long downloadCount){
        if(downloadCount<10000){
            return String.valueOf(downloadCount);
        }else if(downloadCount < 100000000){
            return (downloadCount / 10000) + "万";
        }else {
            return (downloadCount / 100000000) + "亿";
        }
    }
}