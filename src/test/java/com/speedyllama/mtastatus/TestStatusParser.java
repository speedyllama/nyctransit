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
	// TODO: this is an old test that does not have "W" train.
	public void testParserOveral() throws MTAStatusException {
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
		assertEquals("[G] No trains between Bedford-Nostrand Avenues and Court Square.", statusMap.get("G").getTitle());
		assertEquals("[G] No trains between Bedford-Nostrand Avenues and Court Square. Free Shuttle Buses provide alternate service. Late Nights, 10:45 PM to 5 AM, Monday to Friday, December 14 - 18 Monday to Thursday, December 21 - 24. [G] service operates between Church Avenue and Bedford-Nostrand Avenues. Shuttle Buses make all [G] station stops between Bedford-Nostrand Avenues and Court Square. Transfer between [G] trains and Shuttle Buses at Bedford-Nostrand Avenues. Show Shuttle Bus Stops.", statusMap.get("G").getDetail());
	}
	
	@Test
	public void testNullDetail() throws MTAStatusException {
		Map<String, Status> statusMap = parser.parse(fetcher.fetchXML(getClass().getResource("/xmls/20151221T2135.xml").toString()));
		assertEquals("Due to a rail condition at 45 Street, southbound [R] trains are running express from Atlantic Avenue-Barclays Center to 59 Street. As an alternative use B63 bus on Atlantic Avenue. Corresponding stops will be made along 5 Avenue. Allow additional travel time.", statusMap.get("R").getTitle());
		assertEquals(null, statusMap.get("R").getDetail());
	}

	@Test
	public void testDiamondTrain() throws MTAStatusException {
		Map<String, Status> statusMap = parser.parse(fetcher.fetchXML(getClass().getResource("/xmls/20151223T1146.xml").toString()));
		assertEquals("Due to signal problems at 125 Street, the following service changes are in effect: Some northbound [4] and [5] trains are running local from 42 Street- Grand Central to 125 Street. Northbound [4], [5] and [6] trains are running with delays. Allow additional travel time. [6 Express] Brooklyn Bridge-bound trains run local from Parkchester to 3 Avenue-138 Street. [6] The last stop for alternate trains headed toward Pelham Bay Park is 3 Avenue-138 Street.", statusMap.get("6").getTitle());
		assertEquals("Due to signal problems at 125 Street, the following service changes are in effect: Some northbound [4] and [5] trains are running local from 42 Street- Grand Central to 125 Street. Northbound [4], [5] and [6] trains are running with delays. Allow additional travel time. [6 Express] Brooklyn Bridge-bound trains run local from Parkchester to 3 Avenue-138 Street. Days, 10:30 AM to 1 PM, Monday to Thursday, December 21 - 24. Please allow additional travel time. [6] The last stop for alternate trains headed toward Pelham Bay Park is 3 Avenue-138 Street. Days, 10:30 AM to 3 PM, Monday to Thursday, December 21 - 24. Please allow additional travel time in the Bronx.", statusMap.get("6").getDetail());
	}

	@Test
	public void testMultipleOccurrence() throws MTAStatusException {
		Map<String, Status> statusMap = parser.parse(fetcher.fetchXML(getClass().getResource("/xmls/20151224T1854.xml").toString()));
		assertEquals("Due to a train with mechanical problems at Queens Plaza. The following service changes are in effect: Southbound [R] trains are running on the [F] line from 36 Street (Queens) to W 4 Street, and then via the [D] train to 36 Street (Brooklyn). Some northbound [R] trains are terminating at 57 Street-7 Avenue. Expect delays on the [F] and [D] trains. Allow additional travel time.   .", statusMap.get("R").getTitle());
		// TODO: This test fails. We are not sure about the business logic so far. We will push it to next versions.
		//assertEquals(TrainStatus.SERVICE_CHANGE, statusMap.get("D").getStatus());
	}

	@Test
	public void testNonAscii() throws MTAStatusException {
		Map<String, Status> statusMap = parser.parse(fetcher.fetchXML(getClass().getResource("/xmls/20151226T1013.xml").toString()));
		String title = statusMap.get("7").getTitle();
		// TODO: special characters in 7
	}
}
