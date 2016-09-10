package com.flytxt.utils.parser;
import java.util.ArrayList;
import com.flytxt.utils.processor.Store;
import com.flytxt.utils.parser.Marker;
import java.io.IOException;
public  class Script implements com.flytxt.utils.processor.LineProcessor{
private Store folder1Store = new Store("/out/folder1");

private final Marker line = new Marker();
private final byte[] token0x2A = new byte[]{0x2A};
private final byte[] token0x7C = new byte[]{0x7C};
private final byte[] token0x2C = new byte[]{0x2C};
public String getFolder(){ return"home/dk";}
public final void done() throws IOException{
folder1Store.done();
}
public final void process(byte[] data, int lineSize) throws IOException{
line.index = 0;
line.index = lineSize;
ArrayList<Marker> slices = line.splitAndGetMarkers( data, token0x2C);
Marker aon = slices.get(2).splitAndGetMarker( data, token0x7C,1);
ArrayList<Marker> newslices = line.splitAndGetMarkers( data, token0x2A);
Marker mou = newslices.get(1).splitAndGetMarker( data, token0x2A,1);

folder1Store.save( data, aon,mou );

}}