package com.flytxt.utils.parser.p;import java.util.ArrayList;
import com.flytxt.utils.processor.Store;
import com.flytxt.utils.parser.Marker;
import com.flytxt.utils.parser.MarkerFactory;

import java.io.IOException;
public  class scriptFlyLineProcessor implements com.flytxt.utils.processor.LineProcessor{
private Store folder1Store = new Store("/out/folder1");

public String getFolder(){ return"home/dk";}
public void done() throws IOException{
folder1Store.done();
}
public void process(byte[] data, int lineSize) throws IOException{
	Marker line = MarkerFactory.create(0, lineSize);
ArrayList<Marker> slices = line.splitAndGetMarkers( data, new byte[]{0x2C});
Marker aon = slices.get(2).splitAndGetMarker( data, new byte[]{0x7C},1);
ArrayList<Marker> newslices = line.splitAndGetMarkers( data, new byte[]{0x2A});
Marker mou = newslices.get(1).splitAndGetMarker( data, new byte[]{0x2A},1);

folder1Store.save( data, aon,mou );

}}