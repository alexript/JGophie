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

import java.awt.Color;

/**
 *
 * @author malyshev
 */
public final class ColorPalette {

    private final static String SECTION = "Appearance";
    private final ConfigFile conf;

    public static class PColor extends Color {

        private static final long serialVersionUID = 4387960131996869334L;

        private PColor(Color c) {
            super(c.getRGB());
        }

        @Override
        public String toString() {
            int r = getRed();
            int g = getGreen();
            int b = getBlue();
            return String.format("#%02x%02x%02x", r, g, b);
        }

    }

    protected ColorPalette(ConfigFile conf) {
        this.conf = conf;
    }

    private PColor get(String name, String defaultValue) {
        return new PColor(conf.getColor(SECTION, name, defaultValue));
    }

    public PColor getSearchBackground() {
        return get("SEARCH_BACKGROUND", "#248AC2");
    }

    public PColor getSearchText() {
        return get("SEARCH_TEXTCOLOR", "#e8e8e8");
    }

    public PColor getSearchTitle() {
        return get("SEARCH_TITLECOLOR", "#76bce3");
    }

    public PColor getDownloadSelected() {
        return get("DOWNLOAD_SELECTED_COLOR", "#cf9a0c");
    }

    public PColor getDownloadTitle() {
        return get("DOWNLOAD_TITLE_COLOR", "#ffffff");
    }

    public PColor getDownloadText() {
        return get("DOWNLOAD_TEXT_COLOR", "#e0e0e0");
    }

    public PColor getFilelistBackground() {
        return get("FILELIST_BACKGROUND", "#1b1b1b");
    }

    public PColor getActionbarText() {
        return get("ACTIONBAR_TEXTCOLOR", "#ffffff");
    }

    public PColor getActionbarInactive() {
        return get("ACTIONBAR_INACTIVE_TEXTCOLOR", "#76bce3");
    }

    public PColor getActionbarBackground() {
        return get("ACTIONBAR_BACKGROUND", "#248AC2");
    }

    public PColor getNavigationbarBackground() {
        return get("NAVIGATIONBAR_BACKGROUND", "#248AC2");
    }

    public PColor getNavigationbarText() {
        return get("NAVIGATIONBAR_TEXTCOLOR", "#76bce3");
    }

    public PColor getNavigationbarTextHover() {
        return get("NAVIGATIONBAR_TEXTHOVERCOLOR", "#ffffff");
    }

    public PColor getNavigationbarSelection() {
        return get("NAVIGATIONBAR_SELECTION_COLOR", "#ffffff");
    }

    public PColor getMessageviewBackground() {
        return get("MESSAGEVIEW_BACKGROUND_COLOR", "#fcba03");
    }

    public PColor getMessageviewText() {
        return get("MESSAGEVIEW_FOREGROUND_COLOR", "#000000");
    }

    public PColor getPageSelection() {
        return get("PAGE_SELECTION_COLOR", "#cf9a0c");
    }

    public PColor getPageLinkText() {
        return get("PAGE_LINK_COLOR", "#22c75c");
    }

    public PColor getPageLinenumber() {
        return get("PAGE_LINENUMBER_COLOR", "#454545");
    }

    public PColor getPageText() {
        return get("VIEW_TEXTCOLOR", "#e8e8e8");
    }

    public PColor getPageBackground() {
        return get("VIEW_BACKGROUND", "#1b1b1b");
    }
}
