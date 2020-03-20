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
package org.gophie2.config;

import javax.swing.ImageIcon;
import org.gophie2.Gophie;

public class ConfigurationManager {

    private static ConfigFile configFile;

    /**
     * Returns the main configuration file
     *
     * @return The main configuration file as ConfigFile
     */
    public static ConfigFile getConfigFile() {
        if (ConfigurationManager.configFile == null) {
            ConfigurationManager.configFile = new ConfigFile();
        }

        return ConfigurationManager.configFile;
    }

    public static ColorPalette getColors() {
        return new ColorPalette(getConfigFile());
    }

    /**
     * Returns the directory where all the downloads should be stored in which
     * usually resides in the user home
     *
     * @return Path to the download directory
     */
    public static String getDownloadPath() {
        return System.getProperty("user.home") + "/Downloads/";
    }
}
