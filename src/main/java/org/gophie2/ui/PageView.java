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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.gophie2.config.ColorPalette;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.fonts.ConsoleFont;

import org.gophie2.net.GopherItem;
import org.gophie2.net.GopherPage;
import org.gophie2.net.GopherItemType;
import org.gophie2.ui.event.NavigationInputListener;
import org.gophie2.ui.event.PageMenuEventListener;

/**
 * The PageView component renders GopherPage objects
 */
public class PageView extends JScrollPane {

    private static final long serialVersionUID = -1691878866552844125L;

    private PageMenu pageMenu;
    private JEditorPane viewPane;
    private final JEditorPane headerPane;
    private final HTMLEditorKit editorKit;
    private StyleSheet styleSheet;
    private final Font textFont;

    private List<NavigationInputListener> inputListenerList;

    private GopherPage currentPage = null;

    public void addListener(NavigationInputListener listener) {
        inputListenerList.add(listener);
    }

    public void showGopherContent(GopherPage content) {
        /* reset the header to just show nothing */
        headerPane.setText("");

        /* set current page to the page menu */
        pageMenu.setCurrentPage(content);
        GopherItemType contentType = content.getContentType();

        /* check the type of content supplied */
        if (contentType.isImage()) {
            /* try to display as an image */
            try {
                /* try to identify the file extension */
                String imageFileExt = "." + contentType.getFileExt();

                /* try to determine the filetype from the url */
                String imageUrl = content.getUrl().getUrlString();
                if (imageUrl.substring(imageUrl.length() - 4).equals(".")) {
                    imageFileExt = imageUrl.substring(imageUrl.length() - 3);
                }
                if (imageUrl.substring(imageUrl.length() - 5).equals(".")) {
                    imageFileExt = imageUrl.substring(imageUrl.length() - 4);
                }

                /* write the image content to file */
                File tempImageFile = File.createTempFile("gopherimagefile", imageFileExt);
                try (FileOutputStream outputStream = new FileOutputStream(tempImageFile)) {
                    outputStream.write(content.getByteArray());
                }

                /* determine image size and rescale */
                String imageHtmlCode = "<img src=\"" + tempImageFile.toURI()
                        .toURL().toExternalForm() + "\" />";

                try {
                    BufferedImage bufferedImage = ImageIO.read(tempImageFile);
                    int width = bufferedImage.getWidth();
                    int height = bufferedImage.getHeight();
                    if (width > 800) {
                        height = (height / (width / 800));
                        imageHtmlCode = "<img src=\"" + tempImageFile.toURI()
                                .toURL().toExternalForm() + "\" "
                                + "width=\"800\" height=" + height + "\""
                                + "/>";
                    }
                } catch (IOException ex) {
                    /* failed to determine image size */
                    System.out.println("Failed to determine image size: " + ex.getMessage());
                }

                /* display content as an image */
                viewPane.setContentType("text/html");
                viewPane.setText(imageHtmlCode);
            } catch (IOException ex) {
                /* display exception cause as text inside the view */
                viewPane.setContentType("text/plain");
                viewPane.setText("Failed to display the image:\n" + ex.getMessage());
            }
        } else {
            /* display content as plain text */
            viewPane.setContentType("text/plain");
            viewPane.setText(content.getSourceCode().replace("\n.\r\n", ""));
        }
    }

    public void showGopherPage(GopherPage page) {
        /* set the current local gopher page */
        currentPage = page;

        /* set current page to the page menu */
        pageMenu.setCurrentPage(page);

        /* create the headers */
        String renderedHeader = "<table cellspacing=\"0\" cellpadding=\"2\">";
        String renderedContent = "<table cellspacing=\"0\" cellpadding=\"2\">";

        int lineNumber = 1;
        for (GopherItem item : page.getItemList()) {
            /* set the content for the row header */
            renderedHeader += "<tr><td class=\"lineNumber\">" + lineNumber + "</td>"
                    + "<td><div class=\"itemIcon\">"
                    + this.getGopherItemTypeIcon(item.getItemType())
                    + "</div></td></tr>";

            /* set the content for the text view */
            String itemTitle = item.getUserDisplayString().replace(" ", "&nbsp;");
            String itemCode = "<span class=\"text\">" + itemTitle + "</span>";

            /* build links for anything other than infromation items */
            if (item.getItemType() != GopherItemType.INFORMATION) {
                /* create the link for this item */
                itemCode = "<a href=\"" + item.getUrlString() + "\">" + itemTitle + "</a>";
            }

            /* create the item table row */
            renderedContent += "<tr><td class=\"item\">" + itemCode + "</td></tr>";

            lineNumber++;
        }

        /* set content type and add content to view */
        viewPane.setContentType("text/html");
        viewPane.setText(renderedContent + "</table>");

        /* set content type and add content to header */
        headerPane.setContentType("text/html");
        headerPane.setText(renderedHeader + "</table>");

        /* scroll the view pane to the top */
        viewPane.setCaretPosition(0);
    }

    private void configureStyle() {
        ColorPalette colors = ConfigurationManager.getColors();

        /* build up the stylesheet for the rendering */
        styleSheet = editorKit.getStyleSheet();
        styleSheet.addRule("body { white-space:nowrap; }");
        styleSheet.addRule(".text { cursor:text; }");
        styleSheet.addRule(".lineNumber { color: " + colors.getPageLinenumber() + "; }");
        styleSheet.addRule(".itemIcon { font-family:Feather; font-size:10px; margin-left:5px; }");
        styleSheet.addRule("a { text-decoration: none; color: " + colors.getPageLinkText() + "; }");
        styleSheet.addRule(".item { color: " + colors.getPageText() + "; }");
    }

