package org.gophie2.ui.tk.download;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import org.gophie2.config.ColorPalette;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.net.DownloadItem;

import org.gophie2.net.Downloads;
import org.gophie2.ui.tk.buttons.ActionButtonEventListener;
import org.gophie2.net.event.DownloadsEventListener;

public class DownloadWindow implements ActionButtonEventListener {

    private final Downloads list;
    private DownloadItem[] data;

    private JDialog frame;
    private final JList<DownloadItem> fileListView;
    private final ToolBar toolBar;

    public static final DownloadWindow INSTANCE = new DownloadWindow();

    private DownloadWindow() {
        this(Downloads.INSTANCE);
    }

    private DownloadWindow(Downloads downloadList) {

        ColorPalette colors = ConfigurationManager.getColors();

        list = downloadList;
        list.addEventListener(new DownloadsEventListener() {
            @Override
            public void updated() {
                updateList();
            }

            @Override
            public void progressReported() {
                DownloadItem selected = fileListView.getSelectedValue();
                toolBar.handleSelectionChange(selected, list.hasNonActiveItems());
                frame.repaint();
            }
        });

        frame = new JDialog();
        frame.setTitle("Downloads");
        frame.setMinimumSize(new Dimension(400, 200));
        frame.setLayout(new BorderLayout());

        fileListView = new JList<>();
        fileListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileListView.setCellRenderer(new DownloadItemRenderer());
        fileListView.setFixedCellWidth(fileListView.getWidth());
        fileListView.setOpaque(true);
        fileListView.setBackground(colors.getFilelistBackground());

        JScrollPane listScrollPane = new JScrollPane(fileListView);
        listScrollPane.setOpaque(false);
        listScrollPane.getViewport().setOpaque(false);
        frame.add(listScrollPane, BorderLayout.CENTER);

        toolBar = new ToolBar(this);
        frame.add(toolBar, BorderLayout.SOUTH);

        fileListView.addListSelectionListener((ListSelectionEvent e) -> {
            DownloadItem selected = fileListView.getSelectedValue();
            toolBar.handleSelectionChange(selected, list.hasNonActiveItems());
        });

        updateList();
    }

    private void updateList() {
        data = list.getDownloadItemArray();

        int selectedIndex = fileListView.getSelectedIndex();
        fileListView.setListData(data);

        if (selectedIndex < data.length) {
            fileListView.setSelectedIndex(selectedIndex);
        } else {
            if (data.length > 0) {
                fileListView.setSelectedIndex(data.length - 1);
            }
        }
        DownloadItem selected = fileListView.getSelectedValue();
        toolBar.handleSelectionChange(selected, list.hasNonActiveItems());
    }

    public boolean isVisible() {
        return frame.isVisible();
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void show(JFrame parent) {
        updateList();
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);
    }

    @Override
    public void buttonPressed(int buttonId) {
        if (buttonId == 0) {
            DownloadItem item = fileListView.getSelectedValue();

            switch (item.getStatus()) {
                case ACTIVE:
                    item.cancel();
                    list.remove(item);
                    item.deleteFile();
                    break;
                case FAILED:
                    item.start();
                    break;
                case COMPLETED:
                    item.openFileOnDesktop();
                    break;
                case IDLE:
                    item.start();
                    break;
            }

        }
        if (buttonId == 1) {
            list.clearNonActiveItems();
        }
        updateList();
    }
}
