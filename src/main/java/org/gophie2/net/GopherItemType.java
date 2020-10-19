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
package org.gophie2.net;

/**
 * defines the official types of gopher items
 */
public enum GopherItemType {
    /* Canonical types */
    TEXTFILE("0", "txt", false, "Text file"), // 0 = Text file
    GOPHERMENU("1", "gophermap", false, "Gopher menu"), // 1 = Gopher (sub-)menu
    CCSCO_NAMESERVER("2", "ccso", false, "CCSO Nameserver"), // 2 = CCSO Nameserver
    ERRORCODE("3", "error", false, "Error code"), // 3 = Error code returned by a Gopher server to indicate failure
    BINHEX_FILE("4", "hqx", true, "BinHex file (Macintosh)"), // 4 = BinHex-encoded file (primarily for Macintosh computers)
    DOS_FILE("5", "dat", true, "DOS file"), // 5 = DOS file
    UUENCODED_FILE("6", "uue", true, "uuencoded file"), // 6 = uuencoded file
    FULLTEXT_SEARCH("7", "txt", false, "Full-text search"), // 7 = Gopher full-text search
    TELNET("8", "txt", false, "Telnet"), // 8 = Telnet
    BINARY_FILE("9", "dat", true, "Binary file"), // 9 = Binary file
    MIRROR("+", "txt", false, "Mirror"), // + = Mirror or alternate server (load balance or failover)
    GIF_FILE("g", "gif", false, "GIF file"), // g = GIF file
    IMAGE_FILE("I", "jpg", false, "Image file"), // I = Image file
    TELNET3270("T", "txt", false, "Telnet 3270"), // T = Telnet 3270
    /* Non-canonical types */
    HTML_FILE("h", "htm", false, "HTML file"), // h = HTML file
    INFORMATION("i", "txt", false, "Information"), // i = Informational message
    SOUND_FILE("s", "wav", true, "Sound file"), // s = Sound file (especially the WAV format)
    /* Gophie specific (Non-standard) */
    UNKNOWN("?", "dat", false, "Unknown");
    // any other unknown item type code
    private final String fileExt;
    private final boolean binary;
    private final String typeName;
    private final String code;

    private GopherItemType(String code, String fileExtension, boolean isBinary, String typeName) {
        this.code = code;
        this.fileExt = fileExtension;
        this.binary = isBinary;
        this.typeName = typeName;
    }

    /**
     * Returns the default file extension for the provided gopher item type
     *
     * @return File extension as string
     */
    public String getFileExt() {
        return fileExt;
    }

    /**
     * Returns whether this item is supposed to be some sort of binary file that
     * needs to be downloaded or handled differently
     *
     * @return true when is binary file, otherwise false
     */
    public boolean isBinary() {
        return binary;
    }

    /**
     * Returns the type name in a human readable format
     *
     * @return Type name of this gopher item as human readable string
     */
    public String getTypeName() {
        return typeName;
    }

    public String getTypeCode() {
        return code;
    }

    public static GopherItemType getByCode(String code) {
        for (GopherItemType t : values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }
        return UNKNOWN;
    }

}
