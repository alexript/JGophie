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

import org.gophie2.ui.tk.download.DownloadWindow;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Toolkit;
import org.gophie2.ui.tk.search.SearchPanel;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.gophie2.config.ColorPalette;

import org.gophie2.config.ConfigFile;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.view.DataSizeView;
import org.gophie2.net.GopherClient;
import org.gophie2.net.GopherItem;
import org.gophie2.net.GopherItemType;
import org.gophie2.net.GopherPage;
import org.gophie2.net.GopherUrl;
import org.gophie2.net.event.GopherClientEventListener;
import org.gophie2.net.event.GopherError;
import org.gophie2.ui.event.NavigationInputListener;
import org.gophie2.ui.event.PageMenuEventAdapter;
import org.gophie2.ui.event.PageMenuEventListener;
import org.gophie2.ui.tk.requesters.ConfirmDownload;
import org.gophie2.ui.tk.requesters.Requester;

public class MainWindow extends PageMenuEventAdapter implements NavigationInputListener, GopherClientEventListener, PageMenuEventListener {

    public static final String APPLICATION_TITLE = "Gophie2";

    public static final String DEFAULT_GOPHERHOME = "gopher.floodgap.com";

    private final GopherClient gopherClient;


    private List<GopherPage> history = new ArrayList<>();
    private int historyPosition = -1;

    private final JFrame frame;
    private final PageView pageView;
    private final NavigationBar navigationBar;
    private final JPanel headerBar;
    private final MessageView messageView;
    private final SearchPanel searchInput;

    private final ConfirmDownload requesterConfirmDownload;

    public JFrame getMainWindowFrame() {
        return frame;
    }

    public ConfirmDownload getDownloadRequester() {
        return requesterConfirmDownload;
    }

    public MainWindow() {
        /* get the config file */
        ConfigFile configFile = ConfigurationManager.getConfigFile();
        ColorPalette colors = ConfigurationManager.getColors();

        requesterConfirmDownload = new ConfirmDownload(this);

        /* create the instance of the client */
        gopherClient = new GopherClient();

        /* create the main window */
        frame = new JFrame(APPLICATION_TITLE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* create the page view component object */
        pageView = new PageView(this);
        pageView.addListener(this);

        /* create the navigation bar */
        navigationBar = new NavigationBar(
                colors.getNavigationbarBackground(),
                colors.getNavigationbarText(),
                colors.getNavigationbarTextHover()
        );

        /* set the gopher home as defined in the config
            or use the default one if none is defined */
        String gopherHome = configFile.get("Navigation", "GOPHERHOME", DEFAULT_GOPHERHOME);
        navigationBar.setAddressText(gopherHome);

        /* attach listener to navigation bar */
        navigationBar.addListener(this);

        /* create the header bar, message view
            and search input component */
        headerBar = new JPanel();
        headerBar.setLayout(new BoxLayout(headerBar, BoxLayout.Y_AXIS));
        messageView = new MessageView();
        headerBar.add(messageView);
        searchInput = new SearchPanel();
        headerBar.add(searchInput);

        /* set the content pane */
        Container contentPane = frame.getContentPane();
        contentPane.add(headerBar, BorderLayout.NORTH);
        contentPane.add(pageView, BorderLayout.CENTER);
        contentPane.add(navigationBar, BorderLayout.SOUTH);

        /* start the window in the center of the screen */
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
                dim.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);

        /* fetch the default gopher home */
        fetchGopherContent(gopherHome, GopherItemType.GOPHERMENU);
    }

    public void show() {
        /* display the window */
        frame.pack();
        frame.setVisible(true);
    }

