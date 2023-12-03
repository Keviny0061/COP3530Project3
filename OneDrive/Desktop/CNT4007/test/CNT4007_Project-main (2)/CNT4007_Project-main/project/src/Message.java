import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Message {
    public enum MessageType {
        CHOKE(0),
        UNCHOKE(1),
        INTERESTED(2),
        NOT_INTERESTED(3),
        HAVE(4),
        BITFIELD(5),
        REQUEST(6),
        PIECE(7);

        private final byte value;

        MessageType(int value) {
            this.value = (byte) value;
        }

        public byte getValue() {
            return value;
        }
    }

    private byte[] payload;
    private MessageType messageType;

    public Message(MessageType messageType, byte[] payload) {
        this.messageType = messageType;
        this.payload = payload;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public byte[] getPayload() {
        return payload;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Get the message length
        int messageLength = 1 + payload.length;
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.putInt(messageLength);

        // Write the 4-byte message length field
        outputStream.write(lengthBuffer.array(), 0, 4);

        // Write the 1-byte message type field
        outputStream.write(messageType.getValue());

        // Write the message payload
        outputStream.write(payload);

        return outputStream.toByteArray();
    }

}
