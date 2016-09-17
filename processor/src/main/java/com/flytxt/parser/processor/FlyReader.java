package com.flytxt.parser.processor;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import javax.annotation.PreDestroy;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.flytxt.parser.marker.LineProcessor;
import com.flytxt.parser.marker.MarkerFactory;

@Component
@Scope("prototype")
public class FlyReader implements Callable<FlyReader> {

    private String folder;

    private LineProcessor lp;

    private boolean stopRequested;

    public enum Status {
        RUNNING, TERMINATED, SHUTTINGDOWN
    }

    @Getter
    private Status status;

    byte[] eol = System.lineSeparator().getBytes();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void set(final String folder, final LineProcessor lp) {
        this.lp = lp;
        this.folder = folder;
        logger.debug("file reader @ " + folder);
    }

    public void run() {
    	Path folderP =Paths.get(folder);
    	if(!Files.exists(folderP))
			try {
				Files.createDirectories(folderP);
			} catch (IOException e1) {
				logger.info("could not create input folder, stoppin this FlyReader ",e1);
				stopRequested = true;
			}
        logger.debug("Starting file reader @ " + folder);
        final byte[] data = new byte[6024];
        final MarkerFactory mf = new MarkerFactory();
        while (!stopRequested) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folder))) {
                for (final Path path : directoryStream) {
                    final RandomAccessFile file = new RandomAccessFile(path.toString(), "rw");
                    logger.debug("picked up " + path.toString());
                     try {
                            lp.setInputFileName(path.getFileName().toString());
                            processFile(data, path, file, mf);
                            if (stopRequested) {
                                logger.debug("shutting down Wroker @ :"+folder);
                                break;
                            }
                        } catch (final OverlappingFileLockException e) {
                            logger.error("Couldnot process "+path.toString(),e);
                        } finally {
                        	file.close();
                        }
                }
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
        logger.debug("Worker down " + folder);
    }

    private void processFile(final byte[] data, final Path path, final RandomAccessFile file, final MarkerFactory mf) throws IOException {

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
        mf.printStat();
        logger.debug("total time taken: " + (t2 - t1));
    }

    @PreDestroy
    public void stop() {
        stopRequested = true;
    }

    @Override
    public FlyReader call() throws Exception {
        run();
        return this;
    }

    public boolean canProcess(final String folderName, final String fileName) {
        logger.debug("check " + folderName + " & " + lp.getFolder());
        if (lp.getFolder().equals(folderName)) {
            final String regex = lp.getFilter();
            if (regex == null) {
                return true;
            }
            final Pattern pattern = Pattern.compile(regex);
            return pattern.matcher(fileName).find();
        } else {
            return false;
        }
    }
}