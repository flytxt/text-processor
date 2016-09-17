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

    public void tt() {
        try {
            final File f = new File(new URI("http://localhost:9000/getJar?host=demo"));
            final JarFile jf = new JarFile(f);
            final Enumeration<JarEntry> entries = jf.entries();
            while (entries.hasMoreElements()) {
                System.out.println(entries.nextElement().getName());
            }
        } catch (final URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void ty() {
        final ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

        final List<MediaType> supportedApplicationTypes = new ArrayList<MediaType>();
        final MediaType pdfApplication = new MediaType("application", "java-archive");
        supportedApplicationTypes.add(pdfApplication);

        byteArrayHttpMessageConverter.setSupportedMediaTypes(supportedApplicationTypes);
        final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(byteArrayHttpMessageConverter);
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);

        final Object result = restTemplate.getForObject("http://localhost:9000/getJar?host=demo", byte[].class, "1");
        final byte[] resultByteArr = (byte[]) result;

    }

    public void tt1() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        final HttpHeaders headers = new HttpHeaders();
        headers.clear();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Accept-Encoding", "gzip, deflate, sdch");
        final HttpEntity<String> entity = new HttpEntity<String>(headers);

        final ResponseEntity<byte[]> response = restTemplate.exchange("http://localhost:9000/getJar?host=demo", HttpMethod.GET, entity, byte[].class, "1");

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            try (FileOutputStream output = new FileOutputStream(new File("filename.jar"))) {
                output.write(response.getBody());
            } catch (final Exception e) {
                // TODO: handle exception
            }
        }
    }

}