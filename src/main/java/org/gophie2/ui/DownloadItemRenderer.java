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
package org.gophie2.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import org.gophie2.config.ConfigFile;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.view.DataSizeView;
import org.gophie2.net.DownloadItem;

import org.gophie2.net.DownloadItem.DownloadStatus;
import org.gophie2.net.GopherItem;

public class DownloadItemRenderer extends JPanel implements ListCellRenderer<DownloadItem> {

    private static final long serialVersionUID = 1L;

    private final JLabel titleLabel = new JLabel();
    private final JLabel textLabel = new JLabel();

    @Override
    public Component getListCellRendererComponent(JList<? extends DownloadItem> list,
            DownloadItem value, int index,
            boolean isSelected, boolean cellHasFocus) {
        /* get the config file for the color schemes */
        ConfigFile configFile = ConfigurationManager.getConfigFile();

        /* render the cell for this download item */
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(5, 10, 5, 10));

        /* highlight if this element is selected */
        if (isSelected == true) {
            this.setOpaque(true);
            this.setBackground(configFile.getColor("Appearance", "DOWNLOAD_SELECTED_COLOR", "#cf9a0c"));
        }

        /* get the gopher item of this download */
        GopherItem item = value.getGopherItem();

        /* show the file name in the title */
        this.titleLabel.setText(item.getFileName());
        Font titleFont = ConfigurationManager.getConsoleFont(15f);
        this.titleLabel.setFont(titleFont.deriveFont(titleFont.getStyle() | Font.BOLD));
        this.titleLabel.setForeground(configFile.getColor("Appearance", "DOWNLOAD_TITLE_COLOR", "#ffffff"));

        /* create the information text based on the status */
        String statusText = "Download not started";
        String byteLoadedText = DataSizeView.get(value.getByteCountLoaded());

        /* show message for completed downloads */
        if (value.getStatus() == DownloadStatus.COMPLETED) {
            statusText = "Completed (" + byteLoadedText + ")";
        }

        /* show message for active downloads */
        if (value.getStatus() == DownloadStatus.ACTIVE) {
            String transferRate = DataSizeView.get(value.getBytePerSecond());
            statusText = byteLoadedText + " (" + transferRate + "/sec)";
        }

        /* show message for failed downloads */
        if (value.getStatus() == DownloadStatus.FAILED) {
            statusText = "Failed (" + byteLoadedText + ")";
        }

        /* append the host name to the info text */
        statusText += " — " + item.getHostName();

        /* set the text to the status text label */
        this.textLabel.setText(statusText);
        this.textLabel.setBorder(new EmptyBorder(4, 0, 0, 0));
        this.textLabel.setForeground(configFile.getColor("Appearance", "DOWNLOAD_TEXT_COLOR", "#e0e0e0"));
        Font textFont = ConfigurationManager.getConsoleFont(13f);
        this.textLabel.setFont(textFont);

        this.add(this.titleLabel);
        this.add(this.textLabel);

        return this;
    }
}
