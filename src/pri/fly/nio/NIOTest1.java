package pri.fly.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOTest1 {
	public static void main(String[] args) throws IOException {
		RandomAccessFile file = new RandomAccessFile(
				"/Users/fly_pc/Documents/github/niolearning/resources/1.txt",
				"rw");
		RandomAccessFile file2 = new RandomAccessFile(
				"/Users/fly_pc/Documents/github/niolearning/resources/2.txt",
				"rw");
		FileChannel channel = file.getChannel();
		FileChannel channel2 = file2.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(48);
		int bufferRead = channel.read(buffer);
		while (bufferRead != -1) {
			System.out.println("Read " + buffer);
			buffer.flip();
			System.out.println("Read " + buffer);
			channel2.write(buffer);
			buffer.clear();
			bufferRead = channel.read(buffer);
		}
		
		file.close();
	}
}
