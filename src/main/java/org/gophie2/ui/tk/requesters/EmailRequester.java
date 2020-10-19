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
public class EmailRequester implements Requester {

    public EmailRequester() {

    }

    @Override
    public void request(MessageDisplayer messenger, String addressText, GopherItem item) {
        String emailAddress = addressText.replace("mailto:", "");
        String confirmText = "Do you want to send an e-mail to \"" + emailAddress + "\"?";
        String[] optionList = new String[]{"Create new e-mail", "Dismiss"};
        messenger.showConfirm(confirmText, optionList, (int option) -> {
            if (option == 0) {
                /* launch the system email client */
                if (Desktop.isDesktopSupported() == true
                        && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        /* launch the mailto handler of the system */
                        Desktop.getDesktop().browse(new URI("mailto:" + emailAddress));
                    } catch (IOException | URISyntaxException ex) {
                        /* Error: cannot open email client */
                        System.out.println("Unable to open system's "
                                + "email client: " + ex.getMessage());
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
