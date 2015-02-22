package factorie

import cc.factorie.app.topics.lda.{Document, LDA}
import cc.factorie.directed.DirectedModel
import cc.factorie.variable.CategoricalSeqDomain


object RunLDA {
  val ID_RUN = 1
  val random = new scala.util.Random(0)
  val numTopics = 30
  val alpha = numTopics.toDouble / numTopics
  val beta = 0.01
  val numIterations = 5000
  val diagnosticInterval = 100
  val diagnosticPhrases = true
  val tokenRegex = "\\p{Alpha}+"
  val optimizeBurnIn = 100
  val printTopics = numTopics - 1


  val htmlTags = "a,abbr,acronym,address,amp,area,b,base,bdo,big,blockquote,body,br,button,caption,cite,code,col,colgroup,dd,del,dfn,div,dl,DOCTYPE,dt,em,fieldset,form,h1, h2, h3, h4, h5, h6, head,html,hr,i,img,input,ins,kbd,label,legend,li,link,map,meta,noscript,nbsp,object,ol,optgroup,option,p,param,pre,q,samp,script,select,small,span,strong,style,sub,sup,table,tbody,td,textarea,tfoot,th,thead,title,tr,tt,ul,var".split(",").map(x => x.toLowerCase.trim)

  object WordSeqDomain extends CategoricalSeqDomain[String]

  val model = DirectedModel()
  println(model)
  val lda = new LDA(WordSeqDomain, numTopics, alpha, beta, optimizeBurnIn)(model, random)
  //val recursiveLda = new RecursiveLDA(new CategoricalSeqDomain[String], numTopics, alpha, beta)(model, random)
  //println(recursiveLda)
  println(lda)

  val alphaSegmenter = new cc.factorie.app.strings.RegexSegmenter(tokenRegex.r)
  val stopWords = new cc.factorie.app.strings.Stopwords()
  for (htmlTag <- htmlTags) {
    stopWords += htmlTag
  }

  /*
   Add documents from an List to LDA model
   Returns Unit
  */
  def addDocuments(documentList: List[String]) = {
    for ((document, ii) <- documentList.view.zipWithIndex) {
      val doc = Document.fromString(WordSeqDomain,
                                    ii.toString,
                                    document,
                                    segmenter = alphaSegmenter,
                                    stopwords = stopWords,
                                    wordCountMax = Int.MaxValue)
      lda.addDocument(doc, random)
    }
  }

  def runInference : LDA =  {
    lda.inferTopics(numIterations,
                    fitAlphaInterval = Int.MaxValue,
                    diagnosticInterval = diagnosticInterval,
                    diagnosticShowPhrases = diagnosticPhrases
                   )
    lda
  }

  def main(args: Array[String]) {


      val conn = DB.getConnection
      val statement = conn.createStatement
      try {


      } catch {
          case e @ (_: RuntimeException | _: IllegalArgumentException) => println(e)
      }
  }
 }


