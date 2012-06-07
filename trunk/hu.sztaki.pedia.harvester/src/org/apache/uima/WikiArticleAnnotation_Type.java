
/* First created by JCasGen Tue Jun 05 10:31:31 CEST 2012 */
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

/** 
 * Updated by JCasGen Tue Jun 05 14:39:06 CEST 2012
 * @generated */
public class WikiArticleAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (WikiArticleAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = WikiArticleAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new WikiArticleAnnotation(addr, WikiArticleAnnotation_Type.this);
  			   WikiArticleAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new WikiArticleAnnotation(addr, WikiArticleAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = WikiArticleAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.WikiArticleAnnotation");
 
  /** @generated */
  final Feature casFeat_id;
  /** @generated */
  final int     casFeatCode_id;
  /** @generated */ 
  public long getId(int addr) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "org.apache.uima.WikiArticleAnnotation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_id);
  }
  /** @generated */    
  public void setId(int addr, long v) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "org.apache.uima.WikiArticleAnnotation");
    ll_cas.ll_setLongValue(addr, casFeatCode_id, v);}
    
  
 
  /** @generated */
  final Feature casFeat_application;
  /** @generated */
  final int     casFeatCode_application;
  /** @generated */ 
  public String getApplication(int addr) {
        if (featOkTst && casFeat_application == null)
      jcas.throwFeatMissing("application", "org.apache.uima.WikiArticleAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_application);
  }
  /** @generated */    
  public void setApplication(int addr, String v) {
        if (featOkTst && casFeat_application == null)
      jcas.throwFeatMissing("application", "org.apache.uima.WikiArticleAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_application, v);}
    
  
 
  /** @generated */
  final Feature casFeat_lang;
  /** @generated */
  final int     casFeatCode_lang;
  /** @generated */ 
  public String getLang(int addr) {
        if (featOkTst && casFeat_lang == null)
      jcas.throwFeatMissing("lang", "org.apache.uima.WikiArticleAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_lang);
  }
  /** @generated */    
  public void setLang(int addr, String v) {
        if (featOkTst && casFeat_lang == null)
      jcas.throwFeatMissing("lang", "org.apache.uima.WikiArticleAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_lang, v);}
    
  
 
  /** @generated */
  final Feature casFeat_title;
  /** @generated */
  final int     casFeatCode_title;
  /** @generated */ 
  public String getTitle(int addr) {
        if (featOkTst && casFeat_title == null)
      jcas.throwFeatMissing("title", "org.apache.uima.WikiArticleAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_title);
  }
  /** @generated */    
  public void setTitle(int addr, String v) {
        if (featOkTst && casFeat_title == null)
      jcas.throwFeatMissing("title", "org.apache.uima.WikiArticleAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_title, v);}
    
  
 
  /** @generated */
  final Feature casFeat_revision;
  /** @generated */
  final int     casFeatCode_revision;
  /** @generated */ 
  public long getRevision(int addr) {
        if (featOkTst && casFeat_revision == null)
      jcas.throwFeatMissing("revision", "org.apache.uima.WikiArticleAnnotation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_revision);
  }
  /** @generated */    
  public void setRevision(int addr, long v) {
        if (featOkTst && casFeat_revision == null)
      jcas.throwFeatMissing("revision", "org.apache.uima.WikiArticleAnnotation");
    ll_cas.ll_setLongValue(addr, casFeatCode_revision, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public WikiArticleAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_id = jcas.getRequiredFeatureDE(casType, "id", "uima.cas.Long", featOkTst);
    casFeatCode_id  = (null == casFeat_id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_id).getCode();

 
    casFeat_application = jcas.getRequiredFeatureDE(casType, "application", "uima.cas.String", featOkTst);
    casFeatCode_application  = (null == casFeat_application) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_application).getCode();

 
    casFeat_lang = jcas.getRequiredFeatureDE(casType, "lang", "uima.cas.String", featOkTst);
    casFeatCode_lang  = (null == casFeat_lang) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_lang).getCode();

 
    casFeat_title = jcas.getRequiredFeatureDE(casType, "title", "uima.cas.String", featOkTst);
    casFeatCode_title  = (null == casFeat_title) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_title).getCode();

 
    casFeat_revision = jcas.getRequiredFeatureDE(casType, "revision", "uima.cas.Long", featOkTst);
    casFeatCode_revision  = (null == casFeat_revision) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_revision).getCode();

  }
}



    