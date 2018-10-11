package cn.itacast_01;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class DeleteOffheap {
	public static void main(String[] args) throws Exception {
		 ByteBuffer buf=ByteBuffer.allocateDirect(1024*1024*1024);
		Class clazz = Class.forName("java.nio.DirectByteBuffer");
		Field f = clazz.getDeclaredField("cleaner");
		f.setAccessible(true);
		Object obj = f.get(buf);//得到cleaner方法
		Class cla = Class.forName("sun.misc.Cleaner");
		
		Method m = cla.getDeclaredMethod("clean");
		m.invoke(obj);
		
	}
}
