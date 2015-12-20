package com.speedyllama.mtastatus;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestStatusParser {
	private StatusParser parser;
	private XMLFetcher fetcher;
	
	@Before
	public void init() {
		this.parser = new StatusParser();
		this.fetcher = new PlainXMLFetcher();
	}

	private void assertGoodService(String trains, Map<String, Status> statusMap) {
		for (String train : trains.split("\\|")) {
			assertSame(statusMap.get(train).getStatus(), TrainStatus.GOOD_SERVICE);
		}
	}
	
	@Test
	public void test() throws MTAStatusException {
		Map<String, Status> statusMap = parser.parse(fetcher.fetchXML(getClass().getResource("/xmls/20151217T2257.xml").toString()));
		assertGoodService("1|2|3|4|5|6|7|A|C|B|J|Z|L|N|Q|R|S|SIR", statusMap);
		assertSame(statusMap.get("E").getStatus(), TrainStatus.PLANNED_WORK);
		// Two alerts combined
		assertEquals("[E] Jamaica Center-bound trains run local from Queens Plaza to 71 Av. [E] World Trade Center-bound trains run local from 71 Av to Queens Plaza. ", statusMap.get("E").getTitle());
		assertSame(TrainStatus.DELAYS, statusMap.get("D").getStatus());
		assertEquals("Due to ongoing signal problems at Bay 50 St, southbound [D] trains are running with delays. Allow additional travel time.. ", statusMap.get("D").getTitle());
		assertSame(TrainStatus.PLANNED_WORK, statusMap.get("F").getStatus());
		assertEquals("[F] Trains run local in both directions between 21 St-Queensbridge and 71 Av. ", statusMap.get("F").getTitle());
		assertSame(TrainStatus.PLANNED_WORK, statusMap.get("M").getStatus());
		assertSame(TrainStatus.PLANNED_WORK, statusMap.get("G").getStatus());
		assertEquals("[G] No trains between Bedford-Nostrand Avs and Court Sq. ", statusMap.get("G").getTitle());
	}
}
