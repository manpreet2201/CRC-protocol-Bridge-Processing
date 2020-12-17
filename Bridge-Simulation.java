package networks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Q2 {

	public static void main(String[] args) {
		try {
			String st = "";

			// Map to store Bridge FDB
			Map<String, String> BridgeFDB = new HashMap<String, String>();
			ArrayList<String> Bridge = new ArrayList<String>();
			ArrayList<String> RandomFrames = new ArrayList<String>();

			FileWriter myWriter = new FileWriter("BridgeOutput.txt");
			FileWriter myWriter1 = new FileWriter("BridgeFDBUpdated.txt");
			try {
				BufferedReader br = new BufferedReader(
						new FileReader("/Users/manpreetsingh/" + "Downloads/BridgeFDB.txt"));
				while ((st = br.readLine()) != null) {
					Bridge.add(st);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < Bridge.size(); i = i + 2) {
				// adding FDB info to map
				BridgeFDB.put(Bridge.get(i), Bridge.get(i + 1));
			}

			try {
				BufferedReader br1 = new BufferedReader(
						new FileReader("/Users/manpreetsingh/Downloads/" + "RandomFrames.txt"));
				while ((st = br1.readLine()) != null) {
					RandomFrames.add(st);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// processing random frames
			for (int i = 0; i < RandomFrames.size(); i += 3) {
				String source = RandomFrames.get(i);
				String destination = RandomFrames.get(i + 1);
				String port = RandomFrames.get(i + 2);
				// checking if source MAC address and dest MAC are in FDB and if dest port and
				// source ports are equal
				if (BridgeFDB.containsKey(source) && BridgeFDB.containsKey(destination)) {
					String dport = BridgeFDB.get(destination);
//frame discarded if source and dest port are equal
					if (port.equals(dport)) {
						myWriter.write(source + " " + destination + " " + port + " Discarded" + "\n");
					}

					// frame forwarded if source and dest port are not equal
					else {
						myWriter.write(source + " " + destination + " " + port + " Forwarded to port " + dport + "\n");
					}
				}

				// checking if there is source MAC address and no entry for
				// dest MAC in FDB
				else if (BridgeFDB.containsKey(source) && !BridgeFDB.containsKey(destination)) {
					String dport = BridgeFDB.get(destination);
					myWriter.write(source + " " + destination + " " + port + " Broadcasted" + "\n");

				}
				// checking if there is no entry for source MAC address
				else if (!BridgeFDB.containsKey(source)) {
					BridgeFDB.put(source, port);
					myWriter.write(source + " " + destination + " " + port + " Updated FDB" + "\n");
					if (BridgeFDB.containsKey(destination)) {
						String dport = BridgeFDB.get(destination);
						if (port.equals(dport)) {
							myWriter.write(source + " " + destination + " " + port + " Discarded" + "\n");
						}

						else {
							myWriter.write(
									source + " " + destination + " " + port + " Forwarded to port " + dport + "\n");

						}

					} else {
						myWriter.write(source + " " + destination + " " + port + " Broadcasted" + "\n");
					}
				}

			}
//updating BridgeFDB if sourceMAC  address in coming frame doesnot exist in FDB
			for (Map.Entry mapElement : BridgeFDB.entrySet()) {
				String key = (String) mapElement.getKey();
				String value = (String) mapElement.getValue();
				myWriter1.write(key + "\n" + value + "\n");
			}
			myWriter.close();
			myWriter1.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
