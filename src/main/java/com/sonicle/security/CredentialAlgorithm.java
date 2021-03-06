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
package com.sonicle.security;

import com.novell.ldap.util.Base64;
import java.security.MessageDigest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbinola
 */
public enum CredentialAlgorithm {
	PLAIN("PLAIN"),
    SHA("SHA"),
    DES("DES");
	
	private final static Logger logger = (Logger)LoggerFactory.getLogger(CredentialAlgorithm.class);
	private final String name;
	
	CredentialAlgorithm(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public static String encrypt(CredentialAlgorithm algorithm, String string) {
		if(algorithm.equals(PLAIN)) {
			return string;
		} else if(algorithm.equals(SHA)) {
			return encryptDigestBASE64(string, "SHA");
		} else if(algorithm.equals(DES)) {
			return PasswordUtils.encryptDES(string, string);
		} else {
			return null;
		}
	}
	
	public static boolean compare(CredentialAlgorithm algorithm, String string, String encryptedString) {
		if(algorithm.equals(PLAIN)) {
			return StringUtils.equals(string, encryptedString);
		} else if(algorithm.equals(SHA)) {
			return StringUtils.equals(encryptDigestBASE64(string, "SHA"), encryptedString);
		} else if(algorithm.equals(DES)) {
			return StringUtils.equals(PasswordUtils.encryptDES(string, string), encryptedString);
		} else {
			return false;
		}
	}
	
	/**
	 * Method ported from Credential.java class with no modifications (except  
	 * for the exception handling) in order to not break down encryption.
	 * @param s
	 * @param algorithm
	 * @return 
	 */
	private static String encryptDigestBASE64(String s, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(s.getBytes("UTF-8"));
			//TODO: rimuovere la dipendenza al Base64 della Novell, ottenendo
			//il medesimo risultato con la commons
			return Base64.encode(md.digest());
		} catch(Exception ex) {
			logger.error("Crypto error", ex);
			return null;
		}
	}

	/**
	 * Method ported from Credential.java class with no modifications (except  
	 * for the exception handling) in order to not break down encryption.
	 * @param cpass
	 * @param key
	 * @return 
	 */
	/*
	private static String decipherDES(String cpass, String key) {
		try {
			DESKeySpec ks = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKey sk = SecretKeyFactory.getInstance("DES").generateSecret(ks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, sk);
			byte[] dec = Base64.decode(cpass);
			byte[] utf8 = cipher.doFinal(dec);
			return new String(utf8, "UTF-8");
			
		} catch(Exception ex) {
			logger.error("Crypto error", ex);
			return null;
		}
	}
	*/
	
	/**
	 * Method ported from Credential.java class with no modifications (except  
	 * for the exception handling) in order to not break down encryption.
	 * @param pass
	 * @param key
	 * @return 
	 */
	/*
	private static String cipherDES(String pass, String key) {
		try {
			DESKeySpec ks = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKey sk = SecretKeyFactory.getInstance("DES").generateSecret(ks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, sk);
			return Base64.encode(cipher.doFinal(pass.getBytes("UTF-8")));
		} catch(Exception ex) {
			logger.error("Crypto error", ex);
			return null;
		}
	}
	*/
}
