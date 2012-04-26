

/* First created by JCasGen Wed Dec 07 11:17:11 CET 2011 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Feb 02 10:37:47 CET 2012
 * XML source: /home/tfarkas/Dev/workspaces/workspace_uima/hu.sztaki.pedia.uima/descriptors/typeSystemDescriptor.xml
 * @generated */
public class Link extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Link.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Link() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Link(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Link(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Link(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: href

  /** getter for href - gets 
   * @generated */
  public String getHref() {
    if (Link_Type.featOkTst && ((Link_Type)jcasType).casFeat_href == null)
      jcasType.jcas.throwFeatMissing("href", "org.apache.uima.Link");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Link_Type)jcasType).casFeatCode_href);}
    
  /** setter for href - sets  
   * @generated */
  public void setHref(String v) {
    if (Link_Type.featOkTst && ((Link_Type)jcasType).casFeat_href == null)
      jcasType.jcas.throwFeatMissing("href", "org.apache.uima.Link");
    jcasType.ll_cas.ll_setStringValue(addr, ((Link_Type)jcasType).casFeatCode_href, v);}    
  }

    