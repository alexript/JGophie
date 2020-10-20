/*
    This file is part of Gophie.

    Gophie is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gophie is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Gophie. If not, see <https://www.gnu.org/licenses/>.

 */
package org.gophie2.net;

import java.util.ArrayList;
import java.util.List;
import org.gophie2.net.event.DownloadItemEventListener;
import org.gophie2.ui.DesktopUtils;

public class DownloadItem {


    /* local objects and variables */
    private GopherItem item;
    private final GopherClient client;
    private String fileName;

    private DownloadStatus status = DownloadStatus.IDLE;
    private final List<DownloadItemEventListener> eventListenerList;
    private DownloadItemListener downloadItemListener;

    /**
     * Constructor creates the download and starts it immediately
     *
     * @param gopherItem The gopher item to download
     *
     * @param targetFile The file to write the contents to
     *
     * @param openWhenFinished If true, opens the file when finished
     */
    public DownloadItem(GopherItem gopherItem, String targetFile, Boolean openWhenFinished) {
        eventListenerList = new ArrayList<>();
        client = new GopherClient();
        status = DownloadStatus.IDLE;
        item = gopherItem;
        fileName = targetFile;
        downloadItemListener = new DownloadItemListener(this, openWhenFinished);
        start();
    }

    public void addEventListener(DownloadItemEventListener listener) {
        eventListenerList.add(listener);
    }

    protected void notifyProgress() {
        eventListenerList.forEach((listener) -> {
            listener.downloadProgressReported();
        });
    }

    /**
     * Starts the download of the file
     */
    public final void start() {
        /* start the download process */
        client.downloadAsync(item.getUrlString(), fileName, new DownloadItemListener(this));
        status = DownloadStatus.ACTIVE;
    }

    /**
     * Tries to delete the file
     */
    public void deleteFile() {
        DesktopUtils.deleteFile(fileName);
    }

    /**
     * Cancels this download
     *
     */
    public void cancel() {
        client.cancelFetch();
    }

    /**
     * Sets the target file to download to
     *
     * @param targetFile Path of the file to store data in
     */
    public void setTargetFile(String targetFile) {
        fileName = targetFile;
    }

    /**
     * Sets the gopher item
     *
     * @param gopherItem The gopher item to download
     */
    public void setGopherItem(GopherItem gopherItem) {
        item = gopherItem;
    }

    /**
     * Returns the gopher item
     *
     * @return Returns the gopher item to download
     */
    public GopherItem getGopherItem() {
        return item;
    }

    /**
     * Returns the status of this download
     *
     * @return The status as DownloadStatus enum
     */
    public DownloadStatus getStatus() {
        return status;
    }

    protected void setStatus(DownloadStatus status) {
        this.status = status;
    }

    public long getByteCountLoaded() {
        return downloadItemListener.getByteCountLoaded();
    }

    public long getBytePerSecond() {
        return downloadItemListener.getBytePerSecond();
    }

    /**
     * Opens the file on the users desktop
     */
    public void openFileOnDesktop() {
        DesktopUtils.openFileOnDesktop(fileName);
    }

}
