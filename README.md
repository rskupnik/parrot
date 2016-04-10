# Parrot

Parrot is a property-files abstraction layer that aims to simplify the usage of various property files.

It's very simple - Parrot loads every .properties file from the classpath and from the working directory and makes them available true one simple method.

So now instead of worrying about loading each properties file and extracting the appropriate properties, you just do...

```
// At this point Parrot will find and load all .properties files in classpath and root directory
Parrot.init();

// Parrot will look for the "optionalParameter" in each file and return an optional
Optional<String> optionalParameter = Parrot.get("optionalParameter");

// Parrot will return a copy of all the parameters it found in the properties files
Map<String, String> allParameters = Parrot.all(); 
```

... and you're done. Just put your .properties files either in resources or at the root directory and use the parameters freely.
