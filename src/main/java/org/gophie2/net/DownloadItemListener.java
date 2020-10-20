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
package org.gophie2.net;

import org.gophie2.net.event.TransportEventListener;

/**
 *
 * @author malyshev
 */
public class DownloadItemListener implements TransportEventListener {

    private final DownloadItem item;

    /* local variables for calculating the bit rate
        at which the download currently operates */
    private long startTimeMillis;
    private long bytePerSecond;

    private Boolean openFile;
    private long byteCountLoaded;

    public DownloadItemListener(DownloadItem item) {
        this(item, false);
    }

    public DownloadItemListener(DownloadItem item, Boolean openWhenFinished) {
        this.item = item;
        startTimeMillis = 0;
        bytePerSecond = 0;
        openFile = openWhenFinished;
        byteCountLoaded = 0;
    }

    /**
     * Returns the number of bytes loaded
     *
     * @return The number of bytes loaded as long
     */
    public long getByteCountLoaded() {
        return byteCountLoaded;
    }

    public long getBytePerSecond() {
        return bytePerSecond;
    }

    @Override
    public void progress(GopherUrl url, long byteCount) {
        /* set start time to calculate total duration */
        if (startTimeMillis == 0) {
            /* use time in milliseconds */
            startTimeMillis = System.currentTimeMillis();
        }

        /* calculate the bitrate of the download */
        long timeNow = System.currentTimeMillis();
        long duration = ((timeNow - startTimeMillis) / 1000);
        if (duration > 0 && byteCount > 0) {
            bytePerSecond = (byteCount / duration);
        }

        /* update the local byte counter  */
        byteCountLoaded = byteCount;

        item.notifyProgress();
    }

    @Override
    public void loaded(GopherMenu result) {
        /* set the status to complete */
        item.setStatus(DownloadStatus.COMPLETED);

        /* check if file open was requested */
        if (openFile) {
            item.openFileOnDesktop();
        }

        item.notifyProgress();
    }

    @Override
    public void failed(Error error, GopherUrl url) {
        item.setStatus(DownloadStatus.FAILED);
        item.notifyProgress();
    }
}
