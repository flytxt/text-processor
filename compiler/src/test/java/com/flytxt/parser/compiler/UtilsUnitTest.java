package com.flytxt.parser.compiler;

import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.jar.JarOutputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsUnitTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testCompileGivenScript() throws Exception {
        final Utils utils = new Utils();
        final String scr = "/tmp/scripts/demo/script2.pl";
        final String java = utils.createJavaContent(scr);
        final String createFile = utils.createFile("/tmp/java/demo/", java, "script2.java");
        utils.complie(createFile, "/tmp/java/demo");
    }

    @Test
    public void testBadClass() throws Exception {
        final Utils utils = new Utils();
        final String src = "/Users/arunsoman/git/text-processor/compiler/src/test/resources/compiler/Bad.java";
        final String dest = "/tmp/";
        final String res = utils.complie(src, dest);
        logger.debug(res);
        if (res == null) {
            fail("sshould have produced  error text");
        }
    }

    @Test
    public void testGoodClass() throws Exception {
        final Utils utils = new Utils();
        final String src = "/tmp/java/demo/com/flytxt/utils/parser/Script2.java";
        final String dest = "/tmp/classes";
        final String res = utils.complie(src, dest);
        if (res != null) {
            logger.debug(res);
            fail("should not have produced  error text");
        }
    }

    @Test
    public void testDirListing() {
        final String loc = "/Users/arunsoman/git/text-processor/parser/target/classes";
        final Utils utils = new Utils();
        try {
            final FileOutputStream fout = new FileOutputStream("/tmp/test.jar");
            final JarOutputStream jarOut = new JarOutputStream(fout);
            utils.listFiles(Paths.get(loc), jarOut, loc.length() + 1, true);
            jarOut.close();
            fout.close();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
