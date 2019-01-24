package alfheim.common.core.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.ModInfo;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.common.core.version.VersionChecker;

public class InfoLoader {

	public static List<String> info = new ArrayList<String>();
	public static boolean outdated = false;
	
	public static boolean doneChecking = false;
	public static int onlineVersion = 0;
	public static boolean triedToWarnPlayer = false;
	
	public static void start() {
		new ThreadLoadInfo();
		MinecraftForge.EVENT_BUS.register(new InfoLoader());
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
	
	static class ThreadLoadInfo extends Thread {
		
		public ThreadLoadInfo() {
			setName("Alfheim Version Checker Thread");
			setDaemon(true);
			start();
		}
		
		@Override
		public void run() {
			try {
				Node root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new URL("https://bitbucket.org/AlexSocol/alfheim/raw/" + (ModInfo.DEV ? "development" : "master") + "/news/" + MinecraftForge.MC_VERSION + ".xml").openStream()).getDocumentElement();
				String latest = getNodeValue(root, "LATEST");
				onlineVersion = Integer.parseInt(latest.split("-")[1]);
				outdated = onlineVersion > Integer.parseInt(ModInfo.BUILD.replaceAll("\\D", ""));
				if (outdated) info.add(StatCollector.translateToLocalFormatted("alfheimmisc.update", ModInfo.VERSION, latest));
				String s = getNodeValue(root, "UNIVERSAL");
				info.addAll(Arrays.asList(s.split("&")));
				s = getNodeValue(root, ModInfo.VERSION);
				if (s != null && !s.isEmpty()) {
					info.add("=====================================================");
					info.addAll(Arrays.asList(s.split("&")));
				}
			} catch (Exception e) {
				ASJUtilities.error("Unable to load news & version from official repo. Check your internet connection.");
				e.printStackTrace(System.err);
			}
			doneChecking = true;
			ASJUtilities.log("Successfully loaded news & version");
		}
	}
}
