

/* First created by JCasGen Tue Jun 05 10:31:31 CEST 2012 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jun 05 14:39:06 CEST 2012
 * XML source: /home/tfarkas/Dev/workspaces/workspace_uima/hu.sztaki.pedia.harvester/descriptors/typeSystemDescriptor.xml
 * @generated */
public class WikiLinkAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(WikiLinkAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected WikiLinkAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public WikiLinkAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public WikiLinkAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public WikiLinkAnnotation(JCas jcas, int begin, int end) {
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
    if (WikiLinkAnnotation_Type.featOkTst && ((WikiLinkAnnotation_Type)jcasType).casFeat_href == null)
      jcasType.jcas.throwFeatMissing("href", "org.apache.uima.WikiLinkAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WikiLinkAnnotation_Type)jcasType).casFeatCode_href);}
    
  /** setter for href - sets  
   * @generated */
  public void setHref(String v) {
    if (WikiLinkAnnotation_Type.featOkTst && ((WikiLinkAnnotation_Type)jcasType).casFeat_href == null)
      jcasType.jcas.throwFeatMissing("href", "org.apache.uima.WikiLinkAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WikiLinkAnnotation_Type)jcasType).casFeatCode_href, v);}    
   
    
  //*--------------*
  //* Feature: title

  /** getter for title - gets 
   * @generated */
  public String getTitle() {
    if (WikiLinkAnnotation_Type.featOkTst && ((WikiLinkAnnotation_Type)jcasType).casFeat_title == null)
      jcasType.jcas.throwFeatMissing("title", "org.apache.uima.WikiLinkAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WikiLinkAnnotation_Type)jcasType).casFeatCode_title);}
    
  /** setter for title - sets  
   * @generated */
  public void setTitle(String v) {
    if (WikiLinkAnnotation_Type.featOkTst && ((WikiLinkAnnotation_Type)jcasType).casFeat_title == null)
      jcasType.jcas.throwFeatMissing("title", "org.apache.uima.WikiLinkAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WikiLinkAnnotation_Type)jcasType).casFeatCode_title, v);}    
  }

    