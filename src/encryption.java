import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.xml.bind.ParseConversionEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import config.Configurations;


public class encryption {
//	Decryption path
	
	public String decrypt(byte[] b)
	{
		Configurations CF = new Configurations();
//		Defines the decryption value of the byte
		//The 4 lines below needs to work later on, but for now, it will be hardcode
		//System.out.println(CF.getFfcryptkey());
//		System.out.println(CF.getFfcryptkey());
//		String crypKey = CF.getFfcryptkey();
//		System.out.println(crypKey);
//		double gladKo = Double.parseDouble(crypKey);
		byte ff = (byte) 3.1740;
//		Generates for loop containing decryption value
		for(int i = 0 ; i<b.length ; i++)
		{
			b[i] = (byte)(b[i]^ff);
		}
//		Generates new String without any white spaces following or leading
		String encrypted = new String(b).trim();
//		Returns decrypted String
		return encrypted;
	
//	public String decrypt(InputStream is) throws IOException
//	{
//		Configurations CF = new Configurations();
//		Gson gson = new GsonBuilder().create();
////		Defines the decryption value of the byte
//		byte[] b = new byte [40000];
//		int count = is.read(b);
//		
//		ByteArrayInputStream bios = new ByteArrayInputStream(b);
//		//The 4 lines below needs to work later on, but for now, it will be hardcode
//		//System.out.println(CF.getFfcryptkey());
////		System.out.println(CF.getFfcryptkey());
////		String crypKey = CF.getFfcryptkey();
////		System.out.println(crypKey);
////		double gladKo = Double.parseDouble(crypKey);
//		byte ff = (byte) 3.1470;
////		Generates for loop containing decryption value
//		for(int i = 0 ; i<b.length ; i++)
//		{
//			b[i] = (byte)(b[i]^ff);
//		}
////		Generates new String without any white spaces following or leading
////		Returns decrypted String
//		
//		String ny = b.toString();
//		
//		System.out.println(ny);
//		
//		String json = gson.toJson(ny);
//		
//		return json;
	}
}
