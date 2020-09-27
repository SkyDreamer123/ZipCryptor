import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EncrypterThread extends Thread {
    private GUIForm form;
    private File file;
    private ZipParameters parameters;

    private JLabel txtProgress;

    public EncrypterThread(GUIForm form) {
        this.form = form;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setProgressText(JLabel txtProgress) {
        this.txtProgress = txtProgress;
    }

    public void setPassword(String password) {
        parameters = ParametersContainer.getParameters();
        parameters.setPassword(password);
    }

    @Override
    public void run() {
        onStart();
        try {
            String archiveName = getArchiveName();
            ZipFile zipFile = new ZipFile(archiveName);

            if(file.isFile() || txtProgress == null)
            {
                zipFile.addFile(file, parameters);
                return;
            }

            encryptDirectory(zipFile);

        } catch (Exception ex) {
            form.showWarning(ex.getMessage());
        }
        onFinish();
    }

    private void encryptDirectory(ZipFile zipFile) throws ZipException
    {
        List<File> allFiles = getFiledInDirectory(file);
        int lastProgressValue = 0;
        for (int i = 0; i < allFiles.size(); i++) {
            //System.out.println("Add to zip " + allFiles.get(i).getName());
            zipFile.addFile(allFiles.get(i), parameters);
            int newProgressValue = (int) (((i * 1.0) / allFiles.size()) * 100);
            if (newProgressValue > lastProgressValue) {
                txtProgress.setText(String.format("Progress: %d%%", newProgressValue));
                lastProgressValue = newProgressValue;
                txtProgress.updateUI();
            }
        }

        txtProgress.setText("Готово");
    }

    private void onStart() {
        form.setButtonsEnabled(false);
    }

    private void onFinish() {
        parameters.setPassword("");
        form.setButtonsEnabled(true);
        form.showFinished();
    }

    private String getArchiveName() {
        for (int i = 1; ; i++) {
            String number = i > 1 ? Integer.toString(i) : "";
            String archiveName = file.getAbsolutePath() +
                    number + ".enc";
            if (!new File(archiveName).exists()) {
                return archiveName;
            }
        }
    }

    private List<File> getFiledInDirectory(File rootDirectory) {
        List<File> result = new ArrayList<>();

        result.add(rootDirectory);

        if (!rootDirectory.isDirectory()) {
            return result;
        }

        File[] listFiles = rootDirectory.listFiles();
        if (listFiles != null) {
            for (File file : rootDirectory.listFiles()) {
                if (file.isFile()) {
                    result.add(file);
                } else if (file.isDirectory()) {
                    result.addAll(getFiledInDirectory(file));
                }
            }
        }

//        System.out.println("Got fiels=" + result.size());
//        System.out.println(result);

        return result;
    }
}
