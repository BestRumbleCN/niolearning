package pri.fly.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOSocketServer {
	
	private InetSocketAddress socketAddress = new InetSocketAddress("localhost", 8090);
	
	private Selector selector;
	
	private void startServer() throws IOException{
		this.selector = Selector.open();
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		channel.socket().bind(socketAddress);
		channel.register(selector, SelectionKey.OP_ACCEPT);
		while(true){
			this.selector.select();
			Iterator keys = this.selector.selectedKeys().iterator();
			while(keys.hasNext()){
				SelectionKey key = (SelectionKey) keys.next();
				keys.remove();
				if(!key.isValid()){
					continue;
				}
				if(key.isAcceptable()){
					this.accept(key);
				}
				if(key.isReadable()){
					this.read(key);
				}
				if(key.isWritable()){
					//this.write(key);
				}
			}
		}
	}
	
	private void accept(SelectionKey key) throws IOException{
		ServerSocketChannel ServerChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = ServerChannel.accept();
		channel.configureBlocking(false);
		Socket socket = channel.socket();
		SocketAddress address = socket.getRemoteSocketAddress();
		System.out.println("Connected to : "+ address);
		channel.register(selector, SelectionKey.OP_READ);
	}
	
	private void read (SelectionKey key) throws IOException{
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int numRead = channel.read(buffer);
		if(numRead == -1){
			return;
		}
		
		byte[] data = new byte[numRead];
		System.arraycopy(buffer.array(), 0, data, 0, numRead);
		System.out.println("Got :" + new String(data));
		//channel.register(selector, SelectionKey.OP_WRITE);
		ByteBuffer buffer2 = ByteBuffer.wrap("return 1111".getBytes());
		channel.write(buffer);
	}
	
	private void write (SelectionKey key) throws IOException{
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.wrap("return 1111".getBytes());
		channel.write(buffer);
		Socket socket = channel.socket();
		SocketAddress remoteAdd = socket.getRemoteSocketAddress();
		//System.out.println("Connection closed by client:" + remoteAdd);
		//channel.close();
		//key.cancel();
	}
	
	public static void main(String[] args) throws IOException {
		new NIOSocketServer().startServer();
	}
}
