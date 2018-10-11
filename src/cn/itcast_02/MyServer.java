package cn.itcast_02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.Iterator;

public class MyServer {
	public static void main(String[] args) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//开启ServerSocket 通道
		ServerSocketChannel ssc  = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress("localhost", 9999));
		
		//绑定端口
		//开启挑选器
		Selector sel = Selector.open();
		//设置非阻塞
		ssc.configureBlocking(false);
		//注册
		ssc.register(sel, SelectionKey.OP_ACCEPT);
		//选择socketchannel 和ServerSocketchannel都继承的SelectableChannel
		SelectableChannel sc = null;
		while(true) {
			sel.select();//阻塞的方式
			Iterator<SelectionKey> it=sel.selectedKeys().iterator();//找到所有挑选出来的key并迭代
			while(it.hasNext()) {
				SelectionKey key = it.next();
				try {
					//是可接受的事情
					if(key.isAcceptable()) {
						//确定这是服务器通道
						sc = key.channel(); //就是ssc
						SocketChannel sc0=((ServerSocketChannel)sc).accept();
						sc0.configureBlocking(false);
						sc0.register(sel, SelectionKey.OP_READ);
					}
					if(key.isReadable()){
						SocketChannel sc1 = (SocketChannel)key.channel();
						//先发送hello
						byte [] helloBYs = "hello".getBytes();
						buf.put(helloBYs, 0, helloBYs.length);
						while((sc1.read(buf))!=0) {
							buf.flip();
							sc1.write(buf);
							buf.clear();
						}
					}
				
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					sel.keys().remove(key);
				}
			}
			sel.selectedKeys().clear();
			
		}
		
	}
}
