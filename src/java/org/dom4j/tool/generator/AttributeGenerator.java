/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: AttributeGenerator.java,v 1.1.1.1 2001/01/16 18:00:01 jstrachan Exp $
 */

package org.dom4j.tool.generator;

import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.tool.dtd.ElementDecl;
import org.dom4j.tool.dtd.AttributeDecl;

import org.metastuff.coder.*;

/** <p><code>AttributeGenerator</code> generates {@link Attribute} 
  * implementations from a DTD declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision: 1.1.1.1 $
  */
public abstract class AttributeGenerator extends AbstractGenerator {

    protected AttributeDecl attributeDecl;

    public AttributeGenerator() {
        super( "${elementName}_${attributeName}_Attribute" );
    }
    
    public AttributeGenerator(String nameExpression) {
        super( nameExpression );
    }
    
    
    public void setAttributeDecl(AttributeDecl attributeDecl) {
        this.attributeDecl = attributeDecl;
    }

    protected String getAttributeName() {
        return attributeDecl.getAttributeName();
    }
    
    protected String getElementName() {
        return attributeDecl.getElementName();
    }
    
    protected boolean hasNamespace() {
        return false;
    }
    
    protected String getNamespacePrefix() {
        return "";
    }
    
    protected String getNamespaceURI() {
        return "";
    }
    
    protected String createNamespaceExpression() {
        StringBuffer buffer = new StringBuffer( "NameModel.get( \"" );
        buffer.append( getAttributeName() );        
        if ( hasNamespace() ) {
            buffer.append( ", " );
            buffer.append( getNamespacePrefix() );        
            buffer.append( ", " );
            buffer.append( getNamespaceURI() );        
        }
        buffer.append( "\" )" );
        return buffer.toString();
    }
        
    protected String createComment() {
        return "An implementation of {@link Attribute} for the &lt;" 
            + getElementName() + "/&gt; element and attribute name '" 
            + getAttributeName() + "'";
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
 * $Id: AttributeGenerator.java,v 1.1.1.1 2001/01/16 18:00:01 jstrachan Exp $
 */
