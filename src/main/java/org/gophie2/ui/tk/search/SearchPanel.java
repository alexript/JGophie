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
package org.gophie2.ui.tk.search;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.border.EmptyBorder;
import org.gophie2.config.ConfigFile;
import org.gophie2.config.ConfigurationManager;

import org.gophie2.ui.event.SearchInputListener;

public class SearchPanel extends JPanel {

    /* constants */
    public static final String SEARCH_BACKGROUND = "#248AC2";
    public static final String SEARCH_TITLECOLOR = "#76bce3";
    private static final long serialVersionUID = 5104881772647618206L;

    JLabel searchTitle;
    JTextField searchText;

    public SearchPanel() {
        init();
    }

    private void init() throws NumberFormatException {
        ConfigFile configFile = ConfigurationManager.getConfigFile();

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new EmptyBorder(6, 12, 12, 14));
        setBackground(configFile.getColor("Appearance", "SEARCH_BACKGROUND", SEARCH_BACKGROUND));

        add(new SearchIcon());

        searchTitle = new SearchTitle();
        add(searchTitle);

        searchText = new SearchTextField();
        add(searchText);

        setVisible(false);
    }

    public void performSearch(String title, SearchInputListener listener) {
        searchTitle.setText(title);
        searchTitle.setFont(ConfigurationManager.getDefaultFont(14f));
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                /* execute search when the ENTER key is pressed */
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JTextField textField = (JTextField) e.getSource();

                    /* only execute search when text is not empty */
                    if (textField.getText().length() > 0) {
                        listener.searchRequested(textField.getText());
                    }

                    setVisible(false);
                }

                /* just cancel when the user hit ESC */
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setVisible(false);
                }
            }
        });
        setVisible(true);
        searchText.grabFocus();
    }
}
