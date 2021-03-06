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
package org.gophie2.fonts;

import java.awt.Font;
import org.gophie2.config.ConfigurationManager;

/**
 *
 * @author malyshev
 */
public class ConsoleFont extends Font {

    private static final long serialVersionUID = 5842145325493381125L;

    public ConsoleFont(float size) {
        super(FontLoader.getFont(ConfigurationManager.getConfigFile().get("Fonts", "CONSOLE_FONT", "Inconsolata-Regular.ttf"), size));
    }
}
