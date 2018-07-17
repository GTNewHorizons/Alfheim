package alfheim.common.core.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import alfheim.api.ModInfo;

public class InfoLoader {

	public static List<String> info = new ArrayList<String>();
	
	public static void start() throws Throwable {   
		info.addAll(Arrays.asList(getNodeValue(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new URL(getNodeValue(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse((new URL("https://bitbucket.org/AlexSocol/alfheim/raw/master/news/links.xml")).openStream()).getDocumentElement(), "1.7.10")).openStream()).getDocumentElement(), ModInfo.VERSION).split("&")));
	}
	
	public static String getNodeValue(Node root, String attributeValue) {
		NodeList versions = root.getChildNodes();
		for (int i = 0; i < versions.getLength(); i++) {
			Node minecraft = versions.item(i); // <addon> in next iteration
			if (!minecraft.hasChildNodes() || !minecraft.hasAttributes() || !minecraft.getAttributes().item(0).getNodeValue().equals(attributeValue)) continue;

			NodeList links = minecraft.getChildNodes(); // <string>s in next iteration
			for (int j = 0; j < links.getLength(); j++) {
				Node link = links.item(j);
				if (!link.hasChildNodes()) continue;
				
				// only one will be parsed
				return link.getChildNodes().item(0).getNodeValue();
			}
		}
		return "";
	}
}
