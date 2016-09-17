package com.flytxt.parser.store;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flytxt.parser.marker.Marker;

public class Store {

    private RandomAccessFile channel;

    private final byte csv = (byte) ',';

    private final byte[] newLine = System.lineSeparator().getBytes();

    private int status;

    private final String fileName;

    private final String[] headers;

    private IOException e;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Store(final String file, final String... headers) {
        this.fileName = file;
        this.headers = headers;
        createFile();
    }

    private void createFile() {
        Path fileNameP = Paths.get(fileName);
        if (!Files.exists(fileNameP)) {
            final Path folder = fileNameP.getParent();
            try {
                Files.createDirectories(folder);
            } catch (final IOException e) {
                status = -1;
                this.e = e;
                logger.debug("could not create folder @ " + folder.toString());
                return;
            }
        }
        final String[] tmp = fileName.split("\\.");
        final String name = tmp[0] + System.currentTimeMillis() + "." + tmp[1];
        fileNameP = Paths.get(name);
        try {
            channel = new RandomAccessFile(fileNameP.toString(), "rw");
            for (final String aheader : headers) {
                channel.write(aheader.getBytes());
                channel.write(csv);
            }
            channel.write(newLine);
            logger.debug("file created @ " + fileNameP.toString());
        } catch (final IOException e) {
            logger.debug("could not create file @ " + fileNameP.toString(), e);
            status = -1;
            this.e = e;
        }
    }

    public void save(final byte[] data, final Marker... markers) throws IOException {
        if (status == -1) {
            throw e;
        }
        try {
            for (final Marker aMarker : markers) {
                channel.write(data, aMarker.index, aMarker.length);
                channel.write(csv);
            }
            channel.write(newLine);
        } catch (final IOException e) {
            createFile();
            save(data, markers);
        }
    }

    public void done() throws IOException {
        channel.close();
    }
}