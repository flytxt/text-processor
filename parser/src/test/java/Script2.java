import java.util.ArrayList;
import com.flytxt.parser.marker.*;
import com.flytxt.parser.marker.process.Store;

import java.io.IOException;
public  class Script2 implements LineProcessor{
//ivate Store wcStore = new Store("/tmp/out/wc.csv");

private final Marker line = new Marker();
private String currentFileName;
private final byte[] token0x2C = new byte[]{0x2C};
public String getFolder(){ return"/tmp/input";}
public final void done() throws IOException{
//Store.done();
}
public final void setInputFileName(String currentFileName){ this.currentFileName = currentFileName;}
public final void process(byte[] data, int lineSize, MarkerFactory mf) throws IOException{
line.index = 0;
line.length = lineSize;
ArrayList<Marker> all = line.splitAndGetMarkers( data, token0x2C, mf);
Marker country = all.get(2);
Marker club = all.get(6);

//Store.save( data, club,country);

}}