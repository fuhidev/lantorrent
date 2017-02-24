package transferfile.client.transfer.tracker;

public interface IUploadManager {
void finish(ProgressUpload upload);
void add(ProgressUpload upload);
}
