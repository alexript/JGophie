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
package org.gophie2.ui.tk.buttons.internal;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author malyshev
 */
public class ButtonMouseAdapter extends MouseAdapter {

    /* notify the listeners of the button pressed event */
    @Override
    public void mouseReleased(MouseEvent evt) {
        ActionButton btn = getSource(evt);
        if (btn == null) {
            return;
        }
        /* will be handled by another handler */
        btn.getListeners().forEach((listener) -> {
            listener.buttonPressed(btn.getButtonId());
        });
    }

    /* set the color to the hover color and use the hand cursor */
    @Override
    public void mouseEntered(MouseEvent evt) {
        ActionButton btn = getSource(evt);
        if (btn == null) {
            return;
        }
        /* only show hover effect when button is enabled */
        if (btn.isButtonEnabled()) {
            btn.highlight(true);

        }
    }

    /* revert back to the default cursor and default color */
    @Override
    public void mouseExited(MouseEvent evt) {
        ActionButton btn = getSource(evt);
        if (btn == null) {
            return;
        }
        btn.highlight(false);
    }

    private ActionButton getSource(MouseEvent evt) {
        Object source = evt.getSource();
        if (source instanceof ActionButton) {
            return (ActionButton) source;
        }
        return null;
    }
}
