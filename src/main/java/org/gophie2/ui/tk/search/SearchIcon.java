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

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.fonts.IconFont;

/**
 *
 * @author malyshev
 */
public class SearchIcon extends JLabel {

    private static final long serialVersionUID = 1230678513627465485L;

    public SearchIcon() {
        super("");
        init();
    }

    private void init() {
        Font iconFont = new IconFont(ConfigurationManager.getConfigFile().getFloat("Fonts", "SEARCH_ICONS_SIZE", 16f));
        setFont(iconFont);
        setBorder(new EmptyBorder(0, 0, 0, 8));
        setForeground(ConfigurationManager.getColors().getSearchTitle());
    }
}
