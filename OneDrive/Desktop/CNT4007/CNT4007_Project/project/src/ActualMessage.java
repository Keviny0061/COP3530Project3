import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.BitSet;
//this is an implementation of viewing message along with a test, need to see which message class suits our purposes
public class ActualMessage {
    private int messageLength;
    private int messageType;
    private byte[] messagePayload;

    public ActualMessage() {
    }

    public ActualMessage(int messageType) {
        this.messageType = messageType;
        this.messageLength = 1;
        this.messagePayload = new byte[0];
    }

    public ActualMessage(int messageType, byte[] messagePayload) {
        this.messageType = messageType;
        this.messagePayload = messagePayload;
        this.messageLength = this.messagePayload.length + 1;
    }

    public byte[] buildActualMessage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            byte[] bytes = ByteBuffer.allocate(4).putInt(this.messageLength).array();
            stream.write(bytes);
            stream.write(ByteBuffer.allocate(4).putInt(this.messageType).array());
            stream.write(this.messagePayload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    public void readActualMessage(int len, byte[] message) {
        this.messageLength = len;
        this.messageType = extractMessageType(message, 0);
        this.messagePayload = extractPayload(message, 1);
        System.out.print("test");
    }

    public int extractIntFromByteArray(byte[] message, int start) {
        byte[] len = new byte[4];
        for (int i = 0; i < 4; i++) {
            len[i] = message[i + start];
        }
        ByteBuffer bb = ByteBuffer.wrap(len);
        return bb.getInt();
    }

    public int extractMessageType(byte[] message, int index) {
        byte[] typeBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            typeBytes[i] = message[i + index];
        }
        ByteBuffer bb = ByteBuffer.wrap(typeBytes);
        return bb.getInt();
    }

    public byte[] extractPayload(byte[] message, int index) {
        byte[] resp = new byte[this.messageLength - 1];
        System.arraycopy(message, index, resp, 0, this.messageLength - 1);
        return resp;
    }

    public BitSet getBitFieldMessage() {
        BitSet bits = new BitSet();
        bits = BitSet.valueOf(this.messagePayload);
        return bits;
    }

    public int getPieceIndexFromPayload() {
        return extractIntFromByteArray(this.messagePayload, 0);
    }

    public byte[] getPieceFromPayload() {
        int size = this.messageLength - 5;
        byte[] piece = new byte[size];
        for (int i = 0; i < size; i++) {
            piece[i] = this.messagePayload[i + 4];
        }
        return piece;
    }

    public int getMessageType() {
        return this.messageType;
    }

    public static void main(String[] args) { //lets use see if we can recieve messages right
        // Test the ActualMessage class
        ActualMessage testMessage1 = new ActualMessage(1); // Create a test message with messageType 1
        byte[] testMessageBytes1 = testMessage1.buildActualMessage(); // Serialize the message

        ActualMessage receivedMessage1 = new ActualMessage();
        receivedMessage1.readActualMessage(testMessageBytes1.length, testMessageBytes1); // Deserialize the message

        // Display the message type of the received message
        System.out.println("Received Message Type: " + receivedMessage1.getMessageType());

        // Test with a BitField message
        BitSet bitField = new BitSet(8);
        bitField.set(1);
        bitField.set(3);
        byte[] bitFieldBytes = bitField.toByteArray();

        ActualMessage testMessage2 = new ActualMessage(2, bitFieldBytes); // Create a BitField message with messageType 2
        byte[] testMessageBytes2 = testMessage2.buildActualMessage(); // Serialize the BitField message

        ActualMessage receivedMessage2 = new ActualMessage();
        receivedMessage2.readActualMessage(testMessageBytes2.length, testMessageBytes2); // Deserialize the BitField message

        // Display the message type of the received BitField message and its content
        System.out.println("Received BitField Message Type: " + receivedMessage2.getMessageType());
        BitSet receivedBitField = receivedMessage2.getBitFieldMessage();
        System.out.println("Received BitField: " + receivedBitField);
    }
}
