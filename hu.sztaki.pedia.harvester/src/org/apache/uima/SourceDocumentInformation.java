

/* First created by JCasGen Mon Nov 07 11:48:35 CET 2011 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jun 05 14:39:05 CEST 2012
 * XML source: /home/tfarkas/Dev/workspaces/workspace_uima/hu.sztaki.pedia.harvester/descriptors/typeSystemDescriptor.xml
 * @generated */
public class SourceDocumentInformation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(SourceDocumentInformation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected SourceDocumentInformation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public SourceDocumentInformation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public SourceDocumentInformation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public SourceDocumentInformation(JCas jcas, int begin, int end) {
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
  //* Feature: uri

  /** getter for uri - gets 
   * @generated */
  public String getUri() {
    if (SourceDocumentInformation_Type.featOkTst && ((SourceDocumentInformation_Type)jcasType).casFeat_uri == null)
      jcasType.jcas.throwFeatMissing("uri", "org.apache.uima.SourceDocumentInformation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SourceDocumentInformation_Type)jcasType).casFeatCode_uri);}
    
  /** setter for uri - sets  
   * @generated */
  public void setUri(String v) {
    if (SourceDocumentInformation_Type.featOkTst && ((SourceDocumentInformation_Type)jcasType).casFeat_uri == null)
      jcasType.jcas.throwFeatMissing("uri", "org.apache.uima.SourceDocumentInformation");
    jcasType.ll_cas.ll_setStringValue(addr, ((SourceDocumentInformation_Type)jcasType).casFeatCode_uri, v);}    
   
    
  //*--------------*
  //* Feature: offsetInSource

  /** getter for offsetInSource - gets 
   * @generated */
  public int getOffsetInSource() {
    if (SourceDocumentInformation_Type.featOkTst && ((SourceDocumentInformation_Type)jcasType).casFeat_offsetInSource == null)
      jcasType.jcas.throwFeatMissing("offsetInSource", "org.apache.uima.SourceDocumentInformation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((SourceDocumentInformation_Type)jcasType).casFeatCode_offsetInSource);}
    
  /** setter for offsetInSource - sets  
   * @generated */
  public void setOffsetInSource(int v) {
    if (SourceDocumentInformation_Type.featOkTst && ((SourceDocumentInformation_Type)jcasType).casFeat_offsetInSource == null)
      jcasType.jcas.throwFeatMissing("offsetInSource", "org.apache.uima.SourceDocumentInformation");
    jcasType.ll_cas.ll_setIntValue(addr, ((SourceDocumentInformation_Type)jcasType).casFeatCode_offsetInSource, v);}    
   
    
  //*--------------*
  //* Feature: lastSegment

  /** getter for lastSegment - gets 
   * @generated */
  public boolean getLastSegment() {
    if (SourceDocumentInformation_Type.featOkTst && ((SourceDocumentInformation_Type)jcasType).casFeat_lastSegment == null)
      jcasType.jcas.throwFeatMissing("lastSegment", "org.apache.uima.SourceDocumentInformation");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((SourceDocumentInformation_Type)jcasType).casFeatCode_lastSegment);}
    
  /** setter for lastSegment - sets  
   * @generated */
  public void setLastSegment(boolean v) {
    if (SourceDocumentInformation_Type.featOkTst && ((SourceDocumentInformation_Type)jcasType).casFeat_lastSegment == null)
      jcasType.jcas.throwFeatMissing("lastSegment", "org.apache.uima.SourceDocumentInformation");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((SourceDocumentInformation_Type)jcasType).casFeatCode_lastSegment, v);}    
   
    
  //*--------------*
  //* Feature: documentSize

  /** getter for documentSize - gets 
   * @generated */
  public int getDocumentSize() {
    if (SourceDocumentInformation_Type.featOkTst && ((SourceDocumentInformation_Type)jcasType).casFeat_documentSize == null)
      jcasType.jcas.throwFeatMissing("documentSize", "org.apache.uima.SourceDocumentInformation");
    return jcasType.ll_cas.ll_getIntValue(addr, ((SourceDocumentInformation_Type)jcasType).casFeatCode_documentSize);}
    
  /** setter for documentSize - sets  
   * @generated */
  public void setDocumentSize(int v) {
    if (SourceDocumentInformation_Type.featOkTst && ((SourceDocumentInformation_Type)jcasType).casFeat_documentSize == null)
      jcasType.jcas.throwFeatMissing("documentSize", "org.apache.uima.SourceDocumentInformation");
    jcasType.ll_cas.ll_setIntValue(addr, ((SourceDocumentInformation_Type)jcasType).casFeatCode_documentSize, v);}    
   
    
  //*--------------*
  //* Feature: lang

  /** getter for lang - gets 
   * @generated */
  public String getLang() {
    if (SourceDocumentInformation_Type.featOkTst && ((SourceDocumentInformation_Type)jcasType).casFeat_lang == null)
      jcasType.jcas.throwFeatMissing("lang", "org.apache.uima.SourceDocumentInformation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SourceDocumentInformation_Type)jcasType).casFeatCode_lang);}
    
  /** setter for lang - sets  
   * @generated */
  public void setLang(String v) {
    if (SourceDocumentInformation_Type.featOkTst && ((SourceDocumentInformation_Type)jcasType).casFeat_lang == null)
      jcasType.jcas.throwFeatMissing("lang", "org.apache.uima.SourceDocumentInformation");
    jcasType.ll_cas.ll_setStringValue(addr, ((SourceDocumentInformation_Type)jcasType).casFeatCode_lang, v);}    
  }

    