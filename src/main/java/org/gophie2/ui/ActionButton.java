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
package org.gophie2.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

import org.gophie2.config.ConfigurationManager;
import org.gophie2.ui.event.ActionButtonEventListener;

public class ActionButton extends JPanel {

    private static final long serialVersionUID = 1L;

    /* private variables */
    private int buttonId = 0;
    private Color inactiveTextColor;
    private Color textColor;
    private Boolean isEnabledButton = false;
    private ArrayList<ActionButtonEventListener> eventListenerList;

    /* private components */
    private JLabel iconLabel;
    private JLabel textLabel;

    public ActionButton(String iconText, String text, Color textColorHex, Color inactiveTextColorHex) {
        /* construct the base */
        super();
        this.eventListenerList = new ArrayList<>();

        /* set text colors locally */
        this.textColor = textColorHex;
        this.inactiveTextColor = inactiveTextColorHex;

        /* configure the layout for this button */
        this.setLayout(new BorderLayout());
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setOpaque(false);

        /* icon for the button using the icon font */
        this.iconLabel = new JLabel(iconText);
        this.iconLabel.setBorder(new EmptyBorder(0, 0, 0, 6));
        this.iconLabel.setOpaque(false);
        this.iconLabel.setFont(ConfigurationManager.getIconFont(14f));
        this.iconLabel.setForeground(inactiveTextColorHex);
        this.add(iconLabel, BorderLayout.WEST);

        /* text for the button using the default text font */
        this.textLabel = new JLabel(text);
        this.textLabel.setOpaque(false);
        this.textLabel.setFont(ConfigurationManager.getDefaultFont(12f));
        this.textLabel.setForeground(inactiveTextColorHex);
        this.add(textLabel, BorderLayout.EAST);

        this.addMouseListener(new MouseAdapter() {
            /* notify the listeners of the button pressed event */
            @Override
            public void mouseReleased(MouseEvent evt) {
                /* will be handled by another handler */
                eventListenerList.forEach((listener) -> {
                    listener.buttonPressed(buttonId);
                });
            }

            /* set the color to the hover color and use the hand cursor */
            @Override
            public void mouseEntered(MouseEvent evt) {
                /* only show hover effect when button is enabled */
                if (isButtonEnabled() == true) {
                    iconLabel.setForeground(textColor);
                    textLabel.setForeground(textColor);
                }
            }

            /* revert back to the default cursor and default color */
            @Override
            public void mouseExited(MouseEvent evt) {
                iconLabel.setForeground(inactiveTextColor);
                textLabel.setForeground(inactiveTextColor);
            }
        });
    }

    public void setButtonId(int value) {
        this.buttonId = value;
    }

    /**
     * Adds a new event listener to this button
     *
     * @param listener The event listener to add to this button
     */
    public void addEventListener(ActionButtonEventListener listener) {
        this.eventListenerList.add(listener);
    }

    public void setContent(String iconText, String text) {
        this.iconLabel.setText(iconText);
        this.textLabel.setText(text);
    }

    public void setTextColor(Color colorHex) {
        this.textColor = colorHex;
    }

    /**
     * Sets the color for inactive/disabled button
     *
     * @param colorHex The color in hex format
     */
    public void setInactiveTextColor(Color colorHex) {
        this.inactiveTextColor = colorHex;
    }

    /**
     * Enables or disables the button
     *
     * @param value true means enabled, false is otherwise
     */
    public void setButtonEnabled(Boolean value) {
        this.isEnabledButton = value;

        if (value == true) {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public Boolean isButtonEnabled() {
        return this.isEnabledButton;
    }
}
