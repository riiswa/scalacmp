# Scalacmp

A light tool to visualize and compare the execution time of Scala functions.

## Installation

Clone this repo, and run `sbt publishLocal` to install it locally.

Then add the dependancies to your SBT project (e.g. `libraryDependencies += "com.warisradji" %% "scalacmp" % "0.1"` in build.sbt)

## Usage 

```scala
import com.warisradji.scalacmp.FunctionComparator

object Test extends App {
  def naiveExponentiation(x: Long, n: Long): Long = {
    var res = 1L
    var myN = n
    while(myN > 0) {
      res = res * x
      myN = myN - 1
    }
    res
  }

  def binaryExponentiation(x: Long, n: Long): Long = {
    if (n == 0)
      1
    else {
      val tmp = binaryExponentiation(x, n/2)
      if (n % 2 == 0)
        tmp * tmp
      else
        tmp * tmp * x
    }
  }

  FunctionComparator(
    List((binaryExponentiation _).tupled, (naiveExponentiation _).tupled), 
    (10L, 1L),
    (p: (Long, Long)) => (p._1, p._2 + 100), 50
  ).plot(500, List("binaryExponentiation", "naiveExponentiation"), 500, Some("example.png"))
}
```

Here is the output: 

![alt text](example.png "Output")

## Contributing

Pull requests are welcome. This is a small tool that I use personally, but feel free to improve it or add features.

## License

[MIT](https://choosealicense.com/licenses/mit/)