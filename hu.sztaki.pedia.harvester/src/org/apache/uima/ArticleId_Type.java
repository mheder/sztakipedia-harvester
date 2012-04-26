
/* First created by JCasGen Thu Feb 02 10:37:48 CET 2012 */
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
public class ArticleId_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (ArticleId_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = ArticleId_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new ArticleId(addr, ArticleId_Type.this);
  			   ArticleId_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new ArticleId(addr, ArticleId_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = ArticleId.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.ArticleId");
 
  /** @generated */
  final Feature casFeat_Id;
  /** @generated */
  final int     casFeatCode_Id;
  /** @generated */ 
  public String getId(int addr) {
        if (featOkTst && casFeat_Id == null)
      jcas.throwFeatMissing("Id", "org.apache.uima.ArticleId");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Id);
  }
  /** @generated */    
  public void setId(int addr, String v) {
        if (featOkTst && casFeat_Id == null)
      jcas.throwFeatMissing("Id", "org.apache.uima.ArticleId");
    ll_cas.ll_setStringValue(addr, casFeatCode_Id, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public ArticleId_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Id = jcas.getRequiredFeatureDE(casType, "Id", "uima.cas.String", featOkTst);
    casFeatCode_Id  = (null == casFeat_Id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Id).getCode();

  }
}



    