/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: DocumentGenerator.java,v 1.2 2001/02/01 23:32:46 jstrachan Exp $
 */

package org.dom4j.tool.generator;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.tool.dtd.ElementDecl;
import org.dom4j.tool.dtd.ElementDecl;

import org.metastuff.coder.*;

/** <p><code>DocumentGenerator</code> generates a {@link Document} 
  * implementation from a DTD declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision: 1.2 $
  */
public class DocumentGenerator extends AbstractGenerator {

    /** Holds value of property contentFactoryClassName. */
    private String contentFactoryClassName = "SchemaDocumentFactory";
    
    public DocumentGenerator() {
        super( "SchemaDocument" );
    }
    
    public DocumentGenerator(String nameExpression) {
        super( nameExpression );
    }
    
    
    /** Getter for property contentFactoryClassName.
     * @return Value of property contentFactoryClassName.
     */
    public String getDocumentFactoryClassName() {
        return contentFactoryClassName;
    }
    
    /** Setter for property contentFactoryClassName.
     * @param contentFactoryClassName New value of property contentFactoryClassName.
     */
    public void setDocumentFactoryClassName(String contentFactoryClassName) {
        this.contentFactoryClassName = contentFactoryClassName;
    }
    
    
    
    
    protected void enrich(JClass jclass) {
        super.enrich(jclass);
        
        jclass.addImportStatement( "org.dom4j.DocumentFactory" );
        jclass.addImportStatement( "org.dom4j.Document" );
        jclass.addImportStatement( "org.dom4j.Element" );
        jclass.addImportStatement( "org.dom4j.tree.DefaultDocument" );
        
        jclass.setComment( createComment() );
        jclass.setExtendsClass( "DefaultDocument" );
        
        addConstructors();
        addDocumentFactory();
    }
    
    protected void addConstructors() {
        jclass.addConstructor( new JConstructor() );
        
/*        
        JConstructor constructor = new JConstructor();
        constructor.addParameter( new JParameter( "element", "Element" ) );
        constructor.addStatement( "super( element );" );
        jclass.addConstructor( constructor );
*/
    }
    
    protected void addDocumentFactory() {
        jclass.addMember( 
            new JMember(
                "CONTECT_FACTORY", 
                "DocumentFactory", 
                JModifier.PROTECTED_STATIC_FINAL, 
                getDocumentFactoryClassName() + ".getInstance()",
                "The <code>DocumentFactory</code> instance used by default"
            )
        );
        
        JMethod method = new JMethod( 
            "getDocumentFactory", 
            "DocumentFactory", 
            JModifier.PROTECTED, 
            "@return the <code>DocumentFactory</code> instance to be used for the <code>Element</code>" 
        );
        method.addStatement( "return CONTECT_FACTORY;" );
        jclass.addMethod( method );
    }
    
    protected String createComment() {
        return "An {@link Document} implementation for this schema";
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
 * $Id: DocumentGenerator.java,v 1.2 2001/02/01 23:32:46 jstrachan Exp $
 */
