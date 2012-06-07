

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
public class WikiArticleAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(WikiArticleAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected WikiArticleAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public WikiArticleAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public WikiArticleAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public WikiArticleAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: id

  /** getter for id - gets 
   * @generated */
  public long getId() {
    if (WikiArticleAnnotation_Type.featOkTst && ((WikiArticleAnnotation_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "org.apache.uima.WikiArticleAnnotation");
    return jcasType.ll_cas.ll_getLongValue(addr, ((WikiArticleAnnotation_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated */
  public void setId(long v) {
    if (WikiArticleAnnotation_Type.featOkTst && ((WikiArticleAnnotation_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "org.apache.uima.WikiArticleAnnotation");
    jcasType.ll_cas.ll_setLongValue(addr, ((WikiArticleAnnotation_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: application

  /** getter for application - gets 
   * @generated */
  public String getApplication() {
    if (WikiArticleAnnotation_Type.featOkTst && ((WikiArticleAnnotation_Type)jcasType).casFeat_application == null)
      jcasType.jcas.throwFeatMissing("application", "org.apache.uima.WikiArticleAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WikiArticleAnnotation_Type)jcasType).casFeatCode_application);}
    
  /** setter for application - sets  
   * @generated */
  public void setApplication(String v) {
    if (WikiArticleAnnotation_Type.featOkTst && ((WikiArticleAnnotation_Type)jcasType).casFeat_application == null)
      jcasType.jcas.throwFeatMissing("application", "org.apache.uima.WikiArticleAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WikiArticleAnnotation_Type)jcasType).casFeatCode_application, v);}    
   
    
  //*--------------*
  //* Feature: lang

  /** getter for lang - gets 
   * @generated */
  public String getLang() {
    if (WikiArticleAnnotation_Type.featOkTst && ((WikiArticleAnnotation_Type)jcasType).casFeat_lang == null)
      jcasType.jcas.throwFeatMissing("lang", "org.apache.uima.WikiArticleAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WikiArticleAnnotation_Type)jcasType).casFeatCode_lang);}
    
  /** setter for lang - sets  
   * @generated */
  public void setLang(String v) {
    if (WikiArticleAnnotation_Type.featOkTst && ((WikiArticleAnnotation_Type)jcasType).casFeat_lang == null)
      jcasType.jcas.throwFeatMissing("lang", "org.apache.uima.WikiArticleAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WikiArticleAnnotation_Type)jcasType).casFeatCode_lang, v);}    
   
    
  //*--------------*
  //* Feature: title

  /** getter for title - gets 
   * @generated */
  public String getTitle() {
    if (WikiArticleAnnotation_Type.featOkTst && ((WikiArticleAnnotation_Type)jcasType).casFeat_title == null)
      jcasType.jcas.throwFeatMissing("title", "org.apache.uima.WikiArticleAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((WikiArticleAnnotation_Type)jcasType).casFeatCode_title);}
    
  /** setter for title - sets  
   * @generated */
  public void setTitle(String v) {
    if (WikiArticleAnnotation_Type.featOkTst && ((WikiArticleAnnotation_Type)jcasType).casFeat_title == null)
      jcasType.jcas.throwFeatMissing("title", "org.apache.uima.WikiArticleAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((WikiArticleAnnotation_Type)jcasType).casFeatCode_title, v);}    
   
    
  //*--------------*
  //* Feature: revision

  /** getter for revision - gets 
   * @generated */
  public long getRevision() {
    if (WikiArticleAnnotation_Type.featOkTst && ((WikiArticleAnnotation_Type)jcasType).casFeat_revision == null)
      jcasType.jcas.throwFeatMissing("revision", "org.apache.uima.WikiArticleAnnotation");
    return jcasType.ll_cas.ll_getLongValue(addr, ((WikiArticleAnnotation_Type)jcasType).casFeatCode_revision);}
    
  /** setter for revision - sets  
   * @generated */
  public void setRevision(long v) {
    if (WikiArticleAnnotation_Type.featOkTst && ((WikiArticleAnnotation_Type)jcasType).casFeat_revision == null)
      jcasType.jcas.throwFeatMissing("revision", "org.apache.uima.WikiArticleAnnotation");
    jcasType.ll_cas.ll_setLongValue(addr, ((WikiArticleAnnotation_Type)jcasType).casFeatCode_revision, v);}    
  }

    