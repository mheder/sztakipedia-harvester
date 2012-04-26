

/* First created by JCasGen Mon Dec 05 20:32:06 CET 2011 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Feb 02 10:37:48 CET 2012
 * XML source: /home/tfarkas/Dev/workspaces/workspace_uima/hu.sztaki.pedia.uima/descriptors/typeSystemDescriptor.xml
 * @generated */
public class WikiTemplate extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(WikiTemplate.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected WikiTemplate() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public WikiTemplate(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public WikiTemplate(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public WikiTemplate(JCas jcas, int begin, int end) {
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
  //* Feature: name

  /** getter for name - gets 
   * @generated */
  public String getName() {
    if (WikiTemplate_Type.featOkTst && ((WikiTemplate_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.WikiTemplate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WikiTemplate_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated */
  public void setName(String v) {
    if (WikiTemplate_Type.featOkTst && ((WikiTemplate_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.WikiTemplate");
    jcasType.ll_cas.ll_setStringValue(addr, ((WikiTemplate_Type)jcasType).casFeatCode_name, v);}    
  }

    