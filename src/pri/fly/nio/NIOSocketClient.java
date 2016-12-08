package pri.fly.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOSocketClient {
	public void startClient() throws IOException, InterruptedException {
		InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8090);
		SocketChannel socketChannel = SocketChannel.open(hostAddress);
		System.out.println("Client....started");
		String threadName = Thread.currentThread().getName();
		String[] messages = new String[] { threadName + "send message1",
				threadName + "send message2", threadName + "send message3" };
		for (String message : messages) {
			byte[] bytemsg = message.getBytes();
			ByteBuffer buffer = ByteBuffer.wrap(bytemsg);
			socketChannel.write(buffer);
			System.out.println(message);
			buffer.clear();
			Thread.sleep(500);
		}
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		int size = socketChannel.read(readBuffer);
		while(size != -1){
			readBuffer.flip();
			while(readBuffer.hasRemaining()){
				System.out.print(new String(readBuffer.array()));
			}
			readBuffer.clear();
			size = socketChannel.read(readBuffer);
		}
		socketChannel.close();
	}
	public static void main(String[] args) throws IOException, InterruptedException {
		new NIOSocketClient().startClient();
	}
	
	
}
