package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileUtil {
	
	public static String getFileAsString( String fileName ) throws IOException {
		InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(fileName);

		StringBuilder textBuilder = new StringBuilder();
		try( Reader reader = new BufferedReader(new InputStreamReader(in, Charset.forName(StandardCharsets.UTF_8.name())))) {
			int c = 0;
		    while ((c = reader.read()) != -1) {
		    	textBuilder.append((char) c);
		    }
		}
		return textBuilder.toString();
	}
	

}
