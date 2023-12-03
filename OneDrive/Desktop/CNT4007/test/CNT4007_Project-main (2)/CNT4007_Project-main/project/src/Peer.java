import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Peer { String peerId;
    String hostname;
    int port;
    boolean hasFile;
    CommonConfigInfo commonConfigInfo;
    byte[] bitfield;
    List<byte[]> pieces;

    public Peer() {
    }
    public Peer(String peerId, String hostname, int port, boolean hasFile, CommonConfigInfo commonConfigInfo) {
        this.peerId = peerId;
        this.hostname = hostname;
        this.port = port;
        this.hasFile = hasFile;
        this.commonConfigInfo = commonConfigInfo;
        initializeBitfield();

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + peerId + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeBitfield() {
        int num_pieces = (int) Math.ceil((double)commonConfigInfo.fileSize / commonConfigInfo.pieceSize);
        int bitfieldSize = (int) Math.ceil((double)num_pieces / 8);
        this.bitfield = new byte[bitfieldSize];
        this.pieces = new ArrayList<>(Collections.nCopies(num_pieces, null));

        if (hasFile) {
            // todo also file into pieces
            Arrays.fill(this.bitfield, (byte) 0xFF);  // All bits set to 1

            // Zero out extra bits in the last byte
            int extraBits = 8 - (num_pieces % 8);
            if (extraBits != 8) {
                this.bitfield[bitfieldSize - 1] &= (byte) (0xFF << extraBits);
            }
        } else {
            Arrays.fill(this.bitfield, (byte) 0x00);  // All bits set to 0
        }
    }

    public void logTCPConnection(String peerId1, String peerId2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String logMessage = String.format("%s: Peer %s makes a connection to Peer %s.", time, peerId1, peerId2);

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + peerId1 + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

            writer.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void logTCPConnectedFrom(String peerId1, String peerId2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String logMessage = String.format("%s: Peer %s is connected from Peer %s.", time, peerId1, peerId2);

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + peerId1 + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

            writer.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void logPreferredNeighborsChange(String peer_ID, List<String> preferredNeighborList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String preferredNeighbors = String.join(", ", preferredNeighborList);
        String logMessage = String.format("%s: Peer %s has the preferred neighbors %s.", time, peer_ID, preferredNeighbors);

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + peer_ID + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

            writer.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void logOptimisticallyUnchokedNeighborChange(String peer_ID, String optimisticallyUnchokedNeighborID) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String logMessage = String.format("%s: Peer %s has the optimistically unchoked neighbor %s.", time, peer_ID, optimisticallyUnchokedNeighborID);

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + peer_ID + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

            writer.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logUnchoking(String PeerId1, String PeerId2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String logMessage = String.format("%s: Peer %s is unchoked by %s.", time, PeerId1, PeerId2);

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + PeerId1 + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

            writer.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logChoking(String PeerId1, String PeerId2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String logMessage = String.format("%s: Peer %s is choked by %s.", time, PeerId1, PeerId2);

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + PeerId1 + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

            writer.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void logReceivedHaveMessage(String PeerId1, String PeerId2, int pieceIndex) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String logMessage = String.format("%s: Peer %s received the 'have' message from %s for the piece %d.", time, PeerId1, PeerId2, pieceIndex);

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + PeerId1 + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

            writer.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void logReceivedNotInterestedMessage(String PeerId1, String PeerId2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String logMessage = String.format("%s: Peer %s received the 'not interested' message from %s.", time, PeerId1, PeerId2);

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + PeerId1 + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

            writer.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logDownloadedPiece(String PeerId1, String PeerId2, int pieceIndex, int numberOfPieces) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String logMessage = String.format("%s: Peer %s has downloaded the piece %d from %s. Now the number of pieces it has is %d.", time, PeerId1, pieceIndex, PeerId2, numberOfPieces);

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + PeerId1 + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

            writer.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logCompletedDownload(String peer_ID) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String logMessage = String.format("%s: Peer %s has downloaded the complete file.", time, peer_ID);

        try (FileOutputStream fos = new FileOutputStream("log_peer_" + peer_ID + ".log", true);
             PrintWriter writer = new PrintWriter(fos)) {

            writer.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startListening() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    boolean listenForMessages = true;
                    try {
                        // Listen for handshake
                        byte[] receivedHandshakeData = new byte[32];
                        InputStream inStream = clientSocket.getInputStream();
                        inStream.read(receivedHandshakeData);
                        HandShakeMessage receivedHandshakeMsg = new HandShakeMessage("");
                        receivedHandshakeMsg.readHandShakeMessage(receivedHandshakeData);
                        assert(receivedHandshakeMsg.getHandShakeHeader().equals("P2PFILESHARINGPROJ"));
                        logTCPConnectedFrom(peerId, receivedHandshakeMsg.getPeerId());
                        System.out.println("Received handshake from Peer " + receivedHandshakeMsg.getPeerId());


                        // Handshake acknowledged and sent back
                        HandShakeMessage ackHandshakeMsg = new HandShakeMessage(String.valueOf(this.peerId));
                        byte[] ackHandshakeData = ackHandshakeMsg.createHandShakeMessage();
                        OutputStream outStream = clientSocket.getOutputStream();
                        outStream.write(ackHandshakeData);

                        // Listen to messages
                        while (listenForMessages) {
                            if (inStream.available() >= 4) { // Check if at least 4 bytes (for the length) are available
                                // Read 4-byte message length
                                byte[] lengthBytes = new byte[4];
                                inStream.read(lengthBytes);
                                ByteBuffer lengthBuffer = ByteBuffer.wrap(lengthBytes);
                                int messageLength = lengthBuffer.getInt();

                                // Read 1-byte message type
                                byte messageTypeByte = (byte) inStream.read();
                                if (messageTypeByte == -1) continue;
                                Message.MessageType messageType = Message.MessageType.values()[messageTypeByte & 0xFF];


                                switch (messageType) {
                                    case CHOKE:
                                        System.out.println("Handling CHOKE message.");
                                        break;
                                    case UNCHOKE:
                                        System.out.println("Handling UNCHOKE message.");
                                        break;
                                    case INTERESTED:
                                        System.out.println("Handling INTERESTED message.");
                                        break;
                                    case NOT_INTERESTED:
                                        System.out.println("Handling NOT_INTERESTED message.");
                                        break;
                                    case HAVE:
                                        System.out.println("Handling HAVE message.");
                                        break;
                                    case BITFIELD:
                                        System.out.println("Handling BITFIELD message.");
                                        // Read the payload
                                        byte[] payload = new byte[messageLength - 1]; // -1 because it starts reading after payload
                                        inStream.read(payload);

                                        Message receivedMessage = new Message(messageType, payload);
                                        System.out.println("Received message of type: " + receivedMessage.getMessageType());
                                        System.out.println("Message payload: " + Arrays.toString(receivedMessage.getPayload()));
                                        // todo code the response message
                                        break;
                                    case REQUEST:
                                        System.out.println("Handling REQUEST message.");
                                        break;
                                    case PIECE:
                                        System.out.println("Handling PIECE message.");
                                        break;
                                    default:
                                        System.out.println("Received an unknown message type.");
                                        break;
                                }
                            }
                            else {
                                Thread.sleep(100);
                                listenForMessages = false;

                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToPeer(Peer anotherPeer) {
        boolean listenForMessages = true;
        try (Socket socket = new Socket("localhost", anotherPeer.port)) {
            // Send handshake message
            HandShakeMessage handshakeMsg = new HandShakeMessage(this.peerId);
            byte[] handshakeData = handshakeMsg.createHandShakeMessage();
            OutputStream outStream = socket.getOutputStream();
            outStream.write(handshakeData);

            // Listen for handshake response
            byte[] ackData = new byte[32];
            InputStream inStream = socket.getInputStream();
            inStream.read(ackData);

            // Acknowledge handshake
            HandShakeMessage ackMsg = new HandShakeMessage("");
            ackMsg.readHandShakeMessage(ackData);

            assert(ackMsg.getHandShakeHeader().equals("P2PFILESHARINGPROJ"));
            assert(ackMsg.getPeerId().equals(anotherPeer.peerId));
            System.out.println("Received handshake acknowledgement from Peer " + ackMsg.getPeerId());

            logTCPConnection(peerId, anotherPeer.peerId);

            // since each bit in the bit field represents a piece, the bitfield size will be the number of pieces
            // divided by 8 to make an array of bytes
            int num_pieces = (int) Math.ceil((double)commonConfigInfo.fileSize / commonConfigInfo.pieceSize);
            int bitfieldSize = (int) Math.ceil(num_pieces / 8);

            byte[] bitfield = new byte[bitfieldSize];

            // Set all bits to 1
            for (int i = 0; i < bitfieldSize; i++) {
                bitfield[i] = (byte) 0xFF;  // All bits set to 1
            }

            // Zero out extra bits
            int extraBits = 8 - (num_pieces % 8);
            if (extraBits != 8) {
                bitfield[bitfieldSize - 1] &= (byte) (0xFF << extraBits);
            }

            Message bitFieldMessage = new Message(Message.MessageType.BITFIELD, bitfield);

            // Converts the message to bytes and sends it
            outStream.write(bitFieldMessage.toBytes());

            // Listen to messages
            while (listenForMessages) {
                var test = inStream.available();
                if (inStream.available() >= 4) { // Check if at least 4 bytes (for the length) are available
                    // Read 4-byte message length
                    byte[] lengthBytes = new byte[4];
                    inStream.read(lengthBytes);
                    ByteBuffer lengthBuffer = ByteBuffer.wrap(lengthBytes);
                    int messageLength = lengthBuffer.getInt();

                    // Read 1-byte message type
                    byte messageTypeByte = (byte) inStream.read();
                    if (messageTypeByte == -1) continue;
                    Message.MessageType messageType = Message.MessageType.values()[messageTypeByte & 0xFF];


                    switch (messageType) {
                        case CHOKE:
                            System.out.println("Handling CHOKE message.");
                            break;
                        case UNCHOKE:
                            System.out.println("Handling UNCHOKE message.");
                            break;
                        case INTERESTED:
                            System.out.println("Handling INTERESTED message.");
                            break;
                        case NOT_INTERESTED:
                            System.out.println("Handling NOT_INTERESTED message.");
                            break;
                        case HAVE:
                            System.out.println("Handling HAVE message.");
                            break;
                        case BITFIELD:
                            System.out.println("Handling BITFIELD message.");
                            // Read the payload
                            byte[] payload = new byte[messageLength - 1]; // -1 because it starts reading after payload
                            inStream.read(payload);

                            Message receivedMessage = new Message(messageType, payload);
                            System.out.println("Received message of type: " + receivedMessage.getMessageType());
                            System.out.println("Message payload: " + Arrays.toString(receivedMessage.getPayload()));
                            break;
                        case REQUEST:
                            System.out.println("Handling REQUEST message.");
                            break;
                        case PIECE:
                            System.out.println("Handling PIECE message.");
                            break;
                        default:
                            System.out.println("Received an unknown message type.");
                            break;
                    }
                }
                else {
                    Thread.sleep(100);
                    listenForMessages = false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
