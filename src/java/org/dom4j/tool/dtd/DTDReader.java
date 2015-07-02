/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: DTDReader.java,v 1.2 2001/02/01 23:32:46 jstrachan Exp $
 */

package org.dom4j.tool.dtd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;

import org.dom4j.DocumentException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.helpers.XMLReaderFactory;


/** <p><code>DTDReader</code> reads DTD events from a SAX2 parser
  * and generates a document object model.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision: 1.2 $
  */
public class DTDReader {

    private static String DEFAULT_SAX_DRIVER = "org.apache.xerces.parsers.SAXParser";

    /** The classname of the SAX <code>XMLReader</code>  to use */
    private String xmlReaderClassName;
    
    /** <code>XMLReader</code> used to parse the SAX events */
    private XMLReader xmlReader;
    
    /** ErrorHandler class to use */
    private ErrorHandler errorHandler = null;
        
    
    public static void main(String[] args) throws Exception {
        if ( args.length <= 0 ) {
            System.out.println( "Usage: <xmlFileName> [xmlReaderClassName]" );
            return;
        }

        try {
            DTDReader reader = new DTDReader();        
            
            if ( args.length > 1 ) {
                reader.setXMLReaderClassName( args[1] );
            }
            DocumentDecl model = reader.read( args[0] );
            
            model.write( new PrintWriter(System.out) );
        }
        catch (Exception e) {
            System.out.println( "Exception occurred: " + e );
            e.printStackTrace ();
        }
    }
    
    public DTDReader() {
        xmlReaderClassName = System.getProperty( "org.xml.sax.driver", DEFAULT_SAX_DRIVER );
    }
    
    public DTDReader(String xmlReaderClassName) {
        this.xmlReaderClassName = xmlReaderClassName;
    }

    
    /** @return the <code>ErrorHandler</code> used by SAX
      */
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /** Sets the <code>ErrorHandler</code> used by the SAX 
      * <code>XMLReader</code>.
      *
      * @param errorHandler is the <code>ErrorHandler</code> used by SAX
      */
    public void setErrorHandler(ErrorHandler errorHandler) {
        errorHandler = errorHandler;
    }

    /** @return the <code>XMLReader</code> used to parse SAX events
      */
    public XMLReader getXMLReader() throws SAXException {
        if (xmlReader == null) {
            xmlReader = createXMLReader();
        }
        return xmlReader;
    }

    /** Sets the <code>XMLReader</code> used to parse SAX events
      *
      * @param xmlReader is the <code>XMLReader</code> to parse SAX events
      */
    public void setXMLReader(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    /** Sets the class name of the <code>XMLReader</code> to be used 
      * to parse SAX events.
      *
      * @param xmlReaderClassName is the class name of the <code>XMLReader</code> 
      * to parse SAX events
      */
    public void setXMLReaderClassName(String xmlReaderClassName) throws SAXException {
        this.xmlReaderClassName = xmlReaderClassName;
    }

    /** <p>Reads a DocumentDecl from the given <code>File</code></p>
      *
      * @param file is the <code>File</code> to read from.
      * @return the newly created DocumentDecl instance
      * @throws DocumentException if an error occurs during parsing.
      * @throws FileNotFoundException if the file could not be found
      */
    public DocumentDecl read(File file) throws DocumentException, FileNotFoundException {
        //return read(new BufferedReader(new FileReader(file)));
        return read(file.getAbsolutePath());
    }
    
    /** <p>Reads a DocumentDecl from the given <code>URL</code> using SAX</p>
      *
      * @param url <code>URL</code> to read from.
      * @return the newly created DocumentDecl instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public DocumentDecl read(URL url) throws DocumentException {
        String systemID = url.toExternalForm();
        return read(new InputSource(systemID));
    }
    
    /** <p>Reads a DocumentDecl from the given URI using SAX</p>
      *
      * @param systemId is the URI for the input
      * @return the newly created DocumentDecl instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public DocumentDecl read(String systemId) throws DocumentException {
        return read(new InputSource(systemId));
    }

    /** <p>Reads a DocumentDecl from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @return the newly created DocumentDecl instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public DocumentDecl read(InputStream in) throws DocumentException {
        return read(new InputSource(in));
    }

    /** <p>Reads a DocumentDecl from the given <code>Reader</code> using SAX</p>
      *
      * @param reader is the reader for the input
      * @return the newly created DocumentDecl instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public DocumentDecl read(Reader reader) throws DocumentException {
        return read(new InputSource(reader));
    }

    /** <p>Reads a DocumentDecl from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @param systemId is the URI for the input
      * @return the newly created DocumentDecl instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public DocumentDecl read(InputStream in, String systemId) throws DocumentException {
        InputSource source = new InputSource(in);
        source.setSystemId(systemId);
        return read(source);
    }

    /** <p>Reads a DocumentDecl from the given <code>Reader</code> using SAX</p>
      *
      * @param reader is the reader for the input
      * @param systemId is the URI for the input
      * @return the newly created DocumentDecl instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public DocumentDecl read(Reader reader, String SystemId) throws DocumentException {
        InputSource source = new InputSource(reader);
        source.setSystemId(SystemId);
        return read(source);
    }
    
    /** <p>Reads a DocumentDecl from the given <code>InputSource</code> using SAX</p>
      *
      * @param in <code>InputSource</code> to read from.
      * @param systemId is the URI for the input
      * @return the newly created DocumentDecl instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public DocumentDecl read(InputSource in) throws DocumentException {
        try {
            XMLReader reader = getXMLReader();

            DTDHandler handler = new DTDHandler();
            reader.setProperty(
                "http://xml.org/sax/properties/declaration-handler",
                handler
            );
        
            reader.parse(in);
            return handler.getDocumentDecl();
        } 
        catch (Exception e) {
            if (e instanceof SAXParseException) {
                SAXParseException parseException = (SAXParseException) e;
                String systemId = parseException.getSystemId();
                if ( systemId == null ) {
                    systemId = "";
                }
                String message = "Error on line " 
                    + parseException.getLineNumber()
                    + " of document "  + systemId
                    + " : " + parseException.getMessage();
                
                throw new DocumentException(message, e);
            }
            else {
                throw new DocumentException(e.getMessage(), e);
            }
        }
    }
    
    
    /** Factory Method to allow alternate methods of 
      * creating and configuring XMLReader objects
      */
    protected XMLReader createXMLReader() throws SAXException {
        return XMLReaderFactory.createXMLReader( xmlReaderClassName );
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
 * $Id: DTDReader.java,v 1.2 2001/02/01 23:32:46 jstrachan Exp $
 */
