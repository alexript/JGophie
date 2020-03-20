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
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author malyshev
 */
class FontLoader {

    protected static Font getFont(String fileName, float size) {
        Font result = null;

        try {

            result = Font.createFont(Font.TRUETYPE_FONT, FontLoader.class.getResourceAsStream(fileName)).deriveFont(size);
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(result);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(FontLoader.class.getName()).log(Level.SEVERE, "Unable to load font: ", ex);
        }

        return result;
    }
}
