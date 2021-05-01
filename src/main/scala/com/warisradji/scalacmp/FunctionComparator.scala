package com.warisradji.scalacmp

import breeze.{plot => bplot}

import scala.collection.immutable.Queue

/**
 *
 * @param fs List of functions to compare
 * @param start The initial value of the parameter pass to the functions
 * @param incrementer Function to change the state of the parameter
 * @param warmups Number of warmups to avoid pitfalls on the JVM
 */
case class FunctionComparator[P](
                                  fs: Seq[P => _],
                                  start: P,
                                  incrementer: P => P,
                                  warmups: Int = 10
                                ) {

  private def elapsedTime[R](block: => R): Long = {
    val start = System.nanoTime()
    block
    System.nanoTime() - start
  }


  private def computeTimes(n: Int): Queue[Seq[Double]] = {
    (0 until (n + warmups)).
      foldLeft(
        (start, Queue.empty[Seq[Double]])
      )((acc, _) => (incrementer(acc._1), acc._2 :+ fs.map(f => elapsedTime({f(acc._1)}).toDouble))
      )._2.drop(warmups)
  }

  /** Build the graph of time comparisons
   *
   * @param n Number of parameters incrementation
   * @param labels Labels of the lines in the plot
   * @param smoothValue Intensity of the FIR filter to smooth the generated curves
   * @param outputFile Path of the file to save the plot
   */
  def plot(n: Int, labels: Seq[String], smoothValue: Int = 100, outputFile: Option[String] = None): Unit = {

    def firFilter(xs: Seq[Double], b: Seq[Double]): Seq[Double] =
      xs.indices.map(n => b.indices.map(i => b(i) * (if ((n - i) < 0) 0 else xs(n -i))).sum)

    val data = computeTimes(n).transpose

    val filteredData = data.map(x => firFilter(x.toVector, Vector.fill(smoothValue)(1D / smoothValue)))

    val f = bplot.Figure()
    val p = f.subplot(0)

    val x = (0 until n).map(_.toDouble)
    fs.indices.map(i => p += bplot.plot(x, filteredData(i), name = labels(i)))
    p.xlabel = "n"
    p.ylabel = "Time execution in ns"
    p.legend = true
    if (outputFile.isDefined)
      f.saveas(outputFile.get)
  }
}
