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
import org.gophie2.config.ConfigurationManager;

/**
 *
 * @author malyshev
 */
public class SearchTitle extends JLabel {

    private static final long serialVersionUID = 4545277254985765389L;

    public SearchTitle() {
        super("Search");
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(2, 0, 0, 12));
        setForeground(ConfigurationManager.getColors().getSearchTitle());
    }
}
