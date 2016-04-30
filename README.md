# Parrot

Parrot is a property-files abstraction layer that aims to simplify the usage of multiple property files.

It's very simple - Parrot loads every .properties file from the classpath and from the working directory and makes them available through one simple method.

So now instead of worrying about loading each properties file and extracting the appropriate properties, you just do...

```
// At this point Parrot will find and load all .properties files in classpath and root directory
Parrot.init();

// Parrot will look for the "optionalParameter" in the map of properties it loaded from the files and return an optional
Optional<String> optionalParameter = Parrot.get("optionalParameter");

// Parrot will return a copy of all the parameters it found in the properties files
Map<String, String> allParameters = Parrot.all(); 
```

... and you're done. Just put your .properties files either in resources or at the root directory and use the parameters freely.

# How to get it?

### Option 1

`git clone` -> `mvn install`
 
### Option 2

Since it is not yet available on Maven central, you'll need to add my private github-based repository to your repositories.

```
<repositories>
  (...)
  <repository>
    <id>git-rskupnik</id>
    <name>rskupnik's Git based repo</name>
    <url>https://github.com/rskupnik/maven-repo/raw/master/</url>
  </repository>
  (...)
</repositories>
```

And then you can just add a dependency as any other:

```
<dependencies>
  (...)
  <dependency>
    <groupId>com.github.rskupnik</groupId>
    <artifactId>parrot</artifactId>
    <version>1.1</version>
  </dependency>
  (...)
</dependencies>
```

### Option 3

Simply copy the contents of the [Parrot.java](https://github.com/rskupnik/parrot/blob/master/src/main/java/com/github/rskupnik/parrot/Parrot.java) somewhere into your project and use it!

# Examples?

Parrot is very simple, so there are no examples. However, if you really need some, look at these files in the project:

[/src/test/java/com/github/rskupnik/parrot/ParrotTest.java](https://github.com/rskupnik/parrot/blob/master/src/main/java/com/github/rskupnik/parrot/Parrot.java)

[/src/test/resources/test.properties](https://github.com/rskupnik/parrot/blob/master/src/test/resources/test.properties)

[/testUserDir.properties](https://github.com/rskupnik/parrot/blob/master/testUserDir.properties)

# Rationale

When starting any new small hobby project I always use properties files. But these files require me to write some boilerplate code that will read them from the classpath/directory and expose the properties I'm interested in.

I've grown tired of writing this code over and over again so I created Parrot to take care of that for me so I can just use the properties I want from the get-go.

# Future

* Support for other propery file formats, such as YAML or XML
* Support for other variable types than String, so the users don't have to perform casts themselves
* Support for overlapping properties (i.e. the same property names defined in different files), probably through namespaces
