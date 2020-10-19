/*
 * Copyright (C) 2020 malyshev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gophie2.ui.tk.download;

import java.awt.FileDialog;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.net.DownloadItem;
import org.gophie2.net.DownloadList;
import org.gophie2.net.GopherItem;
import org.gophie2.ui.MainWindow;
import org.gophie2.ui.MessageDisplayer;
import org.gophie2.ui.event.PageMenuEventAdapter;
import org.gophie2.ui.tk.requesters.Requester;

/**
 *
 * @author malyshev
 */
public class ConfirmDownload extends PageMenuEventAdapter implements Requester {

    private final DownloadList downloadList;
    private final MainWindow parent;

    public ConfirmDownload(MainWindow parent) {
        this.parent = parent;
        downloadList = DownloadList.INSTANCE;

    }

    @Override
    public void request(MessageDisplayer messenger, String addressText, GopherItem item) {
        /* binary files are handled by the download manager */
        String confirmText = "Download \"" + item.getFileName()
                + "\" from \"" + item.getHostName() + "\"?";
        String[] optionList = new String[]{"Open", "Save", "Dismiss"};
        messenger.showConfirm(confirmText, optionList, (int option) -> {
            if (option == 0) {
                /* store file to download directory and open */
                String targetFileName = ConfigurationManager.getDownloadPath() + item.getFileName();
                downloadList.add(new DownloadItem(item, targetFileName, true));

                /* hide the message view */
                messenger.setVisible(false);
            }
            if (option == 1) {
                /* initiate the download */
                initiateDownload(item);

                /* hide the message view */
                messenger.setVisible(false);
            }

            /* hide the message view */
            messenger.setVisible(false);
        });
    }

    private void initiateDownload(GopherItem fileItem) {
        /* let user select where to store the file */
        FileDialog fileDialog = new FileDialog(parent.getMainWindowFrame(), "Download and save file", FileDialog.SAVE);
        fileDialog.setFile(fileItem.getFileNameWithForcedExt());
        fileDialog.setVisible(true);
        String targetFileName = fileDialog.getDirectory() + fileDialog.getFile();
        if (targetFileName.equals("null") == false
                && targetFileName.equals("nullnull") == false) {
            /* pass url and target file to download manager */
            downloadList.add(new DownloadItem(fileItem, targetFileName, false));
        }
    }

    @Override
    public void itemDownloadRequested(GopherItem item) {
        initiateDownload(item);
    }
}
