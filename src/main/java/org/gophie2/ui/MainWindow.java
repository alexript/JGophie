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
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.util.logging.Logger;
import org.gophie2.ui.tk.search.SearchPanel;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.gophie2.config.ColorPalette;

import org.gophie2.config.ConfigFile;
import org.gophie2.config.ConfigurationManager;
import org.gophie2.view.DataSizeView;
import org.gophie2.net.GopherItem;
import org.gophie2.net.GopherItemType;
import org.gophie2.net.GopherPage;
import org.gophie2.net.GopherUrl;
import org.gophie2.net.event.GopherClientEventListener;
import org.gophie2.net.event.GopherError;
import org.gophie2.ui.event.NavigationInputListener;
import org.gophie2.ui.event.PageMenuEventAdapter;
import org.gophie2.ui.event.PageMenuEventListener;
import org.gophie2.ui.tk.download.ConfirmDownload;
import org.gophie2.ui.tk.history.History;
import org.gophie2.ui.tk.requesters.EmailRequester;
import org.gophie2.ui.tk.requesters.GopherRequester;
import org.gophie2.ui.tk.requesters.Requester;
import org.gophie2.ui.tk.requesters.TelnetRequester;
import org.gophie2.ui.tk.requesters.WebRequester;

public class MainWindow extends PageMenuEventAdapter implements NavigationInputListener, GopherClientEventListener, PageMenuEventListener {

    public static final String APPLICATION_TITLE = "Gophie2";

    public static final String DEFAULT_GOPHERHOME = "gopher.floodgap.com";

    private final GopherRequester gopher;

    private final History history;

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

        /* create the main window */
        frame = new JFrame(APPLICATION_TITLE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /* create the navigation bar */
        navigationBar = new NavigationBar(
                colors.getNavigationbarBackground(),
                colors.getNavigationbarText(),
                colors.getNavigationbarTextHover()
        );
        history = new History(this, navigationBar);

        gopher = new GopherRequester(this, navigationBar);

        /* create the page view component object */
        pageView = new PageView(this);
        pageView.addListener(this);

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
        searchInput = new SearchPanel(gopher);
        headerBar.add(searchInput);

        /* set the content pane */
        Container contentPane = frame.getContentPane();
        contentPane.add(navigationBar, BorderLayout.NORTH);
        contentPane.add(pageView, BorderLayout.CENTER);
        contentPane.add(headerBar, BorderLayout.SOUTH);

        /* start the window in the center of the screen */
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
                dim.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);

        /* fetch the default gopher home */
        gopher.request(messageView, gopherHome, GopherItemType.GOPHERMENU);
    }

    public void show() {
        /* display the window */
        frame.pack();
        frame.setVisible(true);
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
        } else {
            /* this is not a binary file, try to handle and render */
            switch (item.getItemType()) {
                case FULLTEXT_SEARCH:
                    requester = searchInput;
                    break;
                case CCSCO_NAMESERVER:
                    /* CCSO is not part of the Gopher protocol, but its very own
                        protocol and apart from floodgap.com's CCSO server there
                        is hardly any server to test interaction with. The CCSO
                        protocol can also be considered quite simple. A CCSO client
                        would be a software of its own, but sources are even fewer
                        than Gopher servers out there. Hence, Gophie allows the
                        user to use CCSO servers throgh their Telnet client. */
                    requester = new TelnetRequester();
                    break;
                case TELNET:
                    /* handle telnet session requests */
                    requester = new TelnetRequester();
                    break;
                case TELNET3270:
                    /* handle telnet 3270 session requests */
                    requester = new TelnetRequester();
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
                        requester = new WebRequester();
                    } else if (addressText.startsWith("mailto:") == true) {
                        /* this is a mailto link */
                        requester = new EmailRequester();
                    } else {
                        /* just fetch as regular gopher content */
                        requester = gopher;
                    }
                    break;
            }
        }
        requester.request(messageView, addressText, item);
    }

    @Override
    public void backwardRequested() {
        history.backwardRequested();
    }

    @Override
    public void forwardRequested() {
        history.forwardRequested();
    }

    @Override
    public void refreshRequested() {
        /* get the current gopher page to reload it */
        GopherPage currentPage = history.current();

        /* reload practically means just requesting this page again */
        gopher.request(messageView, currentPage.getUrl().getUrlString(), currentPage.getContentType());
    }

    @Override
    public void stopRequested() {
        /* cancel any current operation */
        gopher.cancel();

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
        history.updateHistory(result);

        /* reset the loading indicators */
        navigationBar.setIsLoading(false);
    }

    @Override
    public void pageLoadFailed(GopherError error, GopherUrl url) {
        String infoText = null;
        switch (error) {
            case CONNECT_FAILED:
                if (url != null) {
                    infoText = "Connection refused: " + url.getHost();
                }
                break;
            case CONNECTION_TIMEOUT:
                if (url != null) {
                    infoText = "Connection timed out: " + url.getHost();
                }
                break;
            case HOST_UNKNOWN:
                if (url != null) {
                    infoText = "Server not found: " + url.getHost();
                }
                break;
            case EXCEPTION:
                infoText = "Ouchn, an unknown error occured.";
                break;
        }
        if (infoText != null) {
            messageView.showInfo(infoText);
        }

        /* output some base information to the console */
        Logger.getLogger(this.getClass().getName()).warning("Failed to load gopher page: " + error.toString());

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
        gopher.request(messageView, homeGopherUrl, GopherItemType.GOPHERMENU);
        navigationBar.setAddressText(homeGopherUrl);
    }
}
