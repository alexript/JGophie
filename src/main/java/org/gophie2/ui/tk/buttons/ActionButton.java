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
package org.gophie2.ui.tk.buttons;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gophie2.config.ColorPalette;

import org.gophie2.config.ConfigurationManager;

public class ActionButton extends JPanel {

    private static final long serialVersionUID = -450010270132659964L;

    private int buttonId = 0;

    private Boolean isEnabledButton = false;
    private final List<ActionButtonEventListener> eventListenerList;

    private final JLabel iconLabel;
    private final JLabel textLabel;

    public ActionButton(String iconText, String text) {

        super();
        eventListenerList = new ArrayList<>();
        iconLabel = new ButtonIcon(iconText);
        textLabel = new ButtonText(text);

        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        add(iconLabel, BorderLayout.WEST);
        add(textLabel, BorderLayout.EAST);
        highlight(false);
        addMouseListener(new ButtonMouseAdapter());
    }

    public final void setButtonId(int value) {
        buttonId = value;
    }

    protected int getButtonId() {
        return buttonId;
    }

    /**
     * Adds a new event listener to this button
     *
     * @param listener The event listener to add to this button
     */
    public final void addEventListener(ActionButtonEventListener listener) {
        eventListenerList.add(listener);
    }

    protected List<ActionButtonEventListener> getListeners() {
        return eventListenerList;
    }

    public void setContent(String iconText, String text) {
        iconLabel.setText(iconText);
        textLabel.setText(text);
    }

    /**
     * Enables or disables the button
     *
     * @param value true means enabled, false is otherwise
     */
    public final void setButtonEnabled(Boolean value) {
        isEnabledButton = value;

        if (value == true) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    protected Boolean isButtonEnabled() {
        return isEnabledButton;
    }

    protected final void highlight(boolean b) {
        ColorPalette colors = ConfigurationManager.getColors();
        Color textColor;
        if (b) {
            textColor = colors.getActionbarText();

        } else {
            textColor = colors.getActionbarInactive();
        }
        iconLabel.setForeground(textColor);
        textLabel.setForeground(textColor);
    }
}
