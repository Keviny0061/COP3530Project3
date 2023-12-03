
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HandShakeMessage {
    //The handshake consists of three parts: handshake header, zero bits, and peer ID
    //the zerobits does not need to be a variable since it is constant 10 bytes of zeros

    private String handshakeHeader;
    private String peerId;
    
    
    public HandShakeMessage(String peerId) {
        this.handshakeHeader = "P2PFILESHARINGPROJ";
        this.peerId = peerId;
    }
    //allows us to get the header
    public String getHandShakeHeader(){
        return this.handshakeHeader;
    }
    //allows us to get the peerId
    public String getPeerId(){
        return this.peerId;
    }

    public byte[] createHandShakeMessage(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try{
            //create byte arrays for all elemenets as far as I know all input is standardcharsets
        byte[] handshakeBytes = this.handshakeHeader.getBytes(StandardCharsets.UTF_8);
        byte[] zeroBytes = new byte[10];
        //ensure the new bytes written is 0 uses hexadecimal 
        Arrays.fill(zeroBytes, (byte) 0x00);
        byte[] peerIdBytes = this.peerId.getBytes(StandardCharsets.UTF_8);
        //create the array for the fullMessage
        byte[] fullMessage = new byte[handshakeBytes.length + zeroBytes.length + peerIdBytes.length];
       
        // Copy the individual data arrays into the combined array
        System.arraycopy(handshakeBytes, 0, fullMessage, 0, handshakeBytes.length);
        System.arraycopy(zeroBytes, 0, fullMessage, handshakeBytes.length, zeroBytes.length);
        System.arraycopy(peerIdBytes, 0, fullMessage, handshakeBytes.length + zeroBytes.length, peerIdBytes.length);
        
        // Write the combined data to the stream
        stream.write(fullMessage);
        }
        catch(Exception e){ 
            //incase creating a HandshakeMessage is not working as expected
            System.out.println("Error in createHandshakeMessage");
        }
        //info to help found here: https://www.tutorialspoint.com/java/java_bytearrayoutputstream.htm#:~:text=public%20byte%5B%5D%20toByteArray(),stream%20as%20a%20byte%20array.
        return stream.toByteArray();
    }
    public void readHandShakeMessage(byte[] message){
        //The length of the handshake message is 32 bytes
        //and the peerid is the last 4
        String msg = new String(message,StandardCharsets.UTF_8);
        this.peerId = msg.substring(28,32);
    }

    //below is just for testing reasons
    public static void main(String[] args) {
        testHandShakeMessage();
    }

    public static void testHandShakeMessage() {
        // Replace "yourPeerID" with the actual peer ID you want to use
        String peerID = "1234";

        // Create a HandShakeMessage instance
        HandShakeMessage handshake = new HandShakeMessage(peerID);

        // Create a handshake message byte array
        byte[] handshakeMessage = handshake.createHandShakeMessage();

        // Display the handshake header and peer ID
        System.out.println("Handshake Header: " + handshake.getHandShakeHeader());
        System.out.println("Peer ID: " + handshake.getPeerId());

        // Display the entire handshake message as bytes
        System.out.print("Handshake Message (Bytes): ");
        for (byte b : handshakeMessage) {
            System.out.print(String.format("%02X ", b));
        }
        System.out.println();

        // Test reading the handshake message
        HandShakeMessage receivedHandshake = new HandShakeMessage("");
        receivedHandshake.readHandShakeMessage(handshakeMessage);

        System.out.println("Received Peer ID: " + receivedHandshake.getPeerId());
    }
}

//below is for testing only

