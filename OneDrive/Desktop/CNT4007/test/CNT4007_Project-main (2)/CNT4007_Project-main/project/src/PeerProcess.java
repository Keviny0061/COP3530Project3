import Utils.FilePathsUtil;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class PeerProcess {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Expecting: java PeerProcess <peerID>");
            return;
        }

        String myPeerId = args[0];
        Peer myPeer = new Peer();
        ArrayList<Peer> peersToConnectTo = new ArrayList<>();

        CommonConfigInfoReader configReader = new CommonConfigInfoReader();
        CommonConfigInfo config = configReader.read();

        // Parse file
        String path = System.getProperty("user.dir") +  "/project/src/project_config_file_small/project_config_file_small/testPeerInfo.cfg";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String peerId = parts[0];
                String hostname = parts[1];
                int port = Integer.parseInt(parts[2]);
                boolean hasFile = parts[3].equals("1");

                if (!parts[3].equals("1") && !parts[3].equals("0")) {
                    throw new IllegalArgumentException("Expecting a 1 or 0 for fourth column");
                }

                if (peerId.equals(myPeerId)) {
                    myPeer = new Peer(peerId, hostname, port, hasFile, config);
                    break;
                }

                peersToConnectTo.add(new Peer(peerId, hostname, port, hasFile, config));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (myPeer == null) {
            System.out.println("Peer with ID " + myPeerId + " not found.");
            return;
        }

        assert (myPeer.peerId == myPeerId);
        new Thread(myPeer::startListening).start();

        for (Peer peer : peersToConnectTo) {
            if (!peer.peerId.equals(myPeerId)) {
                myPeer.connectToPeer(peer);
            }
        }
    }
}