package com.flytxt.parser.processor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class FunctionalTest {

	public void tt(){
		try {
			File f = new File(new URI("http://localhost:9000/getjar?host=demo"));
			JarFile jf = new JarFile(f);
			Enumeration<JarEntry> entries = jf.entries();
			while(entries.hasMoreElements()){
				System.out.println(entries.nextElement().getName());
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void ty(){
		ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

		List<MediaType> supportedApplicationTypes = new ArrayList<MediaType>();
		MediaType pdfApplication = new MediaType("application","java-archive");
		supportedApplicationTypes.add(pdfApplication);

		byteArrayHttpMessageConverter.setSupportedMediaTypes(supportedApplicationTypes);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(byteArrayHttpMessageConverter);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(messageConverters);

		Object result = restTemplate.getForObject("http://localhost:9000/getjar?host=demo", byte[].class, "1");
		byte[] resultByteArr = (byte[])result;

	}
	public void tt1(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());    
		HttpHeaders headers = new HttpHeaders();
		headers.clear();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Accept-Encoding","gzip, deflate, sdch");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<byte[]> response = restTemplate.exchange(
				"http://localhost:9000/getjar?host=demo", HttpMethod.GET, entity, byte[].class, "1");
		

		if(response.getStatusCode().equals(HttpStatus.OK))
		        {       
		                try(FileOutputStream output = new FileOutputStream(new File("filename.jar"))){
		                output.write(response.getBody());
		                }catch (Exception e) {
							// TODO: handle exception
						}
		        }
	}
	
}