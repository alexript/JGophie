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

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.gophie2.config.ColorPalette;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.net.DownloadItem;
import org.gophie2.ui.tk.buttons.ActionButtonEventListener;

/**
 *
 * @author malyshev
 */
public class ToolBar extends JPanel {

    private static final long serialVersionUID = 1011082521184124592L;
    private final ClearButton clearButton;
    private final MainButton mainButton;

    public ToolBar(ActionButtonEventListener listener) {
        super();
        clearButton = new ClearButton(listener);
        mainButton = new MainButton(listener);
        init();
    }

    private void init() {
        ColorPalette colors = ConfigurationManager.getColors();
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8, 16, 10, 16));
        setBackground(colors.getActionbarBackground());
        add(clearButton, BorderLayout.EAST);
        add(mainButton, BorderLayout.WEST);
    }

    protected void handleSelectionChange(DownloadItem selected, boolean hasNonActiveItems) {

        if (selected == null) {
            mainButton.setVisible(false);
        } else {
            mainButton.applyStatus(selected.getStatus());

            mainButton.setVisible(true);
            mainButton.setButtonEnabled(true);
        }

        clearButton.setButtonEnabled(hasNonActiveItems);

    }
}
