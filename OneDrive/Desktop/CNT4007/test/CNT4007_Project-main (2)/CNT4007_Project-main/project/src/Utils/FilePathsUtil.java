package Utils;

public class FilePathsUtil {
    public final static String PIECE_PREFIX = "piece_";
    private final static CommonConfigPath CONFIG_PATH = CommonConfigPath.SMALL;
    private final static PeerFolderPath PEER_FOLDER_PATH = PeerFolderPath.WITHIN_SMALL;

    public enum CommonConfigPath {
        SMALL(System.getProperty("user.dir") +  "/project/src/project_config_file_small/project_config_file_small/Common.cfg");

        private String filePath;
        CommonConfigPath(String filePath){
            this.filePath = filePath;
        }

        public static String getDefaultPath(){
            return CONFIG_PATH.filePath;
        }
    }

    public enum PeerFolderPath {
        WITHIN_SMALL(System.getProperty("user.dir") +  "/project/src/project_config_file_small/project_config_file_small/");

        private String filePath;
        PeerFolderPath(String filePath){
            this.filePath = filePath;
        }

        public static String defaultWith(String peerId){
            return PEER_FOLDER_PATH.filePath + peerId + "/";
        }

        public static String defaultWith(String peerId, String filename){
            return PEER_FOLDER_PATH.filePath + peerId + "/" + filename;
        }
    }
}
