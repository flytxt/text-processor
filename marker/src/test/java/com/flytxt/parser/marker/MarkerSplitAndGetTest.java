package com.flytxt.parser.marker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MarkerSplitAndGetTest {

    private final MarkerFactory mf = new MarkerFactory();

    @Test
    public void test() {
        final String strb = "a,bb,c,d";
        final byte[] b = strb.getBytes();
        final int get = 1;
        final int javaIndex = get - 1;

        final String str = ",";
        final Marker line = mf.create(0, b.length - 1);
        final byte[] token = TokenFactory.create(str);
        final Marker ms = line.splitAndGetMarker(b, token, get, mf);
        final String splits[] = strb.split(str);
        // for user index starts @ 1
        if (!splits[javaIndex].equals(ms.toString(b))) {
            assertEquals(splits[javaIndex], ms.toString(b));
        }
    }

    @Test
    public void test1() {
        final String strb = "c,d";
        final byte[] b = strb.getBytes();
        final String str = ",";
        final String splits[] = strb.split(str);
        final int get = splits.length;
        final int javaIndex = get - 1;

        final Marker line = mf.create(0, b.length - 1);
        final byte[] token = TokenFactory.create(str);
        final Marker ms = line.splitAndGetMarker(b, token, get, mf);

        // for user index starts @ 1
        if (!splits[javaIndex].equals(ms.toString(b))) {
            assertEquals(splits[javaIndex], ms.toString(b));
        }
    }

    @Test
    public void test2() {
        final String strb = ",c,d";
        final byte[] b = strb.getBytes();
        final String str = ",";
        final String splits[] = strb.split(str);
        final int get = 1;
        final int javaIndex = get - 1;

        final Marker line = mf.create(0, b.length - 1);
        final byte[] token = TokenFactory.create(str);
        final Marker ms = line.splitAndGetMarker(b, token, get, mf);

        // for user index starts @ 1
        if (!splits[javaIndex].equals(ms.toString(b))) {
            assertEquals(splits[javaIndex], ms.toString(b));
        }
    }

    @Test
    public void test3() {
        final String strb = ",c,d,";
        final byte[] b = strb.getBytes();
        final String str = ",";
        final String splits[] = strb.split(str);
        final int get = splits.length;
        final int javaIndex = get - 1;

        final Marker line = mf.create(0, b.length - 1);
        final byte[] token = TokenFactory.create(str);
        final Marker ms = line.splitAndGetMarker(b, token, get, mf);

        // for user index starts @ 1
        if (!splits[javaIndex].equals(ms.toString(b))) {
            assertEquals(splits[javaIndex], ms.toString(b));
        }
    }
}
