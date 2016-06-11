# What is it?

Parrot is a property-files abstraction layer that aims to simplify the usage of multiple property files.

It's very simple - Parrot loads every .properties file from the classpath and from the working directory and makes them available through one simple method.

So now instead of worrying about loading each properties file and extracting the appropriate properties, you just do...

```
// At this point Parrot will find and load all .properties files in classpath and root directory
// If you want to specify exact files you can do that too, see next code snipept
Parrot parrot = new Parrot();

// Parrot will look for the "optionalParameter" in the map of properties it loaded from the files and return an optional
Optional<String> optionalParameter = Parrot.get("optionalParameter");

// Parrot will return a copy of all the parameters it found in the properties files
Map<String, String> allParameters = Parrot.all(); 
```

... and you're done. Just put your .properties files either in resources or at the root directory and use the parameters freely.

Parrot only deals with Strings for now so it's up to you to cast your values to proper types. This will change in later versions.

You can also specify the exact .properties file names you want Parrot to load, in case you don't want it to load everything in the classpath and root directory:

```
// Note how you don't need to include the .properties, but it's valid to do so
Parrot parrot = new Parrot("somePropertiesFile1", "somePropertiesFile2.properties");
```

# How to get it?

### Option 1

Add the jcenter repository:

```
<repository>
  <snapshots>
      <enabled>false</enabled>
  </snapshots>
  <id>central</id>
  <name>bintray</name>
  <url>http://jcenter.bintray.com</url>
</repository>
```

And then you can just add a dependency as any other:

```
<dependency>
    <groupId>com.github.rskupnik</groupId>
    <artifactId>parrot</artifactId>
    <version>2.0</version>
</dependency>
```
 
### Option 2

`git clone` -> `mvn install`

### Option 3

Simply copy the contents of the [Parrot.java](https://github.com/rskupnik/parrot/blob/master/src/main/java/com/github/rskupnik/parrot/Parrot.java) somewhere into your project and use it!

# Can I mock it?

Yes, it's pretty straightforward.

```
when(parrotMock.get("some-param")).thenReturn(Optional.of("mocked-value"));
```

You can inject the mocked Parrot instance using dependency injection or, in case of simple projects, a known strategy as follows:
```
public class YourClass {
    private Parrot parrot;
    
    /* Your code here that uses parrot via getParrot()... */
    
    public Parrot getParrot() {
        if (parrot == null)
            parrot = new Parrot();
        
        return parrot;
    }
}

@RunWith(MockitoJUnitRunner.class)
public class YourClassTest {

    @Spy
    private YourClass yourClass = new YourClass();
    
    @Mock
    private Parrot parrotMock;
    
    @Before
    public void before() {
       when(yourClass.getParrot()).thenReturn(parrotMock);
       when(parrotMock.get("some-param")).thenReturn(Optional.of("mocked-value"));
       // ...
    }
}
```

# Examples?

Parrot is very simple, so there are no examples. However, if you really need some, look at these files in the project:

[/src/test/java/com/github/rskupnik/parrot/ParrotTest.java](https://github.com/rskupnik/parrot/blob/master/src/test/java/com/github/rskupnik/parrot/ParrotTest.java)

[/src/test/resources/test.properties](https://github.com/rskupnik/parrot/blob/master/src/test/resources/test.properties)

[/testUserDir.properties](https://github.com/rskupnik/parrot/blob/master/testUserDir.properties)

# Rationale

When starting any new small hobby project I always use properties files. But these files require me to write some boilerplate code that will read them from the classpath/directory and expose the properties I'm interested in.

I've grown tired of writing this code over and over again so I created Parrot to take care of that for me so I can just use the properties I want from the get-go.

# Future

* Support for other propery file formats, such as YAML or XML
* Support for other variable types than String, so the users don't have to perform casts themselves
* Support for overlapping properties (i.e. the same property names defined in different files), probably through namespaces

# Credits

Icon made by Freepik from www.flaticon.com 
