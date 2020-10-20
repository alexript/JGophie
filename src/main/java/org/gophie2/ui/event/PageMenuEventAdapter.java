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
package org.gophie2.ui.event;

import org.gophie2.net.GopherMenuItem;
import org.gophie2.net.GopherMenu;

/**
 *
 * @author malyshev
 */
public class PageMenuEventAdapter implements PageMenuEventListener{

    public PageMenuEventAdapter() {
        
    }

    @Override
    public void setHomeGopherRequested(String url) {

    }

    @Override
    public void itemDownloadRequested(GopherMenuItem item) {

    }

    @Override
    public void pageSaveRequested(GopherMenu page) {

    }

    @Override
    public void selectAllTextRequested() {

    }

}
