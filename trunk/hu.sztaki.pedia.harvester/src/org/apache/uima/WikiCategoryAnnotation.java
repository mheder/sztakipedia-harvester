

/* First created by JCasGen Tue Jun 05 10:31:31 CEST 2012 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Jun 21 10:37:39 CEST 2012
 * XML source: /home/tfarkas/Dev/workspaces/workspace_uima/hu.sztaki.pedia.harvester/descriptors/typeSystemDescriptor.xml
 * @generated */
public class WikiCategoryAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(WikiCategoryAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected WikiCategoryAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public WikiCategoryAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public WikiCategoryAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public WikiCategoryAnnotation(JCas jcas, int begin, int end) {
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
    if (WikiCategoryAnnotation_Type.featOkTst && ((WikiCategoryAnnotation_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.WikiCategoryAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WikiCategoryAnnotation_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated */
  public void setName(String v) {
    if (WikiCategoryAnnotation_Type.featOkTst && ((WikiCategoryAnnotation_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "org.apache.uima.WikiCategoryAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WikiCategoryAnnotation_Type)jcasType).casFeatCode_name, v);}    
  }

    