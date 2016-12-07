package pri.fly.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
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
	
	public static void main(String[] args) throws IOException {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(9999));
		serverSocketChannel.configureBlocking(false);
		while (true) {
			SocketChannel socketChannel = serverSocketChannel.accept();
			if (socketChannel != null) {
				// do something with socketChannel...
			}
		}
	}
}
