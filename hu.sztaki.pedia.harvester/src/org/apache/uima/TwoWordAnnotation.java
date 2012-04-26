

/* First created by JCasGen Mon Nov 07 11:55:58 CET 2011 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Feb 02 10:37:48 CET 2012
 * XML source: /home/tfarkas/Dev/workspaces/workspace_uima/hu.sztaki.pedia.uima/descriptors/typeSystemDescriptor.xml
 * @generated */
public class TwoWordAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TwoWordAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TwoWordAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TwoWordAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TwoWordAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TwoWordAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: rating

  /** getter for rating - gets 
   * @generated */
  public double getRating() {
    if (TwoWordAnnotation_Type.featOkTst && ((TwoWordAnnotation_Type)jcasType).casFeat_rating == null)
      jcasType.jcas.throwFeatMissing("rating", "org.apache.uima.TwoWordAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((TwoWordAnnotation_Type)jcasType).casFeatCode_rating);}
    
  /** setter for rating - sets  
   * @generated */
  public void setRating(double v) {
    if (TwoWordAnnotation_Type.featOkTst && ((TwoWordAnnotation_Type)jcasType).casFeat_rating == null)
      jcasType.jcas.throwFeatMissing("rating", "org.apache.uima.TwoWordAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((TwoWordAnnotation_Type)jcasType).casFeatCode_rating, v);}    
   
    
  //*--------------*
  //* Feature: words

  /** getter for words - gets 
   * @generated */
  public StringList getWords() {
    if (TwoWordAnnotation_Type.featOkTst && ((TwoWordAnnotation_Type)jcasType).casFeat_words == null)
      jcasType.jcas.throwFeatMissing("words", "org.apache.uima.TwoWordAnnotation");
    return (StringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TwoWordAnnotation_Type)jcasType).casFeatCode_words)));}
    
  /** setter for words - sets  
   * @generated */
  public void setWords(StringList v) {
    if (TwoWordAnnotation_Type.featOkTst && ((TwoWordAnnotation_Type)jcasType).casFeat_words == null)
      jcasType.jcas.throwFeatMissing("words", "org.apache.uima.TwoWordAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((TwoWordAnnotation_Type)jcasType).casFeatCode_words, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: idf

  /** getter for idf - gets 
   * @generated */
  public double getIdf() {
    if (TwoWordAnnotation_Type.featOkTst && ((TwoWordAnnotation_Type)jcasType).casFeat_idf == null)
      jcasType.jcas.throwFeatMissing("idf", "org.apache.uima.TwoWordAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((TwoWordAnnotation_Type)jcasType).casFeatCode_idf);}
    
  /** setter for idf - sets  
   * @generated */
  public void setIdf(double v) {
    if (TwoWordAnnotation_Type.featOkTst && ((TwoWordAnnotation_Type)jcasType).casFeat_idf == null)
      jcasType.jcas.throwFeatMissing("idf", "org.apache.uima.TwoWordAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((TwoWordAnnotation_Type)jcasType).casFeatCode_idf, v);}    
  }

    