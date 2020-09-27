import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;

public class Encryptor {
    public static void main(String[] args) {
        ZipParameters parameters = ProgramSettings.getEncryptionParameters();

        try {
            ZipFile zipFile = new ZipFile(ProgramSettings.outputFolder + ProgramSettings.outputFilename);
            zipFile.addFolder(
                    new File(ProgramSettings.dataPath + ProgramSettings.toEncrypt),
                    parameters
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
