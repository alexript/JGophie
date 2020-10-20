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

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.gophie2.net.event.TransportEventListener;

public class GopherTransport implements Transport {

    /* thread with the active fetch process */
    private Thread thread;
    private Boolean cancelled;

    public GopherTransport() {
        cancelled = false;
    }

    @Override
    public void cancel() {
        if (this.thread != null) {
            this.thread.interrupt();
            this.cancelled = true;
        }
    }

    /**
     * Returns whether the current operation was cancelled or not
     *
     * @return true when cancelled, false otherwise
     */
    private Boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void downloadAsync(String url, String targetFile, TransportEventListener eventListener) {
        /* instanciate the new thread */
        GopherTransport clientObject = this;
        this.thread = new Thread(() -> {
            try {

                try (OutputStream fileStream = new FileOutputStream(new File(targetFile))) {

                    GopherUrl gopherUrl = new GopherUrl(url);
                    try (Socket gopherSocket = new Socket(gopherUrl.getHost(), gopherUrl.getPort())) {
                        byte[] gopherRequest = (gopherUrl.getSelector() + "\r\n").getBytes(StandardCharsets.US_ASCII);
                        (new DataOutputStream(gopherSocket.getOutputStream())).write(gopherRequest);
                        /* read byte in chunks and report progress */
                        int read;
                        InputStream socketStream = gopherSocket.getInputStream();
                        byte[] data = new byte[16384];
                        long totalByteCount = 0;
                        /* read byte by byte to be able to report progress */
                        while ((read = socketStream.read(data, 0, data.length)) != -1) {
                            fileStream.write(data, 0, read);

                            /* calculate total bytes read */
                            totalByteCount = totalByteCount + data.length;

                            /* report byte count to listener */
                            if (!clientObject.isCancelled()) {
                                if (eventListener != null) {
                                    eventListener.progress(gopherUrl, totalByteCount);
                                }
                            }
                        }
                    }
                }

                if (!clientObject.isCancelled()) {
                    if (eventListener != null) {
                        eventListener.loaded(null);
                    }
                }
            } catch (IOException ex) {
                /* log the exception message */
                System.out.println("Download failed (" + url + "):" + ex.getMessage());

                /* remove the file if already created */
                File createdFile = new File(targetFile);
                if (createdFile.exists()) {
                    createdFile.delete();
                }

                /* notify the handlers */
                if (!clientObject.isCancelled()) {
                    if (eventListener != null) {
                        eventListener.failed(Error.EXCEPTION, new GopherUrl(url));
                    }
                }
            }
        });

        /* start the new thread */
        this.thread.start();
    }

    @Override
    public void fetchAsync(String url, GopherMenuItemType contentType, TransportEventListener eventListener) {
        /* instanciate the new thread */
        GopherTransport clientObject = this;
        this.thread = new Thread(() -> {
            try {
                GopherMenu resultPage = fetch(url, contentType, eventListener);

                if (!clientObject.isCancelled()) {
                    if (eventListener != null) {
                        eventListener.loaded(resultPage);
                    }
                }
            } catch (GopherNetworkException ex) {
                if (!clientObject.isCancelled()) {
                    if (eventListener != null) {
                        eventListener.failed(ex.getGopherErrorType(), new GopherUrl(url));
                    }
                }
            }
        });

        /* start the new thread */
        this.thread.start();
    }

    @Override
    public GopherMenu fetch(String url, GopherMenuItemType contentType, TransportEventListener eventListener) throws GopherNetworkException {
        GopherMenu result = null;

        try {
            /* string result with content */
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            /* parse the url and instanciate the client */
            GopherUrl gopherUrl = new GopherUrl(url);
            try (Socket gopherSocket = new Socket(gopherUrl.getHost(), gopherUrl.getPort())) {
                byte[] gopherRequest = (gopherUrl.getSelector() + "\r\n").getBytes(StandardCharsets.US_ASCII);
                (new DataOutputStream(gopherSocket.getOutputStream())).write(gopherRequest);
                /* read byte in chunks and report progress */
                int read;
                InputStream socketStream = gopherSocket.getInputStream();
                byte[] data = new byte[16384];
                long totalByteCount = 0;
                /* read byte by byte to be able to report progress */
                while ((read = socketStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, read);

                    /* calculate total bytes read */
                    totalByteCount = totalByteCount + data.length;

                    /* report byte count to listener */
                    if (!this.isCancelled()) {
                        if (eventListener != null) {
                            eventListener.progress(gopherUrl, totalByteCount);
                        }
                    }
                }
                /* close the socket to the server */
            }

            /* set the result page */
            result = new GopherMenu(buffer.toByteArray(), contentType, gopherUrl);
        } catch (ConnectException ex) {
            /* handle host connection errors */
            throw new GopherNetworkException(Error.CONNECT_FAILED, ex.getMessage());
        } catch (UnknownHostException ex) {
            /* handle host not found exception */
            throw new GopherNetworkException(Error.HOST_UNKNOWN, ex.getMessage());
        } catch (SocketTimeoutException ex) {
            /* handle host not found exception */
            throw new GopherNetworkException(Error.CONNECTION_TIMEOUT, ex.getMessage());
        } catch (IOException ex) {
            /* handle the error properly and raise and event */
            System.out.println("GOPHER NETWORK EXCEPTION: " + ex.getMessage());
            throw new GopherNetworkException(Error.EXCEPTION, ex.getMessage());
        }

        return result;
    }
}
