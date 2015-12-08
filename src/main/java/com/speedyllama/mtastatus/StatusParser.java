package com.speedyllama.mtastatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StatusParser {
    private static final String DELIM = "\t";
    private static String LINES = "1|2|3|4|5|6|7|8|9|A|C|E|B|D|F|M|G|J|Z|L|N|Q|R|S|SIR";
    
    private XMLFetcher fetcher;
    private String url;
    
    public static class Alert {
        public String timestamp, line, status, title, detail;

        public Alert(String timestamp, String line, String status, String title, String detail) {
            this.timestamp = timestamp;
            this.line = line;
            this.status = status;
            this.title = title;
            this.detail = detail;
        }
        
        public Alert(String timestamp, String line, String status) {
            this(timestamp, line, status, "", "");
        }
        
        public Alert() {}

        @Override
        public String toString() {
            return timestamp + DELIM + line + DELIM + status + DELIM + title + DELIM + detail;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Alert)) {
                return false;
            }
            Alert other = (Alert)obj;
            return other.line.equals(line) &&
                    other.status.equals(status) &&
                    other.title.equals(title) &&
                    other.detail.equals(detail);
        }
    }

    public StatusParser(XMLFetcher fetcher, String url) {
    	this.fetcher = fetcher;
    	this.url = url;
    }
    public Map<String, String> parse() throws MTAStatusException {
    	InputStream rawXMLStream = fetcher.fetchXML(url);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(rawXMLStream);
            doc.getDocumentElement().normalize();
            
            Map<String, List<Alert>> alertsOfTimestamp = new HashMap<String, List<Alert>>();
            String timestamp = Long.toString((new Date()).getTime());
            
            org.w3c.dom.Node subwayNode = doc.getElementsByTagName("subway").item(0);
            NodeList lines = subwayNode.getChildNodes();
            
            for (int lineIndex = 0; lineIndex < lines.getLength(); lineIndex++) {
                org.w3c.dom.Node lineNode = lines.item(lineIndex);
                if (lineNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    org.w3c.dom.Element lineElem = (org.w3c.dom.Element)lineNode;
                    String name = lineElem.getElementsByTagName("name").item(0).getTextContent();
                    String statusInXML = lineElem.getElementsByTagName("status").item(0).getTextContent();
                    String text = lineElem.getElementsByTagName("text").item(0).getTextContent();
                    
                    if (!"GOOD SERVICE".equalsIgnoreCase(statusInXML)) {
                        List<Alert> alerts = parseText(text);
                        for (Alert tmpAlert : alerts) {
                            String title = tmpAlert.title;
                            String detail = tmpAlert.detail;
                            String status = tmpAlert.status;
                            
                            List<SingleTitle> singleTitles = extractLinesFromTitle(title, name, status);
                            if (!singleTitles.isEmpty()) {
                                for (SingleTitle singleTitle : singleTitles) {
                                    Alert alert = new Alert(timestamp, singleTitle.line, status, singleTitle.title, detail);
                                    addAlert(alert, alertsOfTimestamp);
                                }
                            }
                        }
                    }
                 }
            }
            
            Map<String, String> statusMap = new HashMap<String, String>();
            for (String trainKey : LINES.split("\\|")) {
            	List<Alert> alerts = alertsOfTimestamp.get(trainKey);
            	if (alerts != null && !alerts.isEmpty()) {
            		StringBuilder builder = new StringBuilder();
            		builder.append(trainKey + " train's status is " + alerts.get(0).status + ". ");
            		for (Alert alert : alerts) {
            			builder.append(alert.title).append(". ").append(alert.detail).append(".");
            		}
            		statusMap.put(trainKey, builder.toString());
            	} else {
            		statusMap.put(trainKey, trainKey + "-train is in good service.");
            	}
            }
            return statusMap;
        } catch (IOException ie) {
        	throw new MTAStatusException(ie);
        } catch (ParserConfigurationException pe) {
        	throw new MTAStatusException(pe);
        } catch (SAXException se) {
        	throw new MTAStatusException(se);
        } catch (NullPointerException ne) {
        	throw new MTAStatusException(ne);
        }
    }
    
    private static void addAlert(Alert alert, Map<String, List<Alert>> alertsOfTimestamp) {
        List<Alert> alerts = alertsOfTimestamp.get(alert.line);
        if (alerts == null) {
            alerts = new ArrayList<Alert>();
        }
        alerts.add(alert);
        alertsOfTimestamp.put(alert.line, alerts);
    }

    private Alert buildTempAlert(String status, String title, String detail, boolean hasDetail, StringBuilder builder) {
        if (hasDetail) {
            Element detailElem = Jsoup.parse(builder.toString());
            // Remove tables
            detailElem.select("table").remove();
            detail = detailElem.text();
        } else {
            title = Jsoup.parse(builder.toString()).text();
        }
        Alert alert = new Alert(null, null, status, title, detail);
        return alert;
    }

    private List<Alert> parseText(String html) {
        List<Alert> alerts = new ArrayList<Alert>(); 

        Document doc = Jsoup.parseBodyFragment(html);
        List<Node> nodes = doc.body().childNodes();
        
        String status = "", title = "", detail = "";
        boolean isFirst = true;
        boolean hasDetail = false;
        StringBuilder builder = new StringBuilder();
        for (int nodeIndex = 0; nodeIndex < nodes.size(); nodeIndex++) {
            Node node= nodes.get(nodeIndex);
            String nodeName = node.nodeName();
            if ("span".equals(nodeName)) {
                Element elem = (Element)node;
                String clazz = elem.attr("class");
                if (clazz.indexOf("Title") == 0) {
                    // Previous alert is done. Collect
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        alerts.add(buildTempAlert(status, title, detail, hasDetail, builder));
                        // Reset hasDetail;
                        hasDetail = false;
                        builder = new StringBuilder();
                    }
                    
                    // Extract the new status
                    status = elem.text().toUpperCase();
                }
                // Other spans will be disregarded, like "DateStyle" spans
            } else if ("br".equals(nodeName)) {
                // <br> tags are disregarded.
            } else if ("a".equals(nodeName) && ((Element)node).hasAttr("onclick")) {
                // Title found! Has detail
                hasDetail = true;
                title = ((Element)node).text();
                builder = new StringBuilder(); // Now builder builds detail
            } else {
                builder.append(node.outerHtml());
            }
        }

        alerts.add(buildTempAlert(status, title, detail, hasDetail, builder));
        return alerts;
    }
    
    private static Pattern LINES_PATTERN = Pattern.compile("\\[(" + LINES + ")\\]");
    private static String[] NOT_MULTIPLE_LINES_PATTERN = {
        "provide alternate service",
        "via",
        "replace",
        "] station",
        "] Station"
    };

    // Some titles contain multiple line information. This is to separate them into single lines.
    private static class SingleTitle {
        public String line, title;
        public SingleTitle(String line, String title) {
            this.line = line;
            this.title = title;
        }
    }
    private static List<SingleTitle> extractLinesFromTitle(String title, String expectedLinesStr, String status) {
        Matcher matcher = LINES_PATTERN.matcher(title);

        List<String> lines = new ArrayList<String>();
        Set<String> linesSet = new HashSet<String>();
        while (matcher.find()) {
            String tmpLine = matcher.group().replaceAll("\\[", "").replaceAll("\\]", "");
            lines.add(tmpLine);
            linesSet.add(tmpLine);
        } 
        
        List<SingleTitle> singleTitles = new ArrayList<SingleTitle>();
        if (lines.isEmpty()) {
            // Return an empty list if nothing found
            return singleTitles;
        }
        
        // Check if there are really multiple lines. Some statements are "fake".
        if (lines.size() >= 2) {
            boolean isMultipleLines = true;
            if (!"DELAYS".equals(status)) { // Don't check delays. They are not split.
                // Check if special terms are in the title
                for (String notMultipleLines : NOT_MULTIPLE_LINES_PATTERN) {
                    if (title.indexOf(notMultipleLines) != -1) {
                        isMultipleLines = false;
                        break;
                    }
                }
                // Check if they belong to the single line
                if (linesSet.size() < 2) {
                    isMultipleLines = false;
                }
            }
            if (isMultipleLines) {
                for (String line : linesSet) {
                    if (expectedLinesStr.contains(line) || 
                            ("SIR".equals(expectedLinesStr) && "SIR".equals(line))) {
                        singleTitles.add(new SingleTitle(line, title));
                    }
                }
                return singleTitles;
            }
        }
        
        // Only one single line
        SingleTitle singleTitle = new SingleTitle(lines.get(0), title);
        singleTitles.add(singleTitle);
        return singleTitles;
    }
}