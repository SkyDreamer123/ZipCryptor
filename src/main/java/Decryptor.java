import net.lingala.zip4j.core.ZipFile;

public class Decryptor {
    public static void main(String[] args) {
        try {
            ZipFile zipFile = new ZipFile(ProgramSettings.outputFolder + ProgramSettings.outputFilename);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(ProgramSettings.getEncryptionPassword());
            }
            zipFile.extractAll(ProgramSettings.outputFolder + ProgramSettings.decryptOutput);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
