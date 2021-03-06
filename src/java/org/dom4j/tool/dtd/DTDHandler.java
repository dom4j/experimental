/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: DTDHandler.java,v 1.1.1.1 2001/01/16 18:00:01 jstrachan Exp $
 */

package org.dom4j.tool.dtd;

import java.io.*;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/** <p><code>DTDHandler</code> reads DTD events from a SAX2 parser
  * and generates a document declaration model.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision: 1.1.1.1 $
  */
public class DTDHandler implements DeclHandler {

    private DocumentDecl documentDecl = new DocumentDecl();
    private ElementDecl elementDecl;
    
    public DocumentDecl getDocumentDecl() {
        return documentDecl;
    }
    
    public void setDocumentDecl(DocumentDecl documentDecl) {
        this.documentDecl = documentDecl;
    }
    
    public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
        //System.out.println( "Attribute: " + aName + " for element: " + eName + " type: " + type + " value: " + value + " default: " + valueDefault );
        
        elementDecl.add( new AttributeDecl( eName, aName, type, valueDefault, value ) );
    }
    
    public void elementDecl(String name, String model) throws SAXException {
        //System.out.println( "Element: " + name + " has model: " + model );
        
        elementDecl = new ElementDecl( name, model );
        documentDecl.add( elementDecl );
    }
    
    public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
    }
    
    public void internalEntityDecl(String name, String value) throws SAXException {
    }
        
}




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id: DTDHandler.java,v 1.1.1.1 2001/01/16 18:00:01 jstrachan Exp $
 */
