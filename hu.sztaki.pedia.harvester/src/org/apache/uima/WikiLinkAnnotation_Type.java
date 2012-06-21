
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
 * Updated by JCasGen Thu Jun 21 10:37:39 CEST 2012
 * @generated */
public class WikiLinkAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (WikiLinkAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = WikiLinkAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new WikiLinkAnnotation(addr, WikiLinkAnnotation_Type.this);
  			   WikiLinkAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new WikiLinkAnnotation(addr, WikiLinkAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = WikiLinkAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.WikiLinkAnnotation");
 
  /** @generated */
  final Feature casFeat_href;
  /** @generated */
  final int     casFeatCode_href;
  /** @generated */ 
  public String getHref(int addr) {
        if (featOkTst && casFeat_href == null)
      jcas.throwFeatMissing("href", "org.apache.uima.WikiLinkAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_href);
  }
  /** @generated */    
  public void setHref(int addr, String v) {
        if (featOkTst && casFeat_href == null)
      jcas.throwFeatMissing("href", "org.apache.uima.WikiLinkAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_href, v);}
    
  
 
  /** @generated */
  final Feature casFeat_title;
  /** @generated */
  final int     casFeatCode_title;
  /** @generated */ 
  public String getTitle(int addr) {
        if (featOkTst && casFeat_title == null)
      jcas.throwFeatMissing("title", "org.apache.uima.WikiLinkAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_title);
  }
  /** @generated */    
  public void setTitle(int addr, String v) {
        if (featOkTst && casFeat_title == null)
      jcas.throwFeatMissing("title", "org.apache.uima.WikiLinkAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_title, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public WikiLinkAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_href = jcas.getRequiredFeatureDE(casType, "href", "uima.cas.String", featOkTst);
    casFeatCode_href  = (null == casFeat_href) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_href).getCode();

 
    casFeat_title = jcas.getRequiredFeatureDE(casType, "title", "uima.cas.String", featOkTst);
    casFeatCode_title  = (null == casFeat_title) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_title).getCode();

  }
}



    