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

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import org.gophie2.config.ConfigFile;
import org.gophie2.config.ConfigurationManager;
import static org.gophie2.ui.tk.search.SearchPanel.SEARCH_TITLECOLOR;

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
        ConfigFile configFile = ConfigurationManager.getConfigFile();
        setFont(ConfigurationManager.getIconFont(16f));
        setBorder(new EmptyBorder(0, 0, 0, 8));
        setForeground(configFile.getColor("Appearance", "SEARCH_TITLECOLOR", SEARCH_TITLECOLOR));
    }
}
