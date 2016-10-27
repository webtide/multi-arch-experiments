package org.eclipse.jetty.experiments;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jetty.toolchain.test.OS;
import org.junit.Test;

public class FileSystemTest
{
    @Test
    public void testCaseDifferences() throws IOException
    {
        File win1, win2, win3;
        
        if(OS.IS_WINDOWS)
        {
            win1 = new File("c:\\Windows");
            win2 = new File("C:\\Windows");
            win3 = new File("C:\\WINDOWS");
        }
        else if(OS.IS_OSX)
        {
            win1 = new File("/Library/Java");
            win2 = new File("/library/java");
            win3 = new File("/LIBRARY/JAVA");
        }
        else
        {
            win1 = new File("/usr/local");
            win2 = new File("/Usr/Local");
            win3 = new File("/USR/LOCAL");
        }
    
        System.out.println("1.getAbsolutePath = " + win1.getAbsolutePath());
        System.out.println("2.getAbsolutePath = " + win2.getAbsolutePath());
        System.out.println("3.getAbsolutePath = " + win3.getAbsolutePath());
    
        Path winp1 = win1.toPath();
        Path winp2 = win2.toPath();
        Path winp3 = win3.toPath();
    
        System.out.println("1.toPath = " + winp1.toString());
        System.out.println("2.toPath = " + winp2.toString());
        System.out.println("3.toPath = " + winp3.toString());
    
        System.out.println("1.toUri = " + winp1.toUri());
        System.out.println("2.toUri = " + winp2.toUri());
        System.out.println("3.toUri = " + winp3.toUri());
    
        System.out.println("1.toAbsolutePath = " + winp1.toAbsolutePath());
        System.out.println("2.toAbsolutePath = " + winp2.toAbsolutePath());
        System.out.println("3.toAbsolutePath = " + winp3.toAbsolutePath());
        
        boolean winp1Exists = Files.exists(winp1);
        boolean winp2Exists = Files.exists(winp2);
        boolean winp3Exists = Files.exists(winp3);
        
        System.out.println("1) "+winp1+".exists = " + winp1Exists);
        System.out.println("2) "+winp2+".exists = " + winp2Exists);
        System.out.println("3) "+winp3+".exists = " + winp3Exists);
        
        if(winp1Exists && winp2Exists)
            System.out.println("isSameFile(1,2) = " + Files.isSameFile(winp1, winp2));
        if(winp2Exists && winp3Exists)
            System.out.println("isSameFile(2,3) = " + Files.isSameFile(winp2, winp3));
        if(winp3Exists && winp1Exists)
            System.out.println("isSameFile(3,1) = " + Files.isSameFile(winp3, winp1));
    
        System.out.println("1.toRealPath = " + winp1.toRealPath());
        System.out.println("2.toRealPath = " + winp2.toRealPath());
        System.out.println("3.toRealPath = " + winp3.toRealPath());
    }
}
