package totalpos;

import java.io.File;

/**
 *
 * @author Saúl Hidalgo
 */
public class Report {
    private File file;
    private String titleName;

    public Report(File file, String titleName) {
        this.file = file;
        this.titleName = titleName;
    }

    public File getFile() {
        return file;
    }

    public String getTitleName() {
        return titleName;
    }
}
