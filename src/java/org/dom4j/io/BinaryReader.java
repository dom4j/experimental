/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.TreeException;

/** <p><code>BinaryReader</code> reads a DOM4J tree from a binary stream.
  * This <code>TreeReader</code> is useful when communicating DOM4J structures with 
  * multiple processes when performance is important.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class BinaryReader extends TreeReader implements BinaryConstants {

    public BinaryReader() {
    }

    public BinaryReader(DocumentFactory factory) {
        super(factory);
    }
    
    /** <p>Reads a Document from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      */
    public Document read(InputStream in) throws TreeException {
        DataInputStream dataIn = null;
        try {
            dataIn = createDataInputStream(in);
            
            Document document = createDocument();
            readDocument(document, dataIn);
            return document;
        }
        catch (IOException e) {
            throw new TreeException(e);
        }
        finally {
            if ( dataIn != null ) {
                try {
                    dataIn.close();
                }
                catch (Exception e) {
                }
            }
        }
    }

    
    // Implementation methods
    
    protected void readHeader(DataInputStream in) throws IOException, TreeException {
        String header = in.readUTF();
        if (header == null || ! header.equals( HEADER ) ) {
            throw new TreeException( "Header missing. Invalid binary DOM4J stream" );
        }
    }
    
    protected void readDocument(Document document, DataInputStream in) throws IOException, TreeException {
        document.setName( readString(in) );
        
        readBranchContents(document, in);
    }
    
    protected void readBranchContents(Branch branch, DataInputStream in) throws IOException, TreeException {
        while (true) {
            byte b = readOpCode(in);
            switch (b) {
                case ELEMENT_START:
                    readElement(branch, in);
                    break;
                case ELEMENT_END:
                    return;
                case CDATA_START:
                    readCDATA(branch, in);
                    break;
                case COMMENT_START:
                    readComment(branch, in);
                    break;
                case ENTITY_START:
                    readEntity(branch, in);
                    break;
                case NAMESPACE_START:
                    readNamespace(branch, in);
                    break;
                case PI_START:
                    readPI(branch, in);
                    break;
                case TEXT_START:
                    readText(branch, in);
                    break;
                default:
                    throw new TreeException("Invalid binary DOM4J format stream" );
            }
        }
    }
    
    protected void readElement(Branch branch, DataInputStream in) throws IOException, TreeException {
        String name = readString(in);
        String prefix = readString(in);
        String uri = readString(in);
        Element element = branch.addElement(name, prefix, uri);
        int attributeCount = readAttributeCount(in);
        for (int i = 0; i < attributeCount; i++ ) {
            readAttribute(element, in);
        }
        readBranchContents(element, in);
    }
    
    protected void readAttribute(Element element, DataInputStream in) throws IOException, TreeException {
        String name = readString(in);
        String value = readString(in);
        String prefix = readString(in);
        Namespace namespace = element.getNamespaceForPrefix(prefix);
        if ( namespace == null ) {
            System.out.println( "#### WARNING: null namespace!" );
            element.setAttributeValue(name, value);
        }
        else {
            element.setAttributeValue(name, value, namespace);
        }
    }
    
    protected void readCDATA(Branch branch, DataInputStream in) throws IOException, TreeException {
        Element element = asElement(branch);
        element.addCDATA( readString(in) );
    }
    
    protected void readComment(Branch branch, DataInputStream in) throws IOException, TreeException {
        Element element = asElement(branch);
        element.addComment( readString(in) );
    }
    
    protected void readText(Branch branch, DataInputStream in) throws IOException, TreeException {
        Element element = asElement(branch);
        element.addText( readString(in) );
    }
    
    protected void readEntity(Branch branch, DataInputStream in) throws IOException, TreeException {
        Element element = asElement(branch);
        String name = readString(in);
        String text = readString(in);
        element.addEntity( name, text );
    }
    
    protected void readNamespace(Branch branch, DataInputStream in) throws IOException, TreeException {
        Element element = asElement(branch);
        String prefix = readString(in);
        String uri = readString(in);
        element.addAdditionalNamespace( prefix, uri);
    }
    
    protected void readPI(Branch branch, DataInputStream in) throws IOException, TreeException {
        Element element = asElement(branch);
        String target = readString(in);
        String data = readString(in);
        element.addProcessingInstruction( target, data );
    }
    
    protected byte readOpCode(DataInputStream in) throws IOException {
        return in.readByte();
    }
    
    protected String readString(DataInputStream in) throws IOException {
        return in.readUTF();
    }
    
    protected int readAttributeCount(DataInputStream in) throws IOException {
        return in.readInt();
    }
    
    protected Element asElement(Branch branch) throws TreeException {
        if ( branch instanceof Element ) {
            return (Element) branch;
        }
        else {
            throw new TreeException( "Invalid DOM4J format. "
                + "Attempted to add Element content to a Document" 
            );
        }
    }
    
    protected DataInputStream createDataInputStream(InputStream in) throws IOException, TreeException {
        DataInputStream dataIn = new DataInputStream(in);
        readHeader(dataIn);
        return dataIn;
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
 * $Id$
 */
