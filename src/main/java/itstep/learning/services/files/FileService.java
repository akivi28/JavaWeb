package itstep.learning.services.files;

import org.apache.commons.fileupload.FileItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileService {
    String uploadAvatar(FileItem fileItem);
    InputStream download(String fileName) throws IOException;
}
