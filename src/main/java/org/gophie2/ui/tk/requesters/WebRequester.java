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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.gophie2.net.GopherItem;
import org.gophie2.ui.MessageDisplayer;

/**
 *
 * @author malyshev
 */
public class WebRequester implements Requester {

    public WebRequester() {

    }

    @Override
    public void request(MessageDisplayer messenger, String addressText, GopherItem item) {
        String confirmText = "Open \"" + addressText + "\" with your web browser?";
        String[] optionList = new String[]{"Open Website", "Dismiss"};
        messenger.showConfirm(confirmText, optionList, (int option) -> {
            if (option == 0) {
                /* launch the system WWW browser */
                if (Desktop.isDesktopSupported() == true
                        && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        /* launch the systems WWW browser */
                        Desktop.getDesktop().browse(new URI(addressText));
                    } catch (IOException | URISyntaxException ex) {
                        /* Error: cannot enjoy bloated javascript
                        stuffed World Wide Web pages! */
                        System.out.println("Unable to open system's "
                                + "world wide web browser: " + ex.getMessage());
                    }
                }
                /* hide the message view */
                messenger.setVisible(false);
            } else {
                /* hide the message view */
                messenger.setVisible(false);
            }
        });
    }

}