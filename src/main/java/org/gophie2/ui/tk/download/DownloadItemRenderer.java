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
package org.gophie2.ui.tk.download;

import java.awt.Component;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import javax.swing.border.EmptyBorder;

import org.gophie2.config.ColorPalette;
import org.gophie2.config.ConfigFile;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.fonts.ConsoleFont;
import org.gophie2.view.DataSizeView;
import org.gophie2.net.DownloadItem;

import org.gophie2.net.GopherMenuItem;

public class DownloadItemRenderer extends JPanel implements ListCellRenderer<DownloadItem> {

    private static final long serialVersionUID = 8646877382931988294L;

    private final JLabel titleLabel = new JLabel();
    private final JLabel textLabel = new JLabel();

    @Override
    public Component getListCellRendererComponent(JList<? extends DownloadItem> list,
            DownloadItem value, int index,
            boolean isSelected, boolean cellHasFocus) {

        ConfigFile configFile = ConfigurationManager.getConfigFile();
        ColorPalette colors = ConfigurationManager.getColors();

        /* render the cell for this download item */
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(5, 10, 5, 10));

        /* highlight if this element is selected */
        if (isSelected == true) {
            setOpaque(true);
            setBackground(colors.getDownloadSelected());
        }

        /* get the gopher item of this download */
        GopherMenuItem item = value.getGopherItem();

        /* show the file name in the title */
        titleLabel.setText(item.getFileName());
        Font titleFont = new ConsoleFont(configFile.getFloat("Fonts", "DOWNLOADITEM_TITLE_SIZE", 15f));
        titleLabel.setFont(titleFont.deriveFont(titleFont.getStyle() | Font.BOLD));
        titleLabel.setForeground(colors.getDownloadTitle());

        /* create the information text based on the status */
        String statusText;
        String byteLoadedText = DataSizeView.get(value.getByteCountLoaded());

        switch (value.getStatus()) {
            case COMPLETED:
                statusText = String.format("Completed (%s)", byteLoadedText);
                break;
            case ACTIVE:
                String transferRate = DataSizeView.get(value.getBytePerSecond());
                statusText = String.format("%s (%s/sec)", byteLoadedText, transferRate);
                break;
            case FAILED:
                statusText = String.format("Failed (%s)", byteLoadedText);
                break;
            default:
                statusText = "Download not started";
        }


        /* set the text to the status text label */
        textLabel.setText(String.format("%s — %s", statusText, item.getHostName()));
        textLabel.setBorder(new EmptyBorder(4, 0, 0, 0));
        textLabel.setForeground(colors.getDownloadText());
        Font textFont = new ConsoleFont(configFile.getFloat("Fonts", "DOWNLOADITEM_STATUS_SIZE", 13f));
        textLabel.setFont(textFont);

        add(titleLabel);
        add(textLabel);

        return this;
    }
}
