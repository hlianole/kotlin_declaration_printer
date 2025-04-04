package com.hlianole.kotlin_declaration_printer

import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import java.io.File

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Usage: ./solution <path-to-file-or-folder>")
        return
    }

    val path = File(args[0])
    if (!path.exists()) {
        println("Path does not exist")
        return
    }

    val env = createKotlinCoreEnvironment()

    try {
        val files = if (path.isDirectory) {
            findFilesToProcess(path)
        } else {
            listOf(path)
        }

        files.forEach { file ->
            println("---Processing ${file.name}---")
            processFile(file, env)
        }
    } finally {
        Disposer.dispose(env.project)
    }
}