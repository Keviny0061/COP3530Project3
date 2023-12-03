import Utils.FilePathsUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CommonConfigInfoReader {
    /*
        Example structure

        NumberOfPreferredNeighbors 3
        UnchokingInterval 5
        OptimisticUnchokingInterval 10
        FileName thefile
        FileSize 2167705
        PieceSize 16384
     */

    public CommonConfigInfo read() throws IOException {
        CommonConfigInfo info = new CommonConfigInfo();
        String configPath = FilePathsUtil.CommonConfigPath.getDefaultPath();

        try {
            // Read in from filepath
            BufferedReader reader = new BufferedReader(new FileReader(configPath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");

                // Skip lines that don't have two parts
                if (parts.length != 2) {
                    continue;
                }

                String key = parts[0];
                String value = parts[1];

                switch (key) {
                    case "NumberOfPreferredNeighbors":
                        info.numNeighbors = Integer.parseInt(value);
                        break;
                    case "UnchokingInterval":
                        info.unchokingInterval = Integer.parseInt(value);
                        break;
                    case "OptimisticUnchokingInterval":
                        info.optimisticUnchoking = Integer.parseInt(value);
                        break;
                    case "FileName":
                        info.fileName = value;
                        break;
                    case "FileSize":
                        info.fileSize = Integer.parseInt(value);
                        break;
                    case "PieceSize":
                        info.pieceSize = Integer.parseInt(value);
                        break;
                }
            }

            reader.close();
            return info;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
