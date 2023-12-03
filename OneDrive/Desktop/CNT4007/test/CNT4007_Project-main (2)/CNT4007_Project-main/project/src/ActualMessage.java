import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.BitSet;

public class ActualMessage {
    private int messageLength;
    private int messageType;
    private byte[] messagePayload;

    // Default constructor
    public ActualMessage() {
    }

    // Constructor for creating a message with just the message type
    public ActualMessage(int messageType) {
        this.messageType = messageType;
        this.messageLength = 1;
        this.messagePayload = new byte[0];
    }

    // Constructor for creating a message with a message type and payload
    public ActualMessage(int messageType, byte[] messagePayload) {
        this.messageType = messageType;
        this.messagePayload = messagePayload;
        this.messageLength = this.messagePayload.length + 1;
    }

    // Serialize the message into a byte array
    public byte[] buildActualMessage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            // Serialize the message length as 4 bytes
            byte[] lengthBytes = ByteBuffer.allocate(4).putInt(this.messageLength).array();
            stream.write(lengthBytes);
            
            // Serialize the message type as 4 bytes
            byte[] typeBytes = ByteBuffer.allocate(4).putInt(this.messageType).array();
            stream.write(typeBytes);

            // Serialize the payload
            stream.write(this.messagePayload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    // Deserialize the message from a byte array
    public void readActualMessage(int len, byte[] message) {
        this.messageLength = len;
        this.messageType = extractMessageType(message, 0);
        this.messagePayload = extractPayload(message, 4);
    }

    // Extract an integer from a byte array
    public int extractIntFromByteArray(byte[] message, int start) {
        byte[] intBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            intBytes[i] = message[i + start];
        }
        ByteBuffer bb = ByteBuffer.wrap(intBytes);
        return bb.getInt();
    }

    // Extract the message type (integer) from a byte array
    public int extractMessageType(byte[] message, int index) {
        return extractIntFromByteArray(message, index);
    }

    // Extract the payload from a byte array
    public byte[] extractPayload(byte[] message, int index) {
        byte[] resp = new byte[this.messageLength - 1];
        System.arraycopy(message, index, resp, 0, this.messageLength - 1);
        return resp;
    }

    // Get the bit field message as a BitSet
    public BitSet getBitFieldMessage() {
        BitSet bits = new BitSet();
        bits = BitSet.valueOf(this.messagePayload);
        return bits;
    }

    // Get the piece index from the payload
    public int getPieceIndexFromPayload() {
        return extractIntFromByteArray(this.messagePayload, 0);
    }

    // Get the piece data from the payload
    public byte[] getPieceFromPayload() {
        int size = this.messageLength - 5;
        byte[] piece = new byte[size];
        for (int i = 0; i < size; i++) {
            piece[i] = this.messagePayload[i + 4];
        }
        return piece;
    }

    // Get the message type as an integer
    public int getMessageType() {
        return this.messageType;
    }

    public static void main(String[] args) {
        // Test the ActualMessage class

        // Create a test message with just the message type (messageType = 1)
        ActualMessage testMessage1 = new ActualMessage(1);
        byte[] testMessageBytes1 = testMessage1.buildActualMessage();

        // Deserialize and display the message type of the received message
        ActualMessage receivedMessage1 = new ActualMessage();
        receivedMessage1.readActualMessage(testMessageBytes1.length, testMessageBytes1);
        System.out.println("Received Message Type: " + receivedMessage1.getMessageType());

        // Create a BitField message with message type 2
        BitSet bitField = new BitSet(8);
        bitField.set(1);
        bitField.set(3);
        byte[] bitFieldBytes = bitField.toByteArray();
        ActualMessage testMessage2 = new ActualMessage(2, bitFieldBytes);
        byte[] testMessageBytes2 = testMessage2.buildActualMessage();

        // Deserialize and display the message type and its content
        ActualMessage receivedMessage2 = new ActualMessage();
        receivedMessage2.readActualMessage(testMessageBytes2.length, testMessageBytes2);
        System.out.println("Received BitField Message Type: " + receivedMessage2.getMessageType());
        BitSet receivedBitField = receivedMessage2.getBitFieldMessage();
        System.out.println("Received BitField: " + receivedBitField);
    }
}
