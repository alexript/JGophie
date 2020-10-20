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

public class GopherNetworkException extends Exception {

    private static final long serialVersionUID = 5695176349566514307L;



    private final Error errorCode;
    private final String errorMessage;

    public GopherNetworkException(Error errorTypeCode, String errorTypeMessage) {
        this.errorCode = errorTypeCode;
        this.errorMessage = errorTypeMessage;
    }

    public String getGopherErrorMessage() {
        return this.errorMessage;
    }

    public Error getGopherErrorType() {
        return this.errorCode;
    }
}
