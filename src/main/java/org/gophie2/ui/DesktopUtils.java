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
package org.gophie2.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author malyshev
 */
public class DesktopUtils {

    /**
     * Opens the file on the users desktop
     *
     * @param fileName
     */
    public static void openFileOnDesktop(String fileName) {
        try {
            /* use the desktop to open the file */
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(fileName));
            } else {
                /* no desktop support here, report one level up */
                throw new Exception("Desktop not supported");
            }
        } catch (Exception ex) {
            /* output the exception that the file could not be opened */
            System.out.println("Unable to open file after download " + "(" + fileName + "):" + ex.getMessage());
        }
    }

    public static void browse(String uri) {
        if (Desktop.isDesktopSupported() == true
                && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                /* launch the mailto handler of the system */
                Desktop.getDesktop().browse(new URI(uri));
            } catch (IOException | URISyntaxException ex) {
                /* Error: cannot open email client */
                System.out.println("Unable to open system's client: " + ex.getMessage());
            }
        }
    }

    /**
     * Tries to delete the file
     *
     * @param fileName
     */
    public static void deleteFile(String fileName) {
        try {
            File file = new File(fileName);
            file.delete();
        } catch (Exception ex) {
            /* just log the error and keep the crap, what else to do? */
            System.out.println("Failed to delete downloaded file (" + fileName + "): " + ex.getMessage());
        }
    }
}
