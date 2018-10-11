package cn.itacast_01;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

public class MapMemery {
	public static void main(String[] args) throws IOException {
		File f = new File("e:/sf.txt");
		RandomAccessFile fis = new RandomAccessFile(f, "rw");
		MappedByteBuffer buf =  fis.getChannel().map(MapMode.READ_WRITE,0, 10);
		System.out.println((char)buf.get(0));
		System.out.println((char)buf.get(1));
		System.out.println((char)buf.get(2));
	    buf.put(0,(byte)'x');
	    System.out.println((char)buf.get(0));
		
		fis.close();
	}
}
