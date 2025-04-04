# kotlin_declaration_printer
Project that allows to print all public declarations from Kotlin file/folder with Kotlin files

## Project structure
`/src/main/kotlin/`: Project source code

`/build.gradle.kts`: Configuration of gradle build

`/build/dist/`: Folder with executable file

`/solution`: Bash script to simplify usage

## Usage
In root folder
```
./gradlew clean build
``` 
```
chmod +x solution
```
```
./solution <path-to-file-or-folder>
```

## Dependencies
`org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.0`: For Kotlin code analyzing
`org.jetbrains.kotlin:kotlin-stdlib`: Kotlin standart library