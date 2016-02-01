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

    public Map<String, Status> parse(InputStream rawXMLStream) throws MTAStatusException {
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
            
            Map<String, Status> statusMap = new HashMap<String, Status>();
            for (String trainKey : Constants.LINES.split("\\|")) {
            	List<Alert> alerts = alertsOfTimestamp.get(trainKey);
            	boolean hasDetail = false;
            	if (alerts != null && !alerts.isEmpty()) {
            		for (Alert alert : alerts) {
            			alert.title = AdjustAbbreviationAndAddPeriod(alert.title);
            			alert.detail = AdjustAbbreviationAndAddPeriod(alert.detail);
            		}

            		TrainStatus status = TrainStatus.parse(alerts.get(0).status);
            		StringBuilder builder = new StringBuilder();
            		for (Alert alert : alerts) {
            			builder.append(alert.title).append(" ");
            		}
            		String title = builder.toString().trim();

            		builder = new StringBuilder();
            		for (Alert alert : alerts) {
            			builder.append(alert.title).append(" ");
            			if (alert.detail != null && !alert.detail.isEmpty()) {
            				hasDetail = true;
            				builder.append(alert.detail).append(" ");
            			}
            		}
            		
            		String detail = null;
            		if (hasDetail) {
            			detail = builder.toString().trim();
            		}
            		statusMap.put(trainKey, new Status(trainKey, status, title, detail));
            	} else {
            		statusMap.put(trainKey, new Status(trainKey, TrainStatus.GOOD_SERVICE, null, null));
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
    
    private String AdjustAbbreviationAndAddPeriod(String input) {
    	String output = input.trim();
    	if (output.isEmpty()) {
    		return "";
    	}
    	output = output.replaceAll("(?i)\\bst\\b", "Street")
    			.replaceAll("(?i)\\bsts\\b", "Streets")
    			.replaceAll("(?i)\\bav\\b", "Avenue")
    			.replaceAll("(?i)\\bave\\b", "Avenue")
    			.replaceAll("(?i)\\baves\\b", "Avenues")
    			.replaceAll("(?i)\\bavs\\b", "Avenues")
    			.replaceAll("(?i)\\bbklyn\\b", "Brooklyn")
    			.replaceAll("(?i)\\bqns\\b", "Queens")
    			.replaceAll("(?i)\\bmon\\b", "Monday")
    			.replaceAll("(?i)\\btue\\b", "Tuesday")
    			.replaceAll("(?i)\\bwed\\b", "Wednesday")
    			.replaceAll("(?i)\\bthu(r)*\\b", "Thursday")
    			.replaceAll("(?i)\\bfri\\b", "Friday")
    			.replaceAll("(?i)\\bsat\\b", "Saturday")
    			.replaceAll("(?i)\\bsun\\b", "Sunday")
    			.replaceAll("(?i)\\bjan\\b", "January")
    			.replaceAll("(?i)\\bfeb\\b", "Feburary")
    			.replaceAll("(?i)\\bmar\\b", "March")
    			.replaceAll("(?i)\\bapr\\b", "April")
    			.replaceAll("(?i)\\bjun\\b", "June")
    			.replaceAll("(?i)\\bjul\\b", "July")
    			.replaceAll("(?i)\\baug\\b", "August")
    			.replaceAll("(?i)\\bsep\\b", "September")
    			.replaceAll("(?i)\\boct\\b", "October")
    			.replaceAll("(?i)\\bnov\\b", "November")
    			.replaceAll("(?i)\\bdec\\b", "December")
    			.replaceAll("(?i)\\[SB\\]\\s*(free )*(shuttle )*(bus)*(es)*", "$1Shuttle Bus$4")
    			.replaceAll("(?i)\\bsq\\b", "Square");
    	if (!output.endsWith(".")) {
    		output += ".";
    	}
    	return output;
    }

    private void addAlert(Alert alert, Map<String, List<Alert>> alertsOfTimestamp) {
        List<Alert> alerts = alertsOfTimestamp.get(alert.line);
        if (alerts == null) {
            alerts = new ArrayList<Alert>();
        }
        alerts.add(alert);
        alertsOfTimestamp.put(alert.line, alerts);
    }

    private Alert buildTempAlert(String status, String title, String detail, boolean hasDetail, StringBuilder builder) {
    	// Replace <br> to period to add pause.
    	// Add a dot to any line that does not end with a punctuation.
    	String rawString = builder.toString().replaceAll("([a-zA-Z0-9])\\s*(<br>)+", "$1. ");
    	
        if (hasDetail) {
            Element detailElem = Jsoup.parse(rawString);
            // Remove tables
            detailElem.select("table").remove();
            // \\p{C} is for removing non-displayable characters.
            detail = detailElem.text().replaceAll("\\p{C}", "").replaceAll("\\s+", " ");
        } else {
            title = Jsoup.parse(rawString).text().replaceAll("\\p{C}", "").replaceAll("\\s+", " ");
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
            	// Append <br> and they will be treated later.
            	builder.append("<br>");
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
    
    private static Pattern LINES_PATTERN = Pattern.compile("\\[(" + Constants.LINES + ")D*\\]"); // D for diamond
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

        Set<String> linesSet = new HashSet<String>();
        String firstLine = null;
        while (matcher.find()) {
            String tmpLine = matcher.group().replaceAll("\\[", "").replaceAll("\\]", ""); 
            if (!tmpLine.equals("SIR") && tmpLine.length() > 1) {
            	tmpLine = tmpLine.substring(0, 1); // [6D] => [6]
            	title = title.replaceAll("\\[([67])D\\]", "[$1 Express]"); // [6D] => [6 Express]. Only 6 and 7 have diamonds.
            }
            if (firstLine == null) {
            	firstLine = tmpLine;
            }
            linesSet.add(tmpLine);
        } 
        
        List<SingleTitle> singleTitles = new ArrayList<SingleTitle>();
        if (linesSet.isEmpty()) {
            // Return an empty list if nothing found
            return singleTitles;
        }
        
        // Check if there are really multiple lines. Some statements are "fake" multiples.
        if (linesSet.size() >= 2) {
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
        if (expectedLinesStr.contains(firstLine) || 
            ("SIR".equals(expectedLinesStr) && "SIR".equals(firstLine))) {
        	SingleTitle singleTitle = new SingleTitle(firstLine, title);
        	singleTitles.add(singleTitle);
        }
        return singleTitles;
    }
}