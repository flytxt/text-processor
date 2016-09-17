package com.flytxt.utils.parser;
import java.util.ArrayList;
import com.flytxt.parser.store.*;
import com.flytxt.parser.marker.*;
import java.io.IOException;
public  class Script implements LineProcessor{
private Store wcStore = new Store("/tmp/out/wc.csv");

private final Marker line = new Marker();
private String currentFileName;
private final byte[] token0x2C = new byte[]{0x2C};
public String getFolder(){ return"/tmp/input";}
public String getFilter(){ return null ;}
public final void done() throws IOException{
wcStore.done();
}
public final void setInputFileName(String currentFileName){ this.currentFileName = currentFileName;}
public final void process(byte[] data, int lineSize, MarkerFactory mf) throws IOException{
line.index = 0;
line.length = lineSize;
List<Marker> all = line.splitAndGetMarkers( data, token0x2C, mf);
Marker country = all.get(2);
Marker club = all.get(6);

wcStore.save( data, club,country);

}}
~                                              