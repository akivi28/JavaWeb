package itstep.learning.services.files;

import org.apache.commons.fileupload.FileItem;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileService {
    String uploadAvatar(FileItem fileItem);
    OutputStream download(String fileName);
}
