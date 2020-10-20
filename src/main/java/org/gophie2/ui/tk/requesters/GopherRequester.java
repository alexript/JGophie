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
package org.gophie2.ui.tk.requesters;

import org.gophie2.net.GopherClient;
import org.gophie2.net.GopherItem;
import org.gophie2.net.GopherItemType;
import org.gophie2.net.GopherTransport;
import org.gophie2.ui.MainWindow;
import org.gophie2.ui.MessageDisplayer;
import org.gophie2.ui.NavigationBar;

/**
 *
 * @author malyshev
 */
public class GopherRequester implements Requester {

    private final GopherTransport gopherClient;
    private final MainWindow parent;
    private final NavigationBar navigation;

    public GopherRequester(MainWindow parent, NavigationBar navigationBar) {
        this.parent = parent;
        this.navigation = navigationBar;
        gopherClient = new GopherClient();

    }

    public void cancel() {
        gopherClient.cancel();
    }

    @Override
    public void request(MessageDisplayer messenger, String addressText, GopherItem item) {
        request(messenger, addressText, item.getItemType());
    }

    public void request(MessageDisplayer messenger, String addressText, GopherItemType itemType) {
        /* this is default gopher content */
 /* activate the load indicator in the address bar */
        navigation.setIsLoading(true);

        /* update the navigation bar with the new address */
        navigation.setAddressText(addressText);

        try {
            /* try to execute the thread */
            gopherClient.fetchAsync(addressText, itemType, parent);
        } catch (Exception ex) {
            /* might throw an ex when thread is interrupted */
            System.out.println("Exception while fetching async: " + ex.getMessage());
        }
    }

}
