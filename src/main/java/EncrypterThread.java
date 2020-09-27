import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EncrypterThread extends Thread {
    private GUIForm form;
    private File file;
    private ZipParameters parameters;

    private JTextField txtProgress;

    public EncrypterThread(GUIForm form) {
        this.form = form;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setProgressText(JTextField txtProgress) {
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
            if (file.isDirectory()) {
                if (txtProgress != null) {
                    List<File> allFiles = getFiledInDirectory(file);
                    for (int i = 0; i < allFiles.size(); i++) {
                        zipFile.addFile(allFiles.get(i), parameters);
                        txtProgress.setText(String.format("Progress: %d", (int) (((i * 1.0) / allFiles.size()) * 100)));
                    }
                } else {
                    zipFile.addFolder(file, parameters);
                }
            }
            //FIXME: добавлена возможнжость шифровать файлы
            else {
                zipFile.addFile(file, parameters);
            }
        } catch (Exception ex) {
            form.showWarning(ex.getMessage());
        }
        onFinish();
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