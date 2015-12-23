package com.speedyllama.mtastatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
	public void test1() throws MTAStatusException {
		Map<String, Status> statusMap = parser.parse(fetcher.fetchXML(getClass().getResource("/xmls/20151217T2257.xml").toString()));
		assertGoodService("1|2|3|4|5|6|7|A|C|B|J|Z|L|N|Q|R|S|SIR", statusMap);
		assertSame(statusMap.get("E").getStatus(), TrainStatus.PLANNED_WORK);
		// Two alerts combined
		assertEquals("[E] Jamaica Center-bound trains run local from Queens Plaza to 71 Avenue. [E] World Trade Center-bound trains run local from 71 Avenue to Queens Plaza.", statusMap.get("E").getTitle());
		assertSame(TrainStatus.DELAYS, statusMap.get("D").getStatus());
		assertEquals("Due to ongoing signal problems at Bay 50 Street, southbound [D] trains are running with delays. Allow additional travel time.", statusMap.get("D").getTitle());
		assertSame(TrainStatus.PLANNED_WORK, statusMap.get("F").getStatus());
		assertEquals("[F] Trains run local in both directions between 21 Street-Queensbridge and 71 Avenue.", statusMap.get("F").getTitle());
		assertSame(TrainStatus.PLANNED_WORK, statusMap.get("M").getStatus());
		assertSame(TrainStatus.PLANNED_WORK, statusMap.get("G").getStatus());
		assertEquals("[G] No trains between Bedford-Nostrand Avs and Court Sq.", statusMap.get("G").getTitle());
	}
	
	@Test
	public void test2() throws MTAStatusException {
		Map<String, Status> statusMap = parser.parse(fetcher.fetchXML(getClass().getResource("/xmls/20151221T2135.xml").toString()));
		assertEquals("Due to a rail condition at 45 Street, southbound [R] trains are running express from Atlantic Avenue-Barclays Center to 59 Street. As an alternative use B63 bus on Atlantic Avenue. Corresponding stops will be made along 5 Avenue. Allow additional travel time.", statusMap.get("R").getTitle());
		assertEquals(null, statusMap.get("R").getDetail());
	}
}
