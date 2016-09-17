package com.flytxt.parser.marker;


public class Marker {

    public int index;

    public int length;

    public Marker splitAndGetMarker(final byte[] data, final byte[] token, final int indexOfMarker, final MarkerFactory mf) {

        int count = 1, lastIndex = index, currentIndex = index, tokenIndex;

        while (currentIndex - index <= length) {
            for (tokenIndex = 0; tokenIndex < token.length && token[tokenIndex] == data[currentIndex + tokenIndex]; tokenIndex++) { // loop to check if token is present at position i
                ;
            }
            if (tokenIndex == token.length) { // true if token found at currentIndex
                if (indexOfMarker == count++) { // true if correct marker is found
                    return mf.create(lastIndex, currentIndex - lastIndex);
                }
                currentIndex = currentIndex + token.length;
                lastIndex = currentIndex;
            } else {
                currentIndex++;
            }
        }

        if (lastIndex < length + 1 && indexOfMarker == count) { // true if required marker is the last
            return mf.create(lastIndex, this.length - (lastIndex - 1));
        }
        return null;
    }

    public FlyList<Marker> splitAndGetMarkers(final byte[] data, final byte[] token, final MarkerFactory mf) {

        final FlyList<Marker> markers = mf.getArrayList();
        int currentIndex = index, lastIndex = index, tokenIndex;

        while (currentIndex - index <= length) {
            for (tokenIndex = 0; tokenIndex < token.length && token[tokenIndex] == data[currentIndex + tokenIndex]; tokenIndex++) { // loop to check if token is present at position i
                ;
            }
            if (tokenIndex == token.length) {
                markers.add(mf.create(lastIndex, currentIndex - lastIndex));
                currentIndex = currentIndex + tokenIndex;
                lastIndex = currentIndex;
            } else {
                currentIndex++;
            }
        }
        if (lastIndex < length + 1) {
            markers.add(mf.create(lastIndex, this.length - (lastIndex - 1)));
        }
        return markers;
    }

    public String toString(final byte[] b) {
        return new String(b, index, length);
    }
}
