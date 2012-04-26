

/* First created by JCasGen Thu Feb 02 10:37:48 CET 2012 */
package org.apache.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Feb 02 10:37:48 CET 2012
 * XML source: /home/tfarkas/Dev/workspaces/workspace_uima/hu.sztaki.pedia.uima/descriptors/typeSystemDescriptor.xml
 * @generated */
public class ArticleId extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(ArticleId.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected ArticleId() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public ArticleId(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public ArticleId(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public ArticleId(JCas jcas, int begin, int end) {
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
  //* Feature: Id

  /** getter for Id - gets 
   * @generated */
  public String getId() {
    if (ArticleId_Type.featOkTst && ((ArticleId_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "org.apache.uima.ArticleId");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ArticleId_Type)jcasType).casFeatCode_Id);}
    
  /** setter for Id - sets  
   * @generated */
  public void setId(String v) {
    if (ArticleId_Type.featOkTst && ((ArticleId_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "org.apache.uima.ArticleId");
    jcasType.ll_cas.ll_setStringValue(addr, ((ArticleId_Type)jcasType).casFeatCode_Id, v);}    
  }

    