import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ProgramSettings {
    public final static String dataPath = "data/";
    public final static String toEncrypt = "folder";

    public final static String outputFolder = "output/";
    public final static String outputFilename = "archive.zip";
    public final static String decryptOutput = "decrypted/";

    public static ZipParameters getEncryptionParameters()
    {
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        parameters.setPassword(getEncryptionPassword());

        return parameters;
    }

    public static String getEncryptionPassword(){
        return "123456";
    }

}
