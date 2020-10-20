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

import org.gophie2.net.GopherMenuItem;
import org.gophie2.ui.DesktopUtils;
import org.gophie2.ui.MessageDisplayer;

/**
 *
 * @author malyshev
 */
public class EmailRequester implements Requester {

    public EmailRequester() {

    }

    @Override
    public void request(MessageDisplayer messenger, String addressText, GopherMenuItem item) {
        String emailAddress = addressText.replace("mailto:", "");
        String confirmText = "Do you want to send an e-mail to \"" + emailAddress + "\"?";
        String[] optionList = new String[]{"Create new e-mail", "Dismiss"};
        messenger.showConfirm(confirmText, optionList, (int option) -> {
            if (option == 0) {
                DesktopUtils.browse("mailto:" + emailAddress);
                messenger.setVisible(false);
            } else {
                messenger.setVisible(false);
            }
        });
    }

}
