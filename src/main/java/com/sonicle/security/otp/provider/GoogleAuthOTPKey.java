/* 
 * Copyright (C) 2014 Sonicle S.r.l.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY SONICLE, SONICLE DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle[at]sonicle[dot]com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * Sonicle logo and Sonicle copyright notice. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Copyright (C) 2014 Sonicle S.r.l.".
 */
package com.sonicle.security.otp.provider;

import com.sonicle.security.otp.OTPKey;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matteo
 */
public class GoogleAuthOTPKey extends OTPKey {
	private static final String QR_URL_FORMAT = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl={0}";
	public static final String AUTHENTICATOR_URI_FORMAT = "otpauth://totp/{0}?secret={1}&issuer={2}";
	
	private final List<Integer> scratchCodes;
	
	public GoogleAuthOTPKey(String secretKey, int code, List<Integer> scratchCodes) {
		super(secretKey, code);
		this.scratchCodes = new ArrayList<>(scratchCodes);
	}
	
	public List<Integer> getScratchCodes() {
		return this.scratchCodes;
	}
	
	public static String buildQRBarcodeURL(String issuer, String secret, String account) {
		return MessageFormat.format(QR_URL_FORMAT, buildAuthenticatorURI(issuer, secret, account));
	}
	
	public static String buildAuthenticatorURI(String issuer, String secret, String account) {
		return MessageFormat.format(AUTHENTICATOR_URI_FORMAT, account, secret, issuer);
	}
}
