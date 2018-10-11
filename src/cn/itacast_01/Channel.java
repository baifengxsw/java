package cn.itacast_01;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Channel {
	public static void main(String[] args) throws IOException {
		//输入
		FileInputStream fis = new FileInputStream("e:/sf.txt");
		//得到文件通道
		FileChannel fcin = fis.getChannel();
		//输出 
		FileOutputStream fos = new FileOutputStream("e:/gh.txt");
		FileChannel fcout = fos.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(1024*8);
		while(fcin.read(buf)!=-1) {
			buf.flip();
			fcout.write(buf);
			buf.clear();
		}
		fcin.close();
		fcout.close();
		System.out.println("over");
		
		
	}
	
}
