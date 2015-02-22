package factorie

import java.io.File

import cc.factorie.app.strings.{Stopwords, alphaSegmenter}
import cc.factorie.app.topics.lda.TopicsOverTime.WordDomain
import cc.factorie.directed.DirectedModel
import cc.factorie.variable._
import cc.factorie.directed._

import scala.collection.mutable.ArrayBuffer

/**
 * Simple LDA implementation for experimentation and learning Factorie,
 * follows a tutorial closely
 *
 * Created by bugra on 11/13/14.
 */
object SimpleLDA {

  val numTopics = 10
  implicit val model = DirectedModel()

  object ZDomain extends DiscreteDomain(numTopics)

  object ZSeqDomain extends DiscreteSeqDomain {
    def elementDomain = ZDomain
  }

  class Zs(len: Int) extends DiscreteSeqVariable(len) {
    def domain = ZSeqDomain
  }

  object WordSeqDomain extends CategoricalSeqDomain[String]

  class Words(strings: Seq[String]) extends CategoricalSeqVariable(strings) {
    def domain = WordSeqDomain
    def zs = model.parentFactor(this).asInstanceOf[PlatedCategoricalMixture.Factor]._3
  }

  class Document(val file: String, val theta: ProportionsVar, strings: Seq[String]) extends Words(strings)
  val beta = MassesVariable.growableUniform(WordDomain, 0.1)
  val alphas = MassesVariable.dense(numTopics, 0.1)


  def main(args: Array[String]): Unit = {
    implicit val random = new scala.util.Random(0)
    val directories = if (args.length > 0) args.toList else List("12", "11", "10", "09", "08").take(1)
    val phis = Mixture(numTopics)(ProportionsVariable.growableDense(WordDomain) ~ Dirichlet(beta))
    val documents = new ArrayBuffer[Document]
    for (directory <- directories) {
      for (file <- new File(directory).listFiles; if file.isFile) {
        val theta = ProportionsVariable.dense(numTopics) ~ Dirichlet(alphas)
        val tokens = alphaSegmenter(file).map(_.toLowerCase).filter(!Stopwords.contains(_)).toSeq
        val zs = new Zs(tokens.length) :~ PlatedDiscrete(theta)
        documents += new Document(file.toString, theta, tokens) ~ PlatedCategoricalMixture(phis, zs)
      }
    }

    val collapse = new ArrayBuffer[Var]
    collapse += phis
    collapse ++= documents.map(_.theta)
    val sampler = new CollapsedGibbsSampler(collapse, model)

    for (i <- 1 to 20){
      for (doc <- documents) {
        sampler.process(doc)
      }
    }
  }
}

