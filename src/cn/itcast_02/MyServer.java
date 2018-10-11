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
		//����ServerSocket ͨ��
		ServerSocketChannel ssc  = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress("localhost", 9999));
		
		//�󶨶˿�
		//������ѡ��
		Selector sel = Selector.open();
		//���÷�����
		ssc.configureBlocking(false);
		//ע��
		ssc.register(sel, SelectionKey.OP_ACCEPT);
		//ѡ��socketchannel ��ServerSocketchannel���̳е�SelectableChannel
		SelectableChannel sc = null;
		while(true) {
			sel.select();//�����ķ�ʽ
			Iterator<SelectionKey> it=sel.selectedKeys().iterator();//�ҵ�������ѡ������key������
			while(it.hasNext()) {
				SelectionKey key = it.next();
				try {
					//�ǿɽ��ܵ�����
					if(key.isAcceptable()) {
						//ȷ�����Ƿ�����ͨ��
						sc = key.channel(); //����ssc
						SocketChannel sc0=((ServerSocketChannel)sc).accept();
						sc0.configureBlocking(false);
						sc0.register(sel, SelectionKey.OP_READ);
					}
					if(key.isReadable()){
						SocketChannel sc1 = (SocketChannel)key.channel();
						//�ȷ���hello
						byte [] helloBYs = "hello".getBytes();
						buf.put(helloBYs, 0, helloBYs.length);
						while((sc1.read(buf))!=0) {
							buf.flip();
							sc1.write(buf);
							buf.clear();
						}
					}
				
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					sel.keys().remove(key);
				}
			}
			sel.selectedKeys().clear();
			
		}
		
	}
}