    public PageView(MainWindow parent) {

        ColorPalette colors = ConfigurationManager.getColors();

        Color textColor = colors.getPageText();
        Color backgroundColor = colors.getPageBackground();

        /* instanciate input listener list */
        inputListenerList = new ArrayList<>();

        /* create the editor kit instance */
        editorKit = new HTMLEditorKit();

        /* create the editor pane */
        viewPane = new JEditorPane();
        viewPane.setEditable(false);
        viewPane.setBackground(backgroundColor);
        viewPane.setForeground(textColor);
        viewPane.setBorder(new EmptyBorder(10, 4, 8, 16));
        viewPane.setEditorKit(editorKit);
        viewPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        viewPane.setSelectionColor(colors.getPageSelection());

        viewPane.setDragEnabled(false);
        getViewport().add(viewPane);

        /* adjust the scrollbars */
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        /* create the header pane with line numbers and icons */
        headerPane = new JEditorPane();
        headerPane.setEditable(false);
        headerPane.setBackground(backgroundColor);
        headerPane.setForeground(textColor);
        headerPane.setBorder(new EmptyBorder(10, 12, 8, 2));
        headerPane.setEditorKit(editorKit);
        headerPane.setDragEnabled(false);
        setRowHeaderView(headerPane);

        /* configure the style of the header and the view */
        configureStyle();

        /* create the page menu and attach the popup trigger */
        pageMenu = new PageMenu();
        pageMenu.addPageMenuEventListener((PageMenuEventListener) parent);
        pageMenu.addPageMenuEventListener(parent.getDownloadRequester());
        viewPane.add(this.pageMenu);
        viewPane.addMouseListener(new MouseAdapter() {
            /* handle the popup trigger for this document */
            public void mouseReleased(MouseEvent evt) {
                /* get the trigger button for the menu from config
                    (right mouse button id is usually #3) */
                int menuTriggerButtonId = ConfigurationManager.getConfigFile().getInt("Navigation", "MENU_MOUSE_TRIGGERBUTTON", 3);

                if (evt.getButton() == menuTriggerButtonId) {
                    /* trigger hit, show the page menu and also
                        make sure to pass the text selection before */
                    pageMenu.setSelectedText(viewPane.getSelectedText());

                    /* show the menu */
                    pageMenu.show(viewPane,
                            (int) evt.getPoint().getX(),
                            (int) evt.getPoint().getY());
                }
            }
        });

        /* report any links hits as address request to the listeners */
        viewPane.addHyperlinkListener((HyperlinkEvent e) -> {
            /* get the url of that link */
            String urlValue = e.getDescription();

            /* determine the content type of the link target */
            GopherItem itemObject = null;
            if (currentPage != null) {
                /* determine the content type of the gopher item
                by the definition of it in the gopher menu */
                for (GopherItem contentItem : currentPage.getItemList()) {
                    if (contentItem.getUrlString().equals(urlValue)) {
                        itemObject = contentItem;
                    }
                }
            }

            /* pass the active link item to the popup menu */
            if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                pageMenu.setLinkTarget(itemObject);
            }

            /* reset the link target for the popup menu */
            if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
                pageMenu.setLinkTarget(null);
            }

            /* handle link activation (aka left-click) */
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                /* execute the handler */
                for (NavigationInputListener inputListener : inputListenerList) {
                    inputListener.addressRequested(urlValue, itemObject);
                }
            }
        });

        /* try to open the font for icon display */
        textFont = new ConsoleFont(ConfigurationManager.getConfigFile().getFloat("Fonts", "PAGE_SIZE", 17f));

        /* apply the font settings to the view pane */
        viewPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        viewPane.setFont(textFont);

        /* apply the font settings to the header pane */
        headerPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        headerPane.setFont(textFont);
        headerPane.setHighlighter(null);
    }

    public void selectAllText() {
        /* just pass it onto the view */
        viewPane.selectAll();
        viewPane.requestFocus();
    }

    public String getGopherItemTypeIcon(GopherItemType type) {
        String result = "";

        switch (type) {
            case TEXTFILE:
                result = "";
                break;
            case GOPHERMENU:
                result = "";
                break;
            case CCSCO_NAMESERVER:
                result = "";
                break;
            case ERRORCODE:
                result = "";
                break;
            case BINHEX_FILE:
                result = "";
                break;
            case DOS_FILE:
                result = "";
                break;
            case UUENCODED_FILE:
                result = "";
                break;
            case FULLTEXT_SEARCH:
                result = "";
                break;
            case TELNET:
                result = "";
                break;
            case BINARY_FILE:
                result = "";
                break;
            case MIRROR:
                result = "";
                break;
            case GIF_FILE:
                result = "";
                break;
            case IMAGE_FILE:
                result = "";
                break;
            case TELNET3270:
                result = "";
                break;
            case HTML_FILE:
                result = "";
                break;
            case INFORMATION:
                result = "";
                break;
            case SOUND_FILE:
                result = "";
                break;
            case UNKNOWN:
                result = "";
                break;
        }

        return result;
    }
}
