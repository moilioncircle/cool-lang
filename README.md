# cool-lang
An interpreter of Classroom Object Oriented Language(Stanford cs143) written by Java8

## Requirements
```
    $java -version
    $java version "1.8.0_20"

    $mvn -version
    $Apache Maven 3.2.3

```

## Usage
```
    $cd cool-lang/
    $mvn clean package
    $cd target
    $java -jar cool-lang-1.0-SNAPSHOT-jar-with-dependencies.jar ../src/test/resources/hello.cl
```

`output:`

```
    Hello, world! chen bao yi
```

## References

  * [The Cool Reference Manual](https://spark-university.s3.amazonaws.com/stanford-compilers/resources%2Fcool_manual.pdf)
  * [Video Lectures](https://class.coursera.org/compilers-004/lecture)
  * [The shunting yard algorithm](http://www.engr.mun.ca/~theo/Misc/exp_parsing.htm)
  * [Java Tokenizer,Parser](http://hg.openjdk.java.net/jdk8/jdk8/langtools/)



