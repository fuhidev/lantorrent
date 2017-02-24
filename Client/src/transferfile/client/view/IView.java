package transferfile.client.view;

import transferfile.client.transfer.seeder.ProgressDownload;
import transferfile.client.transfer.tracker.ProgressUpload;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.ListFilesInfo;

public interface IView {

	/**
	 * Thêm tiến trình tải về
	 * 
	 * @param download
	 *            Tiến trình cần thêm vào
	 */
	void addDownload(ProgressDownload download);

	/**
	 * Xóa tiến trình
	 * 
	 * @param download
	 *            tiến trình cần xóa
	 */
	void removeDownload(ProgressDownload download);

	/**
	 * Cập nhật danh sách file chưa tải
	 * 
	 * @param listFilesInfo
	 *            Danh sách mới
	 */
	void updateNonActiveFile(ListFilesInfo listFilesInfo);

	/**
	 * Thêm file chưa tải
	 * 
	 * @param data
	 *            File chưa tải
	 */
	void addNonActiveFile(FileInfo data);

	/**
	 * Cập nhật tiến độ tải về của element
	 * 
	 * @param element
	 *            phần tử cần thay đổi tiến độ
	 * @param newValue
	 *            giá trị thay đổi
	 */
	void updateProgress(ProgressDownload element, long newValue);

	/**
	 * Thêm file đã tải
	 * 
	 * @param newValue
	 *            File đã tải
	 */
	void addActiveFile(FileInfo newValue);

	/**
	 * Cập nhật danh sách file đã tải
	 * 
	 * @param listFilesInfo
	 *            Danh sách mới
	 */
	void updateActiveFile(ListFilesInfo listFilesInfo);
	
	/**
	 * Thêm file đang gửi
	 * @param newValue File đang gửi
	 */
	void addSendingFile(ProgressUpload upload);
	/**
	 * Xóa file đang gửi
	 * @param newValue File đang gửi
	 */
	void removeSendingFile(ProgressUpload upload);
	/**
	 * Cập nhật tiến độ gửi lên của element
	 * 
	 * @param element
	 *            phần tử cần thay đổi tiến độ
	 * @param newValue
	 *            giá trị thay đổi
	 */
	void updateProgressUpload(ProgressUpload element, long newValue);
	
	void displayWaiting();
	void finishDisplayWaiting();
}
