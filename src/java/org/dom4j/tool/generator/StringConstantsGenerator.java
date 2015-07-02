/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: StringConstantsGenerator.java,v 1.2 2001/02/01 23:32:46 jstrachan Exp $
 */

package org.dom4j.tool.generator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.tool.dtd.ElementDecl;
import org.dom4j.tool.dtd.AttributeDecl;

import org.metastuff.coder.*;

/** <p><code>StringConstantsGenerator</code> generates the string constants
  * used to generate custom DOM4J schemas.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision: 1.2 $
  */
public class StringConstantsGenerator extends AbstractGenerator {

    protected List names;
    protected List intCodes;
    protected List textCodes;

    public StringConstantsGenerator(String nameExpression) {
        super( nameExpression );
    }
    

    public List getNames() {
        return names;
    }
    
    public void setNames(List names) {
        this.names = names;
        this.intCodes = null;
        this.textCodes = null;
    }

    protected void enrich(JClass jclass) {
        super.enrich(jclass);
        
        jclass.setComment( createComment() );

        addIntCodes();
        addTextCodes();
        
        addToIntegerMethod();
        addFromIntegerMethod();
    }
    
    protected void addIntCodes() {
        List names = getNames();
        List intCodes = getIntCodes();
        int size = names.size();        
        for ( int i = 0; i < size; i++ ) {
            String name = (String) names.get(i);
            String intCode = (String) intCodes.get(i);

            jclass.addMember( 
                new JMember( 
                    intCode, 
                    "int", 
                    JModifier.PUBLIC_STATIC_FINAL, 
                    Integer.toString(i), 
                    "The constant for the integer code of the name '" + name + "'."
                )
            );
        }
        
        JMethod method = new JMethod( 
            "getIndexCount", 
            "int", 
            JModifier.PUBLIC_STATIC, 
            "@return the number of index codes for this class" 
        );
        method.addStatement( "return " + size + ";" );
        
        jclass.addMethod( method );
    }
    
    protected void addTextCodes() {
        List names = getNames();
        List textCodes = getTextCodes();

        int size = names.size();        
        for ( int i = 0; i < size; i++ ) {
            String name = (String) names.get(i);
            String textCode = (String) textCodes.get(i);

            jclass.addMember( 
                new JMember( 
                    textCode, 
                    "String", 
                    JModifier.PUBLIC_STATIC_FINAL, 
                    "\"" + name + "\"", 
                    "The constant for the name of the attribute '" + name + "'."
                )
            );
        }
    }
    
    protected void addToIntegerMethod() {
        JMethod method = new JMethod( "getIndex", "int", JModifier.PUBLIC_STATIC );
        method.setComment( 
            "<p>Converts a string name into an integer index code.</p> "
            + "@param name is the constant represented as a value "
            + "@return the integer index of the given string constant "
            + "or -1 if the string is not a valid" 
        );
        method.addParameter( new JParameter( "code", "String" ) );
        
        List intCodes = getIntCodes();
        List textCodes = getTextCodes();

        boolean first = true;
        int size = intCodes.size();        
        for ( int i = 0; i < size; i++ ) {
            String intCode = (String) intCodes.get(i);
            String textCode = (String) textCodes.get(i);
            
            if ( first ) {
                first = false;
            }
            else {
                method.addStatement( "else" );
            }
            method.addStatement( "if ( " + textCode + ".equals( code ) ) {" );
            method.addStatement( "    return " + intCode + ";" );
            method.addStatement( "}" );
        }
        method.addStatement( "return -1;" );
        
        jclass.addMethod( method );
    }
    
    protected void addFromIntegerMethod() {
        JMethod method = new JMethod( "getName", "String", JModifier.PUBLIC_STATIC );
        method.setComment( 
            "<p>Converts an index of a name into its corresponding String object.</p> " 
            + "@param index is the integer index of the name "
            + "@return the string name for the integer index "
            + "or null if the index does not match a valid name" 
        );
        method.addParameter( new JParameter( "index", "int" ) );
        
        method.addStatement( "switch ( index ) {" );

        List intCodes = getIntCodes();
        List textCodes = getTextCodes();
        int size = intCodes.size();        
        for ( int i = 0; i < size; i++ ) {
            String intCode = (String) intCodes.get(i);
            String textCode = (String) textCodes.get(i);
            
            method.addStatement( "case " + intCode + ":" );
            method.addStatement( "    return " + textCode + ";" );
        }
        method.addStatement( "}" );
        method.addStatement( "return null;" );
        
        jclass.addMethod( method );
    }

    protected List getIntCodes() {
        if ( intCodes == null ) {
            intCodes = new ArrayList();
            int size = names.size();
            for ( int i = 0; i < size; i++ ) {
                String name = (String) names.get(i);
                String intCode = toIntCode(name);
                intCodes.add(intCode);
            }
        }
        return intCodes;
    }
    
    protected List getTextCodes() {
        if ( textCodes == null ) {
            textCodes = new ArrayList();
            int size = names.size();
            for ( int i = 0; i < size; i++ ) {
                String name = (String) names.get(i);
                String textCode = toTextCode(name);
                textCodes.add(textCode);
            }
        }
        return textCodes;
    }
    
    protected String toIntCode(String name) {
        return "INDEX_" + toIdentifier(name);
    }
    
    protected String toTextCode(String name) {
        return "TEXT_" + toIdentifier(name);
    }
    
    protected String toIdentifier(String name) {
        String answer = name.toUpperCase();
        answer = answer.replace( ':', '_' );
        answer = answer.replace( '-', '_' );
        return answer;
    }
    
    protected String createComment() {
        return "Contains the String constants together with helper methods "
            + "for converting between text and integer representations";
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
 * $Id: StringConstantsGenerator.java,v 1.2 2001/02/01 23:32:46 jstrachan Exp $
 */
