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

import org.gophie2.net.DownloadItem;
import org.gophie2.ui.tk.buttons.ActionButton;
import org.gophie2.ui.tk.buttons.ActionButtonEventListener;

/**
 *
 * @author malyshev
 */
public class MainButton extends ActionButton {

    private static final long serialVersionUID = -4411521271507688614L;

    public MainButton(ActionButtonEventListener listener) {
        super("", "Abort");
        init(listener);
    }

    protected void applyStatus(DownloadItem.DownloadStatus status) {
        switch (status) {
            case ACTIVE:
                setContent("", "Abort");
                break;
            case FAILED:
                setContent("", "Retry");
                break;
            case COMPLETED:
                setContent("", "Open");
                break;
            case IDLE:
                setContent("", "Start");
                break;
        }
    }

    private void init(ActionButtonEventListener listener) {
        setButtonId(0);
        setVisible(false);
        addEventListener(listener);
    }
}
