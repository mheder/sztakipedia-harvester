

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
public class WikiTemplateAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(WikiTemplateAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected WikiTemplateAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public WikiTemplateAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public WikiTemplateAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public WikiTemplateAnnotation(JCas jcas, int begin, int end) {
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
    if (WikiTemplateAnnotation_Type.featOkTst && ((WikiTemplateAnnotation_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.WikiTemplateAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WikiTemplateAnnotation_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated */
  public void setName(String v) {
    if (WikiTemplateAnnotation_Type.featOkTst && ((WikiTemplateAnnotation_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.WikiTemplateAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WikiTemplateAnnotation_Type)jcasType).casFeatCode_name, v);}    
  }

    