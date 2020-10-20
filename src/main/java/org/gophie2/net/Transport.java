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
public interface Transport {

    /**
     * Downloads content through gopher and stores it in the define target file
     *
     * @param url Url to download the content from
     *
     * @param targetFile The file to write the content to
     *
     * @param eventListener Listener to report the status to
     */
    void downloadAsync(String url, String targetFile, TransportEventListener eventListener);

    /**
     * Fetches a gopher page asynchronously
     *
     * @param url the url of the gopher page to fetch
     *
     * @param contentType the expected content type of the url
     *
     * @param eventListener the listener to report the result to
     */
    void fetchAsync(String url, GopherMenuItemType contentType, TransportEventListener eventListener);

    /**
     * Fetches a gopher page
     *
     * @param url the url of the page to fetch
     *
     * @param contentType the expected content type
     *
     * @param eventListener event listener to report progress to
     *
     * @return the fetched gopher page object
     *
     * @throws GopherNetworkException Exception with network information
     */
    GopherMenu fetch(String url, GopherMenuItemType contentType, TransportEventListener eventListener) throws GopherNetworkException;

    /**
     * Cancels a current fetch operation
     */
    void cancel();
}
