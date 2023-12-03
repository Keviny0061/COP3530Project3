import Utils.FilePathsUtil;

public class Main {
    public static void main(String[] args) throws Exception {
        CommonConfigInfoReader configReader = new CommonConfigInfoReader();
        CommonConfigInfo configInfo = configReader.read();

        // Now, you can access the loaded values
        System.out.println("NumberOfPreferredNeighbors: " + configInfo.numNeighbors);
        System.out.println("UnchokingInterval: " + configInfo.unchokingInterval);
        System.out.println("OptimisticUnchokingInterval: " + configInfo.optimisticUnchoking);
        System.out.println("FileName: " + configInfo.fileName);
        System.out.println("FileSize: " + configInfo.fileSize);
        System.out.println("PieceSize: " + configInfo.pieceSize);

        PeerFileSplitter peerFileSplitter = new PeerFileSplitter("1001", configInfo);
        peerFileSplitter.splitFileIntoPieceFilesInPeerFolder();
    }
}
