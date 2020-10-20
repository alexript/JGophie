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
package org.gophie2.net;

public class GopherMenuItem {

    private static final int DEFAULT_PORT = 70;

    /* defines the type of this gopher item */
    private GopherMenuItemType itemType;

    /* the user display string of this gopher item */
    private String userDisplayString;

    /* defines the selector of this gopher item */
    private String selector;

    /* defines the host name of this gopher item */
    private String hostName;

    /* defines the port number of this gopher item */
    private int portNumber;

    /* constructs the gopher item taking the single line and parsing its content into the structure of this object
     *   @line       the single gophermenu line for this item
     */
    public GopherMenuItem(String line) {
        this();
        /* type code is defined as first character in line */
        this.itemType = GopherMenuItemType.getByCode(line.substring(0, 1));

        /* get all properties for this item */
        String[] property = line.replace("\r", "").replace("\n", "").split("\t");

        /* display string is first property without type code char */
        if (property.length > 0) {
            if (property[0].length() > 1) {
                this.userDisplayString = property[0].substring(1);
            }
        }

        /* second property is the selector */
        if (property.length > 1) {
            this.selector = property[1].trim();
        }

        /* third property is the host name of the target */
        if (property.length > 2) {
            this.hostName = property[2].trim();
        }

        /* fourth property is the port of the target host */
        if (property.length > 3) {
            try {
                /* try to parse the port number */
                this.portNumber = Integer.parseInt(property[3].trim());
            } catch (NumberFormatException ex) {
                /* report the failure */
                System.out.println("Found what was supposed to be a port number "
                        + "and it did not parse into an integer: " + ex.getMessage());
            }
        }
    }

    /**
     * Constructs an empty gopher item
     *
     */
    public GopherMenuItem() {
        portNumber = DEFAULT_PORT;
        itemType = GopherMenuItemType.UNKNOWN;
        userDisplayString = "";
        selector = "";
        hostName = "";
    }

    /*
        Returns the item type as GopherMenuItemType enum
     */
    public GopherMenuItemType getItemType() {
        return this.itemType;
    }

    /*
        Returns the user display string which is
        supposed to be used as the title of this
        gopher item when being displayed
     */
    public String getUserDisplayString() {
        return this.userDisplayString;
    }

    /*
        Returns the selector of this gopher item
     */
    public String getSelector() {
        return this.selector;
    }

    /*
        Returns the item host name as a string
     */
    public String getHostName() {
        return this.hostName;
    }

    /*
        Returns the port number for the gopher
        item's host to collect the content from
     */
    public int getPortNumber() {
        return this.portNumber;
    }

    /**
     * Creates a URL string for this item
     *
     * @return URL for this item as string
     */
    public String getUrlString() {
        String result = "";

        /* unknown or information links do not have
            any link associated with it */
        if (this.itemType.isUrlDisplayable()) {
            /* check if the selector contains a URL */
            if (this.selector.startsWith("URL:") == true
                    || this.selector.startsWith("/URL:") == true) {
                /* selector is link to other resource */
                if (this.selector.startsWith("/URL:")) {
                    result = this.selector.substring(5);
                } else {
                    result = this.selector.substring(4);
                }
            } else {
                /* protocol is definitely gopher */
                result = "gopher://" + this.hostName;
                if (this.portNumber != 70) {
                    result += ":" + this.portNumber;
                }

                /* add the slash to the URL if not present */
                if (!this.selector.startsWith("/")) {
                    result += "/";
                }

                /* finally append the selector */
                result += this.selector;
            }
        }

        return result;
    }

    /**
     * Returns the file name of this items file
     *
     * @return Filename as string as defined in the url
     */
    public String getFileName() {
        String result = this.getUrlString();

        if (result.lastIndexOf("/") > 0) {
            result = result.substring(result.lastIndexOf("/") + 1);
        }

        return result;
    }

    /**
     * Returns the file name and forces an extension on the filename. If the
     * file does not have an extension attached, it will come with the default
     * extension for the file type
     *
     * @return Filename as string with forced extension
     */
    public String getFileNameWithForcedExt() {
        String result = this.getFileName();

        /* check if the file has an extension */
        if (result.lastIndexOf(".") == -1) {
            result += "." + this.getItemType().getFileExt();
        }

        return result;
    }

    /**
     * Returns the file extension of the item if any
     *
     * @return The file extension as string, if unsure returns txt
     */
    public String getFileExt() {
        String result = "txt";

        String fileName = this.getFileName();
        if (fileName.lastIndexOf(".") > 0) {
            result = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        return result;
    }

}
