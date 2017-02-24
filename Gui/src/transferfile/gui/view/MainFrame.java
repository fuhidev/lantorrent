package transferfile.gui.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Iterator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import transferfile.client.transfer.seeder.ProgressDownload;
import transferfile.client.transfer.tracker.ProgressUpload;
import transferfile.client.view.IView;
import transferfile.gui.model.SelectModel;
import transferfile.lib.controller.IController;
import transferfile.lib.model.FileInfo;
import transferfile.lib.model.ListFilesInfo;

public class MainFrame extends JFrame implements IView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5806804177534925679L;
	private JPanel contentPane;
	private DefaultListModel<SelectModel> selectModel = new DefaultListModel<>();
	private JTable table;
	private TableModel model;
	private DefaultTableModel model1;
	private DefaultTableModel model2;
	private DefaultTableModel model3;
	private DefaultTableModel model4;
	private IController controller;

	public static int tmp = 0;

	public void setController(IController controller) {
		this.controller = controller;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
		selectModel.addElement(new SelectModel("Downloading", 0));
		selectModel.addElement(new SelectModel("Dowloaded", 0));
		selectModel.addElement(new SelectModel("List download", 0));
		selectModel.addElement(new SelectModel("Sending", 0));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 682, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnTcV = new JMenu("File");
		menuBar.add(mnTcV);

		JMenuItem mntmAdd = new JMenuItem("Add");
		mntmAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				addFile();
			}
		});
		mnTcV.add(mntmAdd);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 89, 521, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JPanel panel1 = new JPanel();
		GridBagConstraints gbc_panel1 = new GridBagConstraints();
		gbc_panel1.insets = new Insets(0, 0, 0, 5);
		gbc_panel1.fill = GridBagConstraints.BOTH;
		gbc_panel1.gridx = 0;
		gbc_panel1.gridy = 0;
		contentPane.add(panel1, gbc_panel1);
		panel1.setLayout(new BorderLayout(0, 0));

		JList<SelectModel> selectedList = new JList<SelectModel>(selectModel);
		selectedList.setCellRenderer(new DefaultListCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				SelectModel model = (SelectModel) value;
				JLabel lb = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				lb.setText(model.getName() + "(" + model.getNum() + ")");
				return lb;
			}
		});

		JPanel panel2 = new JPanel();
		GridBagConstraints gbc_panel2 = new GridBagConstraints();
		gbc_panel2.fill = GridBagConstraints.BOTH;
		gbc_panel2.gridx = 1;
		gbc_panel2.gridy = 0;
		contentPane.add(panel2, gbc_panel2);
		panel2.setLayout(new BorderLayout(0, 0));
		model1 = new DefaultTableModel();
		model1.addColumn("File name");
		model1.addColumn("Size");
		model1.addColumn("Percent");

		model2 = new DefaultTableModel();
		model2.addColumn("File name");
		model2.addColumn("Size");

		model3 = new DefaultTableModel();
		model3.addColumn("File name");
		model3.addColumn("Size");

		model4 = new DefaultTableModel();
		model4.addColumn("File name");
		model4.addColumn("Size");
		model4.addColumn("Percent");

		JScrollPane scrollPane = new JScrollPane();
		panel2.add(scrollPane, BorderLayout.CENTER);
		model = model1;
		table = new JTable(model);
		scrollPane.setViewportView(table);
		// su kien right click
		RowPopup pop = new RowPopup();
		pop.addActionListener(e -> {
			if (selectedList.getSelectedIndex() == 2) {
				String tenFile = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
				Long size = Long.parseLong(table.getModel().getValueAt(table.getSelectedRow(), 1).toString());
				download(tenFile, size);
			}
		});
		selectedList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				int index = list.getSelectedIndex();

				switch (index) {
				case 0:
					model = model1;

					break;
				case 1:

					model = model2;
					break;
				case 2:

					model = model3;
					break;
				case 3:

					model = model4;
					break;
				default:
					break;
				}
				// System.out.println(table.getComponentPopupMenu());
				table.setModel(model);

			}
		});
		selectedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel1.add(selectedList);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e))
					if (selectedList.getSelectedIndex() == 2)
						pop.show(e.getComponent(), e.getX(), e.getY());
			}
		});

	}

	private JFileChooser chooser;

	protected void addFile() {
		if (chooser == null)
			chooser = new JFileChooser(System.getProperty("user.home"));
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			controller.addFile(file);
		}

	}

	@Override
	public void addDownload(ProgressDownload download) {
		model1.addRow(new Object[] { download.getFile().getName(), download.getFile().getSize(),
				download.getTotalDownload() / download.getFile().getSize() });
		selectModel.getElementAt(0).setNum(model1.getRowCount());
	}

	@Override
	public void removeDownload(ProgressDownload download) {
		for (int i = 0; i < model1.getRowCount(); i++)
			if (download.getFile().getName().equals(model1.getValueAt(i, 0))
					&& download.getFile().getSize() == (long) model1.getValueAt(i, 1))
				model1.removeRow(i);
		selectModel.getElementAt(0).setNum(model1.getRowCount());
	}

	@Override
	public void addNonActiveFile(FileInfo data) {
		model3.addRow(new Object[] { data.getName(), data.getSize() });
		selectModel.getElementAt(2).setNum(model3.getRowCount());
	}

	@Override
	public void updateNonActiveFile(ListFilesInfo listFilesInfo) {
		while (model3.getRowCount() > 0) {
			model3.removeRow(model3.getRowCount() - 1);
		}
		Iterator<FileInfo> iterator = listFilesInfo.iterator();
		while (iterator.hasNext()) {
			FileInfo next = iterator.next();
			this.addNonActiveFile(next);
		}
		selectModel.getElementAt(2).setNum(model3.getRowCount());
	}

	@Override
	public void updateProgress(ProgressDownload obj, long newValue) {
		for (int i = 0; i < model1.getRowCount(); i++)
			if (obj.getFile().getName().equals(model1.getValueAt(i, 0))
					&& obj.getFile().getSize() == (long) model1.getValueAt(i, 1)) {
				model1.setValueAt(((float) newValue / (long) model1.getValueAt(i, 1)) * 100, i, 2);
			}
	}

	@Override
	public void addActiveFile(FileInfo newValue) {
		model2.addRow(new Object[] { newValue.getName(), newValue.getSize() });
		selectModel.getElementAt(1).setNum(model2.getRowCount());
	}

	@Override
	public void updateActiveFile(ListFilesInfo listFilesInfo) {
		for (int i = 0; i < model2.getRowCount(); i++)

			model2.removeRow(i);

		Iterator<FileInfo> iterator = listFilesInfo.iterator();
		while (iterator.hasNext()) {
			FileInfo next = iterator.next();
			this.addActiveFile(next);
		}
		selectModel.getElementAt(1).setNum(model2.getRowCount());
	}

	private void download(String name, Long size) {
		controller.requestFile(new FileInfo(name, size));
	}

	@Override
	public void addSendingFile(ProgressUpload upload) {
		model4.addRow(new Object[] { upload.getFile().getName(), upload.getFile().getSize(),
				upload.getTotalDownload() / upload.getFile().getSize() });
		selectModel.getElementAt(3).setNum(model4.getRowCount());

	}

	@Override
	public void removeSendingFile(ProgressUpload upload) {
		for (int i = 0; i < model4.getRowCount(); i++)
			if (upload.getFile().getName().equals(model4.getValueAt(i, 0))
					&& upload.getFile().getSize() == (long) model4.getValueAt(i, 1))
				model4.removeRow(i);
		selectModel.getElementAt(3).setNum(model4.getRowCount());

	}

	@Override
	public void updateProgressUpload(ProgressUpload element, long newValue) {
		for (int i = 0; i < model4.getRowCount(); i++)
			if (element.getFile().getName().equals(model4.getValueAt(i, 0))
					&& element.getFile().getSize() == (long) model4.getValueAt(i, 1)) {
				model4.setValueAt(((float) newValue / (long) model4.getValueAt(i, 1)) * 100, i, 2);
			}
		selectModel.getElementAt(3).setNum(model4.getRowCount());
	}

	private WaitForm waitform = new WaitForm();

	@Override
	public void displayWaiting() {
		waitform.setVisible(true);
	}

	@Override
	public void finishDisplayWaiting() {
		waitform.setVisible(false);
	}
}

class RowPopup extends JPopupMenu {

	private JMenuItem download;

	public RowPopup() {
		download = new JMenuItem("Tải về");

		add(download);

	}

	public void addActionListener(ActionListener listener) {
		download.addActionListener(listener);
	}
}
