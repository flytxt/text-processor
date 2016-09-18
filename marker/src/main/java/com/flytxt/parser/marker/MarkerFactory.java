package com.flytxt.parser.marker;

import java.util.ArrayDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarkerFactory {

    private ArrayDeque<Marker> home = new ArrayDeque<Marker>();

    private ArrayDeque<Marker> roam = new ArrayDeque<Marker>();

    private ArrayDeque<FlyList<Marker>> homeList = new ArrayDeque<FlyList<Marker>>();

    private ArrayDeque<FlyList<Marker>> roamList = new ArrayDeque<FlyList<Marker>>();

    private int reused;

    private int created;

    private int reusedList;

    private int createdList;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private int listSize;

    public Marker create(final int lastIndex, final int i) {
        Marker m = null;
        try {
            m = home.pop();
            reused++;
        } catch (final Exception e) {
            m = new Marker();
            created++;
        }
        m.index = lastIndex;
        m.length = i;
        roam.push(m);
        return m;
    }

    public void reclaim() {
        final ArrayDeque<Marker> tmp = home;
        home = roam;
        roam = tmp;

        final ArrayDeque<FlyList<Marker>> tmpList = homeList;
        homeList = roamList;
        roamList = tmpList;
    }

    public void printStat() {
        logger.debug("Markers reused: " + reused + " created: " + created);
        logger.debug("List reused: " + reusedList + " created:" + createdList);
    }

    public FlyList<Marker> getArrayList() {
        FlyList<Marker> list;
        try {
            list = homeList.pop();
            list.clear();
            reusedList++;
        } catch (final Exception e) {
            list = new FlyList<Marker>(listSize);
            createdList++;
        }
        roamList.push(list);
        return list;
    }

	public void setMaxListSize(int maxListSize) {
		listSize = maxListSize;
		
	}
}