package macnss.dao;

import macnss.Enum.RefundFileStatus;
import macnss.model.RefundFile;
import macnss.model.User;

import java.util.List;

public interface RefundFileDAO {
    public boolean addRefundFileToDatabase(RefundFile file);

    public List<RefundFile> getAllFiles();

    public List<RefundFile> getFileByUser(User patient);


    public boolean updateFile(int fileId, RefundFileStatus newStatus);
    public List<RefundFile> getFileBymatricule(int matricule);
}
