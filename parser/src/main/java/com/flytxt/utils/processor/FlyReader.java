package com.flytxt.utils.processor;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flytxt.parser.marker.MarkerFactory;

public class FlyReader {

    private final String folder;

    private final LineProcessor lp;

    private boolean stopRequested;

    byte[] eol = System.lineSeparator().getBytes();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public FlyReader(final String folder, final LineProcessor lp) {
        this.lp = lp;
        this.folder = folder;
        logger.debug("file reader @ " + folder);
    }

    public void start() {
        logger.debug("Startring file reader @ "+ folder);

        final byte[] data = new byte[6024];
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folder))) {
            final MarkerFactory mf = new MarkerFactory();
            for (final Path path : directoryStream) {
                final RandomAccessFile file= new RandomAccessFile(path.toString(), "rw");
                try(FileChannel channel = file.getChannel()){
                    try {
                        lp.setInputFileName(path.getFileName().toString());
                        processFile(data, path, file, channel, mf);
                        if(stopRequested){
                            break;
                        }
                    } catch (final OverlappingFileLockException e) {
                        e.printStackTrace();;
                    } finally {
                    }
                }catch (final Exception e) {
                    e.printStackTrace();;

                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        logger.debug("--done--");
    }

    private void processFile(final byte[] data, final Path path, final RandomAccessFile file, final FileChannel channel, final MarkerFactory mf) throws IOException {

        readLines(file, data, mf);
        lp.done();
        file.close();
        Files.delete(path);
    }

    private final void readLines(final RandomAccessFile file, final byte[] data, final MarkerFactory mf) throws IOException {
        boolean match = false;

        int i = 0;
        int readCnt;
        int j = 0;
        final long t1 = System.currentTimeMillis();
        do {
            readCnt = file.read();
            data[i] = (byte) readCnt;
            if (readCnt == 10) {
            }
            if (j < eol.length && readCnt != -1 && (byte) readCnt == eol[j]) {
                match = true;
                j++;
            }
            if (eol.length == j && match) {
                try {
                    lp.process(data, i, mf);
                } catch (final IndexOutOfBoundsException e) {
                    logger.debug("could not process : " + new String(data, 0, i) + " \n cause:" + e.getMessage());
                }
                i = 0;
                j = 0;
                mf.reclaim();
                continue;
            }
            i++;
        } while (readCnt != -1);
        final long t2 = System.currentTimeMillis();
        logger.debug("total time taken: " + (t2 - t1) / 1000);
    }

    public void stop() {
        stopRequested = true;
    }
}