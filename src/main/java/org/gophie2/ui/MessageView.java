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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import org.gophie2.config.ColorPalette;
import org.gophie2.config.ColorPalette.PColor;
import org.gophie2.config.ConfigurationManager;

import org.gophie2.ui.event.MessageViewListener;

public class MessageView extends JPanel {

    private static final long serialVersionUID = 7668873418937653664L;

    private final JLabel messageIcon;
    private final JLabel messageText;
    private final JPanel buttonPanel;
    private final Font iconFont;

    public MessageView() {

        ColorPalette colors = ConfigurationManager.getColors();

        /* get the icon font for this navigation bar */
        this.iconFont = ConfigurationManager.getIconFont(19f);

        /* set box layout for this message view */
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 10, 10, 16));
        this.setBackground(colors.getMessageviewBackground());

        /* create the label instance */
        this.messageIcon = new JLabel();
        this.messageIcon.setFont(this.iconFont);
        this.messageIcon.setBorder(new EmptyBorder(0, 5, 0, 10));
        this.messageIcon.setForeground(colors.getMessageviewText());

        this.messageText = new JLabel();
        this.messageText.setFont(ConfigurationManager.getDefaultFont(11f));
        this.messageText.setForeground(colors.getMessageviewText());

        this.buttonPanel = new JPanel();
        this.buttonPanel.setBorder(new EmptyBorder(0, 30, 0, 0));
        this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, BoxLayout.X_AXIS));
        this.buttonPanel.setBackground(colors.getMessageviewBackground());

        this.add(this.messageIcon, BorderLayout.WEST);
        this.add(this.messageText, BorderLayout.CENTER);
        this.add(this.buttonPanel, BorderLayout.EAST);

        /* do not show by default */
        this.setVisible(false);
    }

    private JLabel createButton(String text) {

        PColor buttonColor = ConfigurationManager.getColors().getMessageviewText();

        String labelTitle = String.format("<html><div style=\"border:1px solid %s; padding:2px 6px 2px 6px;border-radius:6px;\">%s</div></html>", buttonColor, text);
        JLabel customButton = new JLabel(labelTitle);
        customButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customButton.setFont(ConfigurationManager.getDefaultFont(11f));
        customButton.setBorder(new EmptyBorder(0, 5, 0, 5));
        customButton.setForeground(buttonColor);
        return customButton;
    }

    public void showInfo(String text) {
        /* build up icon and text */
        this.messageIcon.setText("");
        this.messageText.setText(text);
        this.buttonPanel.removeAll();

        /* add the handle to close this message */
        JLabel closeButton = this.createButton("Close");
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent evt) {
                setVisible(false);
            }
        });

        /* create and set to visible */
        this.buttonPanel.add(closeButton);
        this.setVisible(true);
    }

    public void showConfirm(String text, String[] optionList, MessageViewListener eventListener) {
        /* remove all components */
        this.messageIcon.setText("");
        this.messageText.setText(text);

        /* build the option buttons */
        this.buttonPanel.removeAll();
        for (int i = 0; i < optionList.length; i++) {
            JLabel optionButton = this.createButton(optionList[i]);
            final int optionId = i;
            optionButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent evt) {
                    eventListener.optionSelected(optionId);
                }
            });

            this.buttonPanel.add(optionButton);
        }

        this.setVisible(true);
    }
}
