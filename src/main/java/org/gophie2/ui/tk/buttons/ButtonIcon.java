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

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import org.gophie2.config.ColorPalette;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.fonts.IconFont;

/**
 *
 * @author malyshev
 */
public class ButtonIcon extends JLabel {

    private static final long serialVersionUID = 5563587044347970205L;

    public ButtonIcon(String icoText) {
        super(icoText);
        init();
    }

    private void init() {
        ColorPalette colors = ConfigurationManager.getColors();

        setBorder(new EmptyBorder(0, 0, 0, 6));
        setOpaque(false);
        setFont(new IconFont(ConfigurationManager.getConfigFile().getFloat("Fonts", "ACTION_ICONS_SIZE", 14f)));

        setForeground(colors.getActionbarInactive());
    }
}
