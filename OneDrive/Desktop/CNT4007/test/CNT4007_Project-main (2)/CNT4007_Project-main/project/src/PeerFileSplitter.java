import Utils.FilePathsUtil;

import java.io.*;

public class PeerFileSplitter {
    //Given file, splits into piece_1... piece_n
    String peerId;
    CommonConfigInfo configInfo;

    public PeerFileSplitter(String peerId, CommonConfigInfo configInfo){
        this.peerId = peerId;
        this.configInfo = configInfo;
    }

    public void splitFileIntoPieceFilesInPeerFolder() throws IOException {
        String outputFolderPath = FilePathsUtil.PeerFolderPath.defaultWith(peerId);

        byte[] fileBytes = readPeerFileByteArr();
        double pieceCount = Math.ceil(configInfo.fileSize/(double)configInfo.pieceSize);

        //For every piece to create
        for(int i = 0; i < pieceCount; i++){
            // Copy corresponding bytes from file to piece
            byte[] pieceByteArr = readBytesAtPieceIndex(i, fileBytes);

            // Write out piece bytes into piece file.
            String piecePath = outputFolderPath + FilePathsUtil.PIECE_PREFIX + i;
            try(FileOutputStream fileOutputStream = new FileOutputStream(piecePath)){
                fileOutputStream.write(pieceByteArr);
            }
        }
    }

    public byte[] readBytesAtPieceIndex(int pieceIndex, byte[] fileByteArr){
        int currentByte = pieceIndex * configInfo.pieceSize;

        int pieceSize = Math.min(configInfo.pieceSize, configInfo.fileSize - currentByte);
        byte[] pieceByteArr = new byte[pieceSize];

        for(int j = 0; j < pieceSize; j++){
            pieceByteArr[j] = fileByteArr[currentByte];
            currentByte = currentByte + 1;
        }

        return pieceByteArr;
    }

    public byte[] readPeerFileByteArr() throws IOException {
        String inputFilePath = FilePathsUtil.PeerFolderPath.defaultWith(peerId, configInfo.fileName);
        byte[] fileByteArr = new byte[configInfo.fileSize];
        //Read file into arr
        try(FileInputStream fileInputStream = new FileInputStream(inputFilePath)){
            fileInputStream.read(fileByteArr);
        }
        return fileByteArr;
    }
}
