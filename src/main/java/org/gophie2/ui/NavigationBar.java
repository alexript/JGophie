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
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.*;
import java.util.*;
import org.gophie2.config.ColorPalette;
import org.gophie2.config.ConfigFile;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.fonts.DefaultFont;
import org.gophie2.fonts.IconFont;
import org.gophie2.images.ImageLoader;

import org.gophie2.net.GopherItem;
import org.gophie2.ui.event.NavigationInputListener;

public class NavigationBar extends JPanel {

    /* constants */
    private static final long serialVersionUID = 1L;

    /* static variables */
    private final Color textColor;
    private final Color textHoverColor;


    /* local variables and objects */
    private final Font iconFont;
    private JLabel backButton;
    private JLabel forwardButton;
    private JLabel refreshButton;
    private JLabel homeButton;
    private final JTextField addressInput;
    private JLabel downloadButton;
    private final JLabel statusIcon;
    private Boolean isLoadingStatus = false;
    private Boolean allowNavigateForward = false;
    private Boolean allowNavigateBack = false;

    /* listeners for local events */
    private ArrayList<NavigationInputListener> inputListenerList;

    /* the constructor essentially builds up the entire
        component with all its sub-components

        @backgroundColor    Background color of this bar
        @textColor          Color of the text and icons
        @textHoverColor     Color of the text and icons on hover
     */
    public NavigationBar(Color backgroundColor, Color textColor, Color textHoverColor) {
        inputListenerList = new ArrayList<>();

        ConfigFile configFile = ConfigurationManager.getConfigFile();

        /* get the icon font from the configuration */
        iconFont = new IconFont(configFile.getFloat("Fonts", "NAVIGATIONBAR_ICONS_SIZE", 19f));

        /* store the text color locally */
        this.textHoverColor = textHoverColor;
        this.textColor = textColor;

        /* set the background color initially */
        setBackgroundColor(backgroundColor);

        /* set the layout for this navigation bar */
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new EmptyBorder(4, 4, 4, 4));

        /* create the back navigation button */
        backButton = this.createButton("");
        backButton.addMouseListener(new MouseAdapter() {
            /* notify the listeners of the move back request */
            @Override
            public void mouseReleased(MouseEvent evt) {
                if (allowNavigateBack == true) {
                    inputListenerList.forEach((inputListener) -> {
                        inputListener.backwardRequested();
                    });
                }
            }

            /* set the color to the hover color and use the hand cursor */
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (allowNavigateBack == true) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    backButton.setForeground(textHoverColor);
                }
            }

