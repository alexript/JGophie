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
import org.gophie2.config.ConfigFile;
import org.gophie2.config.ConfigurationManager;
import static org.gophie2.ui.tk.search.SearchPanel.SEARCH_BACKGROUND;

/**
 *
 * @author malyshev
 */
public class SearchTextField extends JTextField {

    private static final String SEARCH_TEXTCOLOR = "#e8e8e8";
    private static final long serialVersionUID = -370287179587150041L;

    public SearchTextField() {
        super();
        init();
    }

    private void init() {
        ConfigFile configFile = ConfigurationManager.getConfigFile();
        setBorder(new EmptyBorder(2, 0, 0, 0));
        setBackground(configFile.getColor("Appearance", "SEARCH_BACKGROUND", SEARCH_BACKGROUND));
        setForeground(configFile.getColor("Appearance", "SEARCH_TEXTCOLOR", SEARCH_TEXTCOLOR));
        setCaretColor(configFile.getColor("Appearance", "SEARCH_TEXTCOLOR", SEARCH_TEXTCOLOR));
        setFont(ConfigurationManager.getDefaultFont(14f));
    }
}
