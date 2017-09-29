# API Client Library for [CodeBeamer](https://intland.com)

## How to Use:

```
./gradlew publishToMavenLocal
```

And then depend on it in your project, e.g.:

```
repositories {
    mavenLocal()
}
dependencies {
    compile(group: 'com.intland', name: 'codebeamer-api-client', version: '1.0-SNAPSHOT')
}
```