            /* revert back to the default cursor and default color */
            @Override
            public void mouseExited(MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                backButton.setForeground(textColor);
            }
        });

        /* create the forward navigation button */
        forwardButton = this.createButton("");
        forwardButton.addMouseListener(new MouseAdapter() {
            /* notify the listeners of the forward move request */
            @Override
            public void mouseReleased(MouseEvent evt) {
                if (allowNavigateForward == true) {
                    inputListenerList.forEach((inputListener) -> {
                        inputListener.forwardRequested();
                    });
                }
            }

            /* set the color to the hover color and use the hand cursor */
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (allowNavigateForward == true) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    forwardButton.setForeground(textHoverColor);
                }
            }

            /* revert back to the default cursor and default color */
            @Override
            public void mouseExited(MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                forwardButton.setForeground(textColor);
            }
        });

        /* create the refresh button and handle it */
        refreshButton = this.createButton("");
        refreshButton.addMouseListener(new MouseAdapter() {
            /* initiate a refresh or cancel process */
            @Override
            public void mouseReleased(MouseEvent evt) {
                /* request a refresh when currently in network idle */
                if (isLoadingStatus == false) {
                    inputListenerList.forEach((inputListener) -> {
                        inputListener.refreshRequested();
                    });
                }

                /* request a stop or cancellation when currently loading */
                if (isLoadingStatus == true) {
                    inputListenerList.forEach((inputListener) -> {
                        inputListener.stopRequested();
                    });
                }
            }

            /* set the color to the hover color and use the hand cursor */
            @Override
            public void mouseEntered(MouseEvent evt) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                refreshButton.setForeground(textHoverColor);
            }

            /* revert back to the default cursor and default color */
            @Override
            public void mouseExited(MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                refreshButton.setForeground(textColor);
            }
        });

        /* create the refresh button and handle it */
        homeButton = this.createButton("");
        homeButton.addMouseListener(new MouseAdapter() {
            /* request navigation to the home page */
            @Override
            public void mouseReleased(MouseEvent evt) {
                /* request to g to home gopher page */
                inputListenerList.forEach((inputListener) -> {
                    inputListener.homeGopherRequested();
                });
            }

            /* set the color to the hover color and use the hand cursor */
            @Override
            public void mouseEntered(MouseEvent evt) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                homeButton.setForeground(textHoverColor);
            }

            /* revert back to the default cursor and default color */
            @Override
            public void mouseExited(MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                homeButton.setForeground(textColor);
            }
        });

        /* create the address input */
        ColorPalette colors = ConfigurationManager.getColors();
        addressInput = this.createAddressInput();
        addressInput.setSelectionColor(colors.getNavigationbarSelection());
        addressInput.setCaretColor(textColor);

        /* create the download button and handle it */
        downloadButton = this.createButton("");
        downloadButton.addMouseListener(new MouseAdapter() {
            /* notify the listeners of the forward move request */
            @Override
            public void mouseReleased(MouseEvent evt) {
                inputListenerList.forEach((inputListener) -> {
                    inputListener.showDownloadRequested();
                });
            }

            /* set the color to the hover color and use the hand cursor */
            @Override
            public void mouseEntered(MouseEvent evt) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                downloadButton.setForeground(textHoverColor);
            }

            /* revert back to the default cursor and default color */
            @Override
            public void mouseExited(MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                downloadButton.setForeground(textColor);
            }
        });

        /* create the status indicator */
        ImageIcon statusIconImage = ImageLoader.load("loading.gif");
        statusIcon = new JLabel(statusIconImage);
        statusIcon.setBorder(new EmptyBorder(0, 8, 0, 2));
        statusIcon.setOpaque(false);
        statusIcon.setVisible(false);
        add(this.statusIcon);
    }

    /**
     * Defines whether forward navigation is enabled
     *
     * @param value true enables navigation, false disables
     */
    public void setNavigateForward(Boolean value) {
        allowNavigateForward = value;
    }

    /**
     * Defines whether navigation back is enabled
     *
     * @param value true enables navigation, false disables
     */
    public void setNavigateBack(Boolean value) {
        allowNavigateBack = value;
    }

    /**
     * Defines whether is loading content or not
     *
     * @param status true when currently loading content, false if not
     */
    public void setIsLoading(Boolean status) {
        isLoadingStatus = status;

        if (status == true) {
            statusIcon.setVisible(true);
            refreshButton.setText("");
        } else {
            statusIcon.setVisible(false);
            refreshButton.setText("");
        }
    }

    /*
        Adds a listener for navigation events
     */
    public void addListener(NavigationInputListener listener) {
        inputListenerList.add(listener);
    }

    /*
        Sets the text for the address input

        @addressText    text to insert into address input
     */
    public void setAddressText(String addressText) {
        addressInput.setText(addressText);
    }

    /*
        Creates the address input to insert gopher URLs in

        @return     the JTextField which is the address input
     */
    private JTextField createAddressInput() {
        JTextField inputField = new JTextField();
        inputField.setBorder(new EmptyBorder(4, 10, 6, 4));
        inputField.setFont(new DefaultFont(ConfigurationManager.getConfigFile().getFloat("Fonts", "NAVIGATION_TEXT_SIZE", 14f)));
        inputField.setForeground(textColor);
        inputField.setOpaque(false);
        add(inputField);

        inputField.addActionListener((ActionEvent e) -> {
            String requestedAddress = inputField.getText().trim();
            if (requestedAddress.length() > 0) {
                inputListenerList.forEach((inputListener) -> {
                    inputListener.addressRequested(requestedAddress, new GopherItem());
                });
            }
        });

        return inputField;
    }

    /*
        Creates a new button on this navigation bar

        @text       text on the new button
        @return     the JButton object created
     */
    private JLabel createButton(String text) {
        JLabel button = new JLabel(text);

        /* set the icon font */
        button.setFont(iconFont);
        button.setForeground(textColor);

        /* set the border properly to give some space */
        button.setBorder(new EmptyBorder(0, 8, 0, 8));

        /* add the button the bar */
        add(button);

        return button;
    }

    /*
        sets the background color has hex (e.g. #ffffff)

        @colorHex       the hex code for the background color
     */
    public void setBackgroundColor(Color colorHex) {
        setBackground(colorHex);
    }
}
