/*
 * Copyright 2007-2017 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright (C) 2007-2017 Ping Identity Corporation
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package com.unboundid.ldap.sdk.extensions;



import org.testng.annotations.Test;

import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.ExtendedRequest;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSDKTestCase;
import com.unboundid.ldap.sdk.ResultCode;



/**
 * This class provides a set of test cases for the WhoAmIExtendedRequest class.
 */
public class WhoAmIExtendedRequestTestCase
       extends LDAPSDKTestCase
{
  /**
   * Tests the first constructor.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testConstructor1()
         throws Exception
  {
    WhoAmIExtendedRequest extendedRequest = new WhoAmIExtendedRequest();
    extendedRequest = new WhoAmIExtendedRequest(extendedRequest);
    extendedRequest = extendedRequest.duplicate();

    assertNotNull(extendedRequest.getOID());
    assertEquals(extendedRequest.getOID(), "1.3.6.1.4.1.4203.1.11.3");

    assertNull(extendedRequest.getValue());

    assertNotNull(extendedRequest.getControls());
    assertEquals(extendedRequest.getControls().length, 0);

    assertNotNull(extendedRequest.getExtendedRequestName());
    assertNotNull(extendedRequest.toString());
  }



  /**
   * Tests the second constructor.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testConstructor2()
         throws Exception
  {
    Control[] controls =
    {
      new Control("1.2.3.4"),
      new Control("1.2.3.5", true, null)
    };

    WhoAmIExtendedRequest extendedRequest = new WhoAmIExtendedRequest(controls);
    extendedRequest = new WhoAmIExtendedRequest(extendedRequest);
    extendedRequest = extendedRequest.duplicate();

    assertNotNull(extendedRequest.getOID());
    assertEquals(extendedRequest.getOID(), "1.3.6.1.4.1.4203.1.11.3");

    assertNull(extendedRequest.getValue());

    assertNotNull(extendedRequest.getControls());
    assertEquals(extendedRequest.getControls().length, 2);

    assertNotNull(extendedRequest.getExtendedRequestName());
    assertNotNull(extendedRequest.toString());
  }



  /**
   * Tests the third constructor with a generic request containing a value.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test(expectedExceptions = { LDAPException.class })
  public void testConstructor5WithValue()
         throws Exception
  {
    new WhoAmIExtendedRequest(new ExtendedRequest("1.2.3.4",
                                                  new ASN1OctetString("foo")));
  }



  /**
   * Tests the ability to use the "Who Am I?" extended operation over protocol.
   * <BR><BR>
   * Access to a Directory Server instance is required for complete processing.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testSendWhoAmIRequest()
         throws Exception
  {
    if (! isDirectoryInstanceAvailable())
    {
      return;
    }

    LDAPConnection conn = getAdminConnection();

    WhoAmIExtendedRequest request = new WhoAmIExtendedRequest();
    WhoAmIExtendedResult result = request.process(conn, 1);

    assertNotNull(result);
    assertEquals(result.getResultCode(), ResultCode.SUCCESS);

    assertNotNull(result.getAuthorizationID());

    assertNotNull(result.toString());

    conn.close();
  }
}
