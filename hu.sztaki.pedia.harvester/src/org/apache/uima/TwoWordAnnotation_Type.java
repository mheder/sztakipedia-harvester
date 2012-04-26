
/* First created by JCasGen Mon Nov 07 11:55:58 CET 2011 */
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
 * Updated by JCasGen Thu Feb 02 10:37:48 CET 2012
 * @generated */
public class TwoWordAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TwoWordAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TwoWordAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TwoWordAnnotation(addr, TwoWordAnnotation_Type.this);
  			   TwoWordAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TwoWordAnnotation(addr, TwoWordAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TwoWordAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.TwoWordAnnotation");



  /** @generated */
  final Feature casFeat_rating;
  /** @generated */
  final int     casFeatCode_rating;
  /** @generated */ 
  public double getRating(int addr) {
        if (featOkTst && casFeat_rating == null)
      jcas.throwFeatMissing("rating", "org.apache.uima.TwoWordAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_rating);
  }
  /** @generated */    
  public void setRating(int addr, double v) {
        if (featOkTst && casFeat_rating == null)
      jcas.throwFeatMissing("rating", "org.apache.uima.TwoWordAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_rating, v);}
    
  
 
  /** @generated */
  final Feature casFeat_words;
  /** @generated */
  final int     casFeatCode_words;
  /** @generated */ 
  public int getWords(int addr) {
        if (featOkTst && casFeat_words == null)
      jcas.throwFeatMissing("words", "org.apache.uima.TwoWordAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_words);
  }
  /** @generated */    
  public void setWords(int addr, int v) {
        if (featOkTst && casFeat_words == null)
      jcas.throwFeatMissing("words", "org.apache.uima.TwoWordAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_words, v);}
    
  
 
  /** @generated */
  final Feature casFeat_idf;
  /** @generated */
  final int     casFeatCode_idf;
  /** @generated */ 
  public double getIdf(int addr) {
        if (featOkTst && casFeat_idf == null)
      jcas.throwFeatMissing("idf", "org.apache.uima.TwoWordAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_idf);
  }
  /** @generated */    
  public void setIdf(int addr, double v) {
        if (featOkTst && casFeat_idf == null)
      jcas.throwFeatMissing("idf", "org.apache.uima.TwoWordAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_idf, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public TwoWordAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_rating = jcas.getRequiredFeatureDE(casType, "rating", "uima.cas.Double", featOkTst);
    casFeatCode_rating  = (null == casFeat_rating) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rating).getCode();

 
    casFeat_words = jcas.getRequiredFeatureDE(casType, "words", "uima.cas.StringList", featOkTst);
    casFeatCode_words  = (null == casFeat_words) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_words).getCode();

 
    casFeat_idf = jcas.getRequiredFeatureDE(casType, "idf", "uima.cas.Double", featOkTst);
    casFeatCode_idf  = (null == casFeat_idf) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_idf).getCode();

  }
}



    