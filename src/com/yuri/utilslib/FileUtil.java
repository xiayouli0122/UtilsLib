package com.yuri.utilslib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class FileUtil {

	/**
	 * get parent path
	 * @param path current file path
	 * @return parent path
	 */
	public static String getParentPath(String path){
		File file = new File(path);
		return file.getParent();
	}
	
	/**
	 * io copy
	 * 
	 * @param srcPath
	 *           source file path
	 * @param desPath
	 *           destination file path
	 * @return
	 * @throws Exception
	 */
	public static void fileStreamCopy(String srcPath, String desPath) throws IOException{
		int copySize = 1024 * 8 * 4;
		File files = new File(desPath);
		FileOutputStream fos = new FileOutputStream(files);
		byte buf[] = new byte[128];
		InputStream fis = new BufferedInputStream(new FileInputStream(srcPath), copySize);
		do {
			int read = fis.read(buf);
			if (read <= 0) {
				break;
			}
			fos.write(buf, 0, read);
		} while (true);
		fis.close();
		fos.close();
	}
	
	/**
	 * bytes tp chars
	 * 
	 * @param bytes
	 * @return
	 */
	public static char[] getChars(byte[] bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		return cb.array();
	}

}
