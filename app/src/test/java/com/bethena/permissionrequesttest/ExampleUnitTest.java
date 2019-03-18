package com.bethena.permissionrequesttest;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.apache.tika.Tika;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
        System.out.println(Math.pow(3,2));
        System.out.println(4%4);

        File file = new File("C:\\Users\\win10\\Desktop\\SVG.svg");
        System.out.println(FileUtils.getFileType(file.getAbsolutePath()));
        System.out.println(FileUtils.getFileHeader(file.getAbsolutePath()));

        File file2 = new File("C:\\Users\\win10\\Desktop\\a.xml");
        System.out.println(FileUtils.getFileType(file2.getAbsolutePath()));
        System.out.println(FileUtils.getFileHeader(file2.getAbsolutePath()));
    }
}