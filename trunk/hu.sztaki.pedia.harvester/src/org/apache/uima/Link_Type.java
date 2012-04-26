
/* First created by JCasGen Wed Dec 07 11:17:11 CET 2011 */
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
public class Link_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Link_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Link_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Link(addr, Link_Type.this);
  			   Link_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Link(addr, Link_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = Link.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.Link");
 
  /** @generated */
  final Feature casFeat_href;
  /** @generated */
  final int     casFeatCode_href;
  /** @generated */ 
  public String getHref(int addr) {
        if (featOkTst && casFeat_href == null)
      jcas.throwFeatMissing("href", "org.apache.uima.Link");
    return ll_cas.ll_getStringValue(addr, casFeatCode_href);
  }
  /** @generated */    
  public void setHref(int addr, String v) {
        if (featOkTst && casFeat_href == null)
      jcas.throwFeatMissing("href", "org.apache.uima.Link");
    ll_cas.ll_setStringValue(addr, casFeatCode_href, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Link_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_href = jcas.getRequiredFeatureDE(casType, "href", "uima.cas.String", featOkTst);
    casFeatCode_href  = (null == casFeat_href) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_href).getCode();

  }
}



    