    private void updateHistory(GopherPage page) {
        Boolean addToHistory = false;

        /* check if current position is at last page */
        if (historyPosition == history.size() - 1) {
            /* add this page to the history */
            if (history.size() > 0) {
                /* make sure this was not just a reload and the last
                    page in the history is not already ours */
                if (!history.get(history.size() - 1).getUrl().getUrlString()
                        .equals(page.getUrl().getUrlString())) {
                    /* just drop it in */
                    addToHistory = true;
                }
            } else {
                /* empty history, just drop in the page */
                addToHistory = true;
            }
        } else {
            /* user navigation inside history, check if the current
                page is at the position in history or if it is a
                new page the user went to */
            if (!history.get(historyPosition).getUrl()
                    .getUrlString().equals(page.getUrl().getUrlString())) {
                /* it is a new page outside the history, keep the history
                    up until the current page and add this page as a new
                    branch to the history, eliminating the
                    previous branch forward */
                ArrayList<GopherPage> updatedHistory = new ArrayList<>();
                for (int h = 0; h <= historyPosition; h++) {
                    updatedHistory.add(history.get(h));
                }

                /* update the history */
                history = updatedHistory;

                /* allow adding to history */
                addToHistory = true;
            }
        }

        /* reset navigation allowance */
        navigationBar.setNavigateBack(false);
        navigationBar.setNavigateForward(false);

        /* add to history, if allowed */
        if (addToHistory == true) {
            /* add to the stack of pages */
            history.add(page);

            /* update position to the top */
            historyPosition = history.size() - 1;

            /* disable forward */
            navigationBar.setNavigateForward(false);
            if (history.size() > 1) {
                /* allow back if more than just this page exist */
                navigationBar.setNavigateBack(true);
            }
        } else {
            /* if position is 0, there is nowhere to go back to */
            if (historyPosition > 0) {
                /* allow navigation back in history */
                navigationBar.setNavigateBack(true);
            }
            if (historyPosition < (history.size() - 1)) {
                /* if position is at the end, there is nowhere
                    to move forward to */
                navigationBar.setNavigateForward(true);
            }
        }
    }

    @Override
    public void addressRequested(String addressText, GopherItem item) {
        Requester requester;
        /* check if this file is binary or not as
            binaries such as media or other files
            will be handled differently (e.g. downloaded) */
        if (item.getItemType().isBinary()) {
            /* binary files are handled by the download manager */
            requester = requesterConfirmDownload;
            requester.request(messageView, addressText, item);
        } else {
            /* this is not a binary file, try to handle and render */
            switch (item.getItemType()) {
                case FULLTEXT_SEARCH:
                    /* show the search interface */
                    searchInput.performSearch(item.getUserDisplayString(), (String text) -> {
                        /* execute search through gopher */
                        String searchQueryText = addressText + "\t" + text;
                        fetchGopherContent(searchQueryText, GopherItemType.GOPHERMENU);
                    });
                    break;
                case CCSCO_NAMESERVER:
                    /* CCSO is not part of the Gopher protocol, but its very own
                        protocol and apart from floodgap.com's CCSO server there
                        is hardly any server to test interaction with. The CCSO
                        protocol can also be considered quite simple. A CCSO client
                        would be a software of its own, but sources are even fewer
                        than Gopher servers out there. Hence, Gophie allows the
                        user to use CCSO servers throgh their Telnet client. */
                    openTelnetSession(item.getHostName(), item.getPortNumber());
                    break;
                case TELNET:
                    /* handle telnet session requests */
                    openTelnetSession(item.getHostName(), item.getPortNumber());
                    break;
                case TELNET3270:
                    /* handle telnet 3270 session requests */
                    openTelnetSession(item.getHostName(), item.getPortNumber());
                    break;
                default:
                    /* check what type of link was requested and execute
                        the appropriate external application or use the
                        default approach for gopher content */
                    if (addressText.startsWith("https://") == true
                            || addressText.startsWith("http://") == true) {
                        /* this is the World Wide Web using HTTP or HTTPS,
                            so try to open the systems browser so that the
                            user can enjoy bloated javascript based html
                            content with the fine-art of pop-up advertising
                            and animated display banners */
                        openWebContent(addressText, item.getItemType());
                    } else if (addressText.startsWith("mailto:") == true) {
                        /* this is a mailto link */
                        openEmailClient(addressText.replace("mailto:", ""));
                    } else {
                        /* just fetch as regular gopher content */
                        fetchGopherContent(addressText, item.getItemType());
                    }
                    break;
            }
        }
    }

