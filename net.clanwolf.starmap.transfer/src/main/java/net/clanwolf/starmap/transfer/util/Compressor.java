package net.clanwolf.starmap.transfer.util;

import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Compressor {

	public static byte[] compress( Object o){
		ObjectOutputStream objectOut = null;
		GZIPOutputStream gzipOut = null;
		ByteArrayOutputStream baos = null;
		byte[] bytes = null;

		try {
			baos = new ByteArrayOutputStream();
//			gzipOut = new GZIPOutputStream(baos);
			gzipOut = new GZIPOutputStream(baos) {
				{
					def.setLevel(Deflater.BEST_COMPRESSION);
				}
			};
			objectOut = new ObjectOutputStream(gzipOut);
			objectOut.writeObject(o);
			gzipOut.finish();

			bytes = baos.toByteArray();

		} catch(IOException e)	{
			e.printStackTrace();
		} finally {
			try {
				objectOut.close();
				gzipOut.close();
				baos.close();
			} catch (IOException e2){
				e2.printStackTrace();
			}
		}

		return bytes;
	}

	public static Object deCompress(byte[] bytes){
		ByteArrayInputStream bais = null;
		GZIPInputStream gzipIn = null;
		ObjectInputStream objectIn = null;
		Object res = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			gzipIn = new GZIPInputStream(bais);
			objectIn = new ObjectInputStream(gzipIn);
			res = objectIn.readObject();

		} catch(IOException | ClassNotFoundException e2)	{
			//NOP
			e2.printStackTrace();
		} finally {
			try {
				gzipIn.close();
				objectIn.close();
			} catch (IOException e2){
				//NOP
				e2.printStackTrace();
			}
		}
		return res;
	}

	public static byte[] convertToByteArray( Object o){
		ObjectOutputStream objectOut = null;
		ByteArrayOutputStream baos = null;
		byte[] bytes = null;

		try {
			baos = new ByteArrayOutputStream();
			objectOut = new ObjectOutputStream(baos);
			objectOut.writeObject(o);

			bytes = baos.toByteArray();

		} catch(IOException e)	{
			e.printStackTrace();
		} finally {
			try {
				objectOut.close();
				baos.close();
			} catch (IOException e2){
				e2.printStackTrace();
			}
		}
		return bytes;
	}
}
