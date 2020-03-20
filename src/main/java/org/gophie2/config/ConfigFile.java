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
package org.gophie2.config;

import com.nikhaldimann.inieditor.IniEditor;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gophie2.Gophie;

/**
 *
 * @author malyshev
 */
public class ConfigFile extends IniEditor {

    private static final String CONFIG_FOLDERNAME = ".gophie2";
    private static final String CONFIG_FILENAME = "config.ini";

    protected ConfigFile() {
        super(true);
        init();
    }

    /**
     * Returns the configuration directory's path
     *
     * @return The full path to the configuration directory
     */
    private static String getConfigPath() {
        /* define the full path of the configuration directory */
        String result = System.getProperty("user.home") + "/" + CONFIG_FOLDERNAME + "/";
        File folder = new File(result);
        folder.mkdirs();
        return result;
    }

    private void init() {
        String configPath = getConfigPath();
        File file = new File(configPath, CONFIG_FILENAME);

        if (file.exists()) {
            try {
                load(file);
                return;
            } catch (IOException ex) {
                Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            load(Gophie.class.getResourceAsStream(CONFIG_FILENAME));
            save();
        } catch (IOException ex) {
            Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String get(String section, String option) {
        String val = super.get(section, option);
        if (val == null) {
            return null;
        }
        String[] split = val.split(";", 2);
        return split[0];
    }

    @Override
    public void set(String section, String option, String value) {
        if (!hasSection(section)) {
            addSection(section);
        }
        super.set(section, option, value);
    }

    public void save() {
        String configPath = getConfigPath();
        File file = new File(configPath, CONFIG_FILENAME);
        try {
            save(file);
        } catch (IOException ex) {
            Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String get(String section, String option, String defaultValue) {
        String v = get(section, option);
        if (v == null) {
            return defaultValue;
        }
        return v;
    }

    public int getInt(String section, String option) {
        return getInt(section, option, 0);
    }

    public int getInt(String section, String option, int defaultValue) {
        String v = get(section, option);
        if (v == null) {
            return defaultValue;
        }
        return Integer.parseInt(v.trim());
    }

    public boolean getBool(String section, String option) {
        return getBool(section, option, false);
    }

    public boolean getBool(String section, String option, boolean defaultValue) {
        String v = get(section, option);
        if (v == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(v.trim());
    }

    public Color getColor(String section, String option) {
        return getColor(section, option, "#000000");
    }

    public Color getColor(String section, String option, String defaultValue) {
        String v = get(section, option);
        if (v == null) {
            v = defaultValue;
        }
        return Color.decode(v);
    }

}
