package cn.itcast_02;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Client {
	public static void main(String[] args) throws Exception {
		//开一个挑选器
		Selector sel  = Selector.open();
		ByteBuffer buf = ByteBuffer.allocate(1024*8); 
		//开启客户端socket通道
		SocketChannel sc = SocketChannel.open();
		sc.connect(new InetSocketAddress("localhost", 9999));
		//设置非阻塞
		sc.configureBlocking(false);
		//字符流
		//注册一个挑选器
		sc.register(sel, SelectionKey.OP_READ);
		//先写一些数据
		new Thread() {
			int i = 0;
			
			public void run() {
				ByteBuffer buf2 = ByteBuffer.allocate(1024*8);
				while(true) {
					try {
					    buf2.put(("Tom"+(i++)).getBytes());
					    buf2.flip();
					    sc.write(buf2);
					    buf2.clear();
					    Thread.sleep(500);
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
		}
		}.start();
		
		
		while(true) {
			sel.select();//挑选感兴趣的key
			// 本质是sc sel.selectedKeys().iterator().next();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while(sc.read(buf)>0) {
				buf.flip();
				baos.write(buf.array(),0,buf.limit());//写到字符流中
				buf.clear();
			}
			System.out.println(new String(baos.toByteArray()));
			sel.selectedKeys().clear();
		}
	}
}
