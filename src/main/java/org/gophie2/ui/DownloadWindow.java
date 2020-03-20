package org.gophie2.ui;

import org.gophie2.ui.tk.buttons.internal.ActionButton;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import org.gophie2.config.ColorPalette;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.net.DownloadItem;

import org.gophie2.net.DownloadItem.DownloadStatus;
import org.gophie2.net.DownloadList;
import org.gophie2.net.event.DownloadListEventListener;
import org.gophie2.ui.tk.buttons.internal.ActionButtonEventListener;
import org.gophie2.ui.tk.buttons.AbortButton;
import org.gophie2.ui.tk.buttons.ClearButton;

public class DownloadWindow implements ActionButtonEventListener {


    /* local objects */
    private final DownloadList list;
    private DownloadItem[] data;

    /* local components */
    private JDialog frame;
    private final JList<DownloadItem> fileListView;
    private final JPanel actionBar = new JPanel();
    private final ActionButton clearButton;
    private final ActionButton actionButton;

    public DownloadWindow(DownloadList downloadList) {

        ColorPalette colors = ConfigurationManager.getColors();

        list = downloadList;
        list.addEventListener(new DownloadListEventListener() {
            @Override
            public void downloadListUpdated() {
                updateList();
            }

            @Override
            public void downloadProgressReported() {
                handleSelectionChange();
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

        clearButton = new ClearButton(this);
        actionButton = new AbortButton(this);

        actionBar.setLayout(new BorderLayout());
        actionBar.setBorder(new EmptyBorder(8, 16, 10, 16));
        actionBar.setBackground(colors.getActionbarBackground());
        actionBar.add(clearButton, BorderLayout.EAST);
        actionBar.add(actionButton, BorderLayout.WEST);
        frame.add(actionBar, BorderLayout.SOUTH);

        /* hide the action button for empty lists */
        actionButton.setVisible(false);

        fileListView.addListSelectionListener((ListSelectionEvent e) -> {
            handleSelectionChange();
        });

        /* update the list for the first time */
        updateList();
    }

    private void handleSelectionChange() {
        DownloadItem selected = fileListView.getSelectedValue();
        if (selected == null) {
            actionButton.setVisible(false);
        } else {
            if (selected.getStatus() == DownloadStatus.ACTIVE) {
                actionButton.setContent("", "Abort");
            }
            if (selected.getStatus() == DownloadStatus.FAILED) {
                actionButton.setContent("", "Retry");
            }
            if (selected.getStatus() == DownloadStatus.COMPLETED) {
                actionButton.setContent("", "Open");
            }
            if (selected.getStatus() == DownloadStatus.IDLE) {
                actionButton.setContent("", "Start");
            }

            actionButton.setVisible(true);
            actionButton.setButtonEnabled(true);
        }

        /* disable the clear list button for empty lists */
        if (list.hasNonActiveItems()) {
            clearButton.setButtonEnabled(true);
        } else {
            clearButton.setButtonEnabled(false);
        }
    }

    public void updateList() {
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

        handleSelectionChange();
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
            /* the action button */
            DownloadItem item = fileListView.getSelectedValue();
            if (item.getStatus() == DownloadStatus.ACTIVE) {
                /* cancel the currently active item */
                item.cancel();

                /* remove the item from the list */
                list.remove(item);

                /* delete the file form disk */
                item.deleteFile();
            }
            if (item.getStatus() == DownloadStatus.FAILED) {
                /* retry failed item */
                item.start();
            }
            if (item.getStatus() == DownloadStatus.COMPLETED) {
                /* open completed item file */
                item.openFileOnDesktop();
            }
            if (item.getStatus() == DownloadStatus.IDLE) {
                /* start item in idle item */
                item.start();
            }
        }
        if (buttonId == 1) {
            /* the clear list button */
            list.clearNonActiveItems();
        }

        /* update our local list */
        updateList();
    }
}
