
/* First created by JCasGen Mon Nov 07 11:44:19 CET 2011 */
package org.apache.uima;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** sentence annotation
 * Updated by JCasGen Tue Jun 05 14:39:05 CEST 2012
 * @generated */
public class SentenceAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (SentenceAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = SentenceAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SentenceAnnotation(addr, SentenceAnnotation_Type.this);
  			   SentenceAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SentenceAnnotation(addr, SentenceAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = SentenceAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.SentenceAnnotation");
 
  /** @generated */
  final Feature casFeat_lang;
  /** @generated */
  final int     casFeatCode_lang;
  /** @generated */ 
  public String getLang(int addr) {
        if (featOkTst && casFeat_lang == null)
      jcas.throwFeatMissing("lang", "org.apache.uima.SentenceAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_lang);
  }
  /** @generated */    
  public void setLang(int addr, String v) {
        if (featOkTst && casFeat_lang == null)
      jcas.throwFeatMissing("lang", "org.apache.uima.SentenceAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_lang, v);}
    
  
 
  /** @generated */
  final Feature casFeat_hash;
  /** @generated */
  final int     casFeatCode_hash;
  /** @generated */ 
  public String getHash(int addr) {
        if (featOkTst && casFeat_hash == null)
      jcas.throwFeatMissing("hash", "org.apache.uima.SentenceAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_hash);
  }
  /** @generated */    
  public void setHash(int addr, String v) {
        if (featOkTst && casFeat_hash == null)
      jcas.throwFeatMissing("hash", "org.apache.uima.SentenceAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_hash, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public SentenceAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_lang = jcas.getRequiredFeatureDE(casType, "lang", "uima.cas.String", featOkTst);
    casFeatCode_lang  = (null == casFeat_lang) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_lang).getCode();

 
    casFeat_hash = jcas.getRequiredFeatureDE(casType, "hash", "uima.cas.String", featOkTst);
    casFeatCode_hash  = (null == casFeat_hash) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_hash).getCode();

  }
}



    