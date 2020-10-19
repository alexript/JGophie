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
package org.gophie2.ui.tk.history;

import java.util.ArrayList;
import org.gophie2.net.GopherPage;
import org.gophie2.ui.MainWindow;
import org.gophie2.ui.NavigationBar;

/**
 *
 * @author malyshev
 */
public class History extends ArrayList<GopherPage> {

    private static final long serialVersionUID = 5761909433840183093L;
    private int historyPosition;
    private final NavigationBar navigator;
    private final MainWindow parent;

    public History(MainWindow parent, NavigationBar navigator) {
        super();
        this.parent = parent;
        this.navigator = navigator;
        historyPosition = -1;
    }

    public void updateHistory(GopherPage page) {
        Boolean addToHistory = false;

        /* check if current position is at last page */
        if (historyPosition == size() - 1) {
            /* add this page to the history */
            if (size() > 0) {
                /* make sure this was not just a reload and the last
                    page in the history is not already ours */
                if (!get(size() - 1).getUrl().getUrlString()
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
            if (!get(historyPosition).getUrl()
                    .getUrlString().equals(page.getUrl().getUrlString())) {
                /* it is a new page outside the history, keep the history
                    up until the current page and add this page as a new
                    branch to the history, eliminating the
                    previous branch forward */
                ArrayList<GopherPage> updatedHistory = new ArrayList<>();
                for (int h = 0; h <= historyPosition; h++) {
                    updatedHistory.add(get(h));
                }

                /* update the history */
                clear();
                addAll(updatedHistory);

                /* allow adding to history */
                addToHistory = true;
            }
        }

        /* reset navigation allowance */
        navigator.setNavigateBack(false);
        navigator.setNavigateForward(false);

        /* add to history, if allowed */
        if (addToHistory == true) {
            /* add to the stack of pages */
            add(page);

            /* update position to the top */
            historyPosition = size() - 1;

            /* disable forward */
            navigator.setNavigateForward(false);
            if (size() > 1) {
                /* allow back if more than just this page exist */
                navigator.setNavigateBack(true);
            }
        } else {
            /* if position is 0, there is nowhere to go back to */
            if (historyPosition > 0) {
                /* allow navigation back in history */
                navigator.setNavigateBack(true);
            }
            if (historyPosition < (size() - 1)) {
                /* if position is at the end, there is nowhere
                    to move forward to */
                navigator.setNavigateForward(true);
            }
        }
    }

    public GopherPage current() {
        return get(historyPosition);
    }

    public void backwardRequested() {
        /* set the new history position */
        if (historyPosition > 0) {
            historyPosition--;

            /* get the new page from history */
            parent.pageLoaded(get(historyPosition));

            /* update the history */
            updateHistory(get(historyPosition));
        }
    }

    public void forwardRequested() {
        /* set the new history position */
        if (historyPosition < (size() - 1)) {
            historyPosition++;

            /* get the new page from history */
            parent.pageLoaded(get(historyPosition));

            /* update the history */
            updateHistory(get(historyPosition));
        }
    }
}
