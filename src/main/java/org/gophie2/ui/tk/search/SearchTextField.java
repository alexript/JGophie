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
package org.gophie2.ui.tk.search;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.gophie2.config.ColorPalette;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.fonts.DefaultFont;

/**
 *
 * @author malyshev
 */
public class SearchTextField extends JTextField {

    private static final long serialVersionUID = -370287179587150041L;

    public SearchTextField() {
        super();
        init();
    }

    private void init() {
        ColorPalette colors = ConfigurationManager.getColors();

        setBorder(new EmptyBorder(2, 0, 0, 0));
        setBackground(colors.getSearchBackground());
        setForeground(colors.getSearchText());
        setCaretColor(colors.getSearchText());
        setFont(new DefaultFont(ConfigurationManager.getConfigFile().getFloat("Fonts", "SEARCHPANEL_TEXT_SIZE", 14f)));
    }
}