    private void openEmailClient(String emailAddress) {
        String confirmText = "Do you want to send an e-mail to \"" + emailAddress + "\"?";
        String[] optionList = new String[]{"Create new e-mail", "Dismiss"};
        messageView.showConfirm(confirmText, optionList, (int option) -> {
            if (option == 0) {
                /* launch the system email client */
                if (Desktop.isDesktopSupported() == true
                        && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        /* launch the mailto handler of the system */
                        Desktop.getDesktop().browse(new URI("mailto:" + emailAddress));
                    } catch (IOException | URISyntaxException ex) {
                        /* Error: cannot open email client */
                        System.out.println("Unable to open system's "
                                + "email client: " + ex.getMessage());
                    }
                }
                /* hide the message view */
                messageView.setVisible(false);
            } else {
                /* hide the message view */
                messageView.setVisible(false);
            }
        });
    }

    private void openTelnetSession(String hostName, int portNumber) {
        String confirmText = "Open a Telnet session with \"" + hostName + ":" + portNumber + "\"?";
        String[] optionList = new String[]{"Open Telnet", "Dismiss"};
        messageView.showConfirm(confirmText, optionList, (int option) -> {
            if (option == 0) {
                /* launch the system WWW browser */
                if (Desktop.isDesktopSupported() == true
                        && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        /* launch the systems telnet client by creating
                        a telnet URI and calling the systems protocol handler */
                        String telnetUri = "telnet://" + hostName + ":" + portNumber;
                        Desktop.getDesktop().browse(new URI(telnetUri));
                    } catch (IOException | URISyntaxException ex) {
                        /* Error: cannot open telnet client */
                        System.out.println("Unable to open system's "
                                + "telnet client: " + ex.getMessage());
                    }
                }
                /* hide the message view */
                messageView.setVisible(false);
            } else {
                /* hide the message view */
                messageView.setVisible(false);
            }
        });
    }

    private void openWebContent(String addressText, GopherItemType contentType) {
        String confirmText = "Open \"" + addressText + "\" with your web browser?";
        String[] optionList = new String[]{"Open Website", "Dismiss"};
        messageView.showConfirm(confirmText, optionList, (int option) -> {
            if (option == 0) {
                /* launch the system WWW browser */
                if (Desktop.isDesktopSupported() == true
                        && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        /* launch the systems WWW browser */
                        Desktop.getDesktop().browse(new URI(addressText));
                    } catch (IOException | URISyntaxException ex) {
                        /* Error: cannot enjoy bloated javascript
                        stuffed World Wide Web pages! */
                        System.out.println("Unable to open system's "
                                + "world wide web browser: " + ex.getMessage());
                    }
                }
                /* hide the message view */
                messageView.setVisible(false);
            } else {
                /* hide the message view */
                messageView.setVisible(false);
            }
        });
    }

    private void fetchGopherContent(String addressText, GopherItemType contentType) {
        /* this is default gopher content */
 /* activate the load indicator in the address bar */
        navigationBar.setIsLoading(true);

        /* update the navigation bar with the new address */
        navigationBar.setAddressText(addressText);

        try {
            /* try to execute the thread */
            gopherClient.fetchAsync(addressText, contentType, this);
        } catch (Exception ex) {
            /* might throw an ex when thread is interrupted */
            System.out.println("Exception while fetching async: " + ex.getMessage());
        }
    }

    @Override
    public void backwardRequested() {
        /* set the new history position */
        if (historyPosition > 0) {
            historyPosition--;

            /* get the new page from history */
            pageLoaded(history.get(historyPosition));

            /* update the history */
            updateHistory(history.get(historyPosition));
        }
    }

    @Override
    public void forwardRequested() {
        /* set the new history position */
        if (historyPosition < (history.size() - 1)) {
            historyPosition++;

            /* get the new page from history */
            pageLoaded(history.get(historyPosition));

            /* update the history */
            updateHistory(history.get(historyPosition));
        }
    }

    @Override
    public void refreshRequested() {
        /* get the current gopher page to reload it */
        GopherPage currentPage = history.get(historyPosition);

        /* reload practically means just requesting this page again */
        fetchGopherContent(currentPage.getUrl().getUrlString(), currentPage.getContentType());
    }

    @Override
    public void stopRequested() {
        /* cancel any current operation */
        gopherClient.cancelFetch();

        /* notify the local handler about cancellation by the user */
        pageLoadFailed(GopherError.USER_CANCELLED, null);
    }

    @Override
    public void pageLoaded(GopherPage result) {
        /* set the window title to the url of this page */
        frame.setTitle(result.getUrl().getUrlString()
                + " (" + DataSizeView.get(result.getByteArray().length) + ")"
                + " - " + APPLICATION_TITLE);

        /* update the address text with the loaded page */
        navigationBar.setAddressText(result.getUrl().getUrlString());

        /* detect the content type and determine how the handle it */
        if (result.getContentType() == GopherItemType.GOPHERMENU) {
            /* this is a gopher menu hence it is rendered like
                one including highlighting of links and
                the menu icons for the various item types */
            pageView.showGopherPage(result);
        } else {
            /* this is plain content, so render it
                appropriately and let the view decide
                on how to handle the content */
            pageView.showGopherContent(result);
        }

        /* update the history */
        updateHistory(result);

        /* reset the loading indicators */
        navigationBar.setIsLoading(false);
    }

    @Override
    public void pageLoadFailed(GopherError error, GopherUrl url) {
        /* show message for connection timeout */
        if (error == GopherError.CONNECT_FAILED) {
            if (url != null) {
                messageView.showInfo("Connection refused: " + url.getHost());
            }
        }

        /* show message for connection timeout */
        if (error == GopherError.CONNECTION_TIMEOUT) {
            if (url != null) {
                messageView.showInfo("Connection timed out: " + url.getHost());
            }
        }

        /* show DNS or host not found error */
        if (error == GopherError.HOST_UNKNOWN) {
            if (url != null) {
                messageView.showInfo("Server not found: " + url.getHost());
            }
        }

        /* show some information about an exception */
        if (error == GopherError.EXCEPTION) {
            messageView.showInfo("Ouchn, an unknown error occured.");
        }

        /* output some base information to the console */
        System.out.println("Failed to load gopher page: " + error.toString());

        /* reset the navigation bar status */
        navigationBar.setIsLoading(false);
    }

    @Override
    public void progress(GopherUrl url, long byteCount) {
        /* report the download size in the title bar */
        frame.setTitle(url.getUrlString()
                + " (" + DataSizeView.get(byteCount) + ")"
                + " - " + APPLICATION_TITLE);
    }

    @Override
    public void showDownloadRequested() {
        if (DownloadWindow.INSTANCE.isVisible()) {
            DownloadWindow.INSTANCE.hide();
        } else {
            DownloadWindow.INSTANCE.show(frame);
        }
    }

    @Override
    public void setHomeGopherRequested(String url) {
        /* set the gopher home to the config file */
        ConfigFile configFile = ConfigurationManager.getConfigFile();
        configFile.set("Navigation", "GOPHERHOME", url);
        configFile.save();
    }



    @Override
    public void pageSaveRequested(GopherPage page) {
        /* let user select where to store the file */
        FileDialog fileDialog = new FileDialog(frame, "Save current file", FileDialog.SAVE);
        fileDialog.setFile(page.getFileName());
        fileDialog.setVisible(true);
        String targetFileName = fileDialog.getDirectory() + fileDialog.getFile();
        if (targetFileName.equals("null") == false
                && targetFileName.equals("nullnull") == false) {
            /* pass url and target file to download manager */
            page.saveAsFile(targetFileName);
        }
    }

    @Override
    public void selectAllTextRequested() {
        /* hand that one back to the page view */
        pageView.selectAllText();
    }

    @Override
    public void homeGopherRequested() {
        ConfigFile configFile = ConfigurationManager.getConfigFile();
        String homeGopherUrl = configFile.get("Navigation", "GOPHERHOME", DEFAULT_GOPHERHOME);
        fetchGopherContent(homeGopherUrl, GopherItemType.GOPHERMENU);
        navigationBar.setAddressText(homeGopherUrl);
    }
}
