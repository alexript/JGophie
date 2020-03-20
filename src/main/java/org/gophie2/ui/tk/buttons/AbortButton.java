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
package org.gophie2.ui.tk.buttons;

import org.gophie2.ui.tk.buttons.internal.ActionButton;
import org.gophie2.ui.tk.buttons.internal.ActionButtonEventListener;

/**
 *
 * @author malyshev
 */
public class AbortButton extends ActionButton {

    private static final long serialVersionUID = -4411521271507688614L;

    public AbortButton(ActionButtonEventListener listener) {
        super("", "Abort");
        setButtonId(0);
        addEventListener(listener);
    }
}