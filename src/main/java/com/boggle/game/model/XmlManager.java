package com.boggle.game.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlManager {

    public void parseXml() {
        File xmlFile = new File("gameserver.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();
            System.out.println("Root Element :" + document.getDocumentElement().getNodeName());

            NodeList nodeList = document.getDocumentElement().getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    System.out.println("\nNode Name = " + element.getNodeName());

                    if (element.hasChildNodes()) {
                        NodeList childNodeList = element.getChildNodes();
                        for (int j = 0; j < childNodeList.getLength(); j++) {
                            Node childNode = childNodeList.item(j);
                            if (childNode.getNodeType() == Node.TEXT_NODE) {
                                System.out.println("Value = " + childNode.getNodeValue().trim());
                            }
                        }
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }


    public void xmlWrite(PlayerDetailsModel player) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.newDocument();

            Element rootElement = xmlDocument.createElement("Player");

            Element playerNameElement = xmlDocument.createElement("Name");
            playerNameElement.appendChild(xmlDocument.createTextNode(player.getPlayerName()));
            rootElement.appendChild(playerNameElement);

            Element checkedWordsElement = xmlDocument.createElement("CheckedWords");
            for (String word : player.getListOfCheckedWords()) {
                Element wordElement = xmlDocument.createElement("Word");
                wordElement.appendChild(xmlDocument.createTextNode(word));
                checkedWordsElement.appendChild(wordElement);
            }
            rootElement.appendChild(checkedWordsElement);

            Element possibleWordsElement = xmlDocument.createElement("PossibleWords");
            for (String word : player.getPossibleWords()) {
                Element wordElement = xmlDocument.createElement("Word");
                wordElement.appendChild(xmlDocument.createTextNode(word));
                possibleWordsElement.appendChild(wordElement);
            }
            rootElement.appendChild(possibleWordsElement);

            Element scoreElement = xmlDocument.createElement("Score");
            scoreElement.appendChild(xmlDocument.createTextNode(player.getScore().toString()));
            rootElement.appendChild(scoreElement);

            Element roundElement = xmlDocument.createElement("Round");
            roundElement.appendChild(xmlDocument.createTextNode(player.getRoundInt().toString()));
            rootElement.appendChild(roundElement);

            Element overallElement = xmlDocument.createElement("Overall");
            overallElement.appendChild(xmlDocument.createTextNode(player.getOverallInt().toString()));
            rootElement.appendChild(overallElement);

            xmlDocument.appendChild(rootElement);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Source xmlSource = new DOMSource(xmlDocument);
            Result xmlResult = new StreamResult(new File("playerDetails.xml"));
            transformer.transform(xmlSource, xmlResult);
            System.out.println("XML Created!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerDetailsModel xmlRead() {
        PlayerDetailsModel player = new PlayerDetailsModel();
        try {
            File xmlFile = new File("playerDetails.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(xmlFile);

            Element rootElement = xmlDocument.getDocumentElement();

            String playerName = rootElement.getElementsByTagName("Name").item(0).getTextContent();
            player.playerName = playerName;

            NodeList checkedWords = ((Element) rootElement.getElementsByTagName("CheckedWords").item(0)).getElementsByTagName("Word");
            List<String> checkedWordList = new ArrayList<>();
            for (int i = 0; i < checkedWords.getLength(); i++) {
                checkedWordList.add(checkedWords.item(i).getTextContent());
            }

            NodeList possibleWords = ((Element) rootElement.getElementsByTagName("PossibleWords").item(0)).getElementsByTagName("Word");
            ArrayList<String> possibleWordList = new ArrayList<>();
            for (int i = 0; i < possibleWords.getLength(); i++) {
                possibleWordList.add(possibleWords.item(i).getTextContent());
            }

            String score = rootElement.getElementsByTagName("Score").item(0).getTextContent();

            String round = rootElement.getElementsByTagName("Round").item(0).getTextContent();

            String overall = rootElement.getElementsByTagName("Overall").item(0).getTextContent();

            player.setRoundDetails(checkedWordList, possibleWordList, Integer.parseInt(score), Integer.parseInt(round), Integer.parseInt(overall));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return player;
    }


}
