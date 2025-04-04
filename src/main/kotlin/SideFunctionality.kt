package com.hlianole.kotlin_declaration_printer

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import java.io.File

fun createKotlinCoreEnvironment() : KotlinCoreEnvironment {
    val config = CompilerConfiguration()
    return KotlinCoreEnvironment.createForProduction(
        parentDisposable = Disposer.newDisposable(),
        configuration = config,
        configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES
    )
}

fun findFilesToProcess(folder : File): List<File> {
    return folder.walkTopDown().filter {
        it.isFile && it.extension == "kt"
    }.toList()
}

fun processFile(file: File, env: KotlinCoreEnvironment) {
    val sourceText = file.readText()
    val psiFile = PsiFileFactory.getInstance(env.project)
        .createFileFromText(file.name, KotlinLanguage.INSTANCE, sourceText)

    if (psiFile is KtFile) {
        psiFile.children.forEach { child ->
            processMembers(child)
        }
    }
}

private fun processMembers(member: PsiElement, prefix: String = "") {
    when (member) {
        is KtClass -> {
            if (member.isPublic()) {
                println("${prefix}class ${member.name} {")
                member.children.forEach { child ->
                    processMembers(child, "$prefix   ")
                }
                println("${prefix}}")
            }
        }
        is KtObjectDeclaration -> {
            if (member.isPublic()) {
                println("${prefix}object ${member.name} {")
                member.children.forEach { child ->
                    processMembers(child, "$prefix   ")
                }
                println("${prefix}}")
            }
        }
        is KtNamedFunction -> {
            if (member.isPublic()) {
                println("${prefix}fun ${member.name}() {")
                member.children.forEach { child ->
                    processMembers(child, "$prefix   ")
                }
                println("${prefix}}")
            }
        }
        is KtProperty -> {
            if (member.isPublic()) {
                if (member.isVar) {
                    println("${prefix}var ${member.name}")
                } else {
                    println("${prefix}val ${member.name}")
                }
            }
        }
        else -> {
            member.children.forEach { child ->
                processMembers(child, prefix)
            }
        }
    }
}

private fun KtModifierListOwner.isPublic() : Boolean {
    return this.modifierList?.hasModifier(KtTokens.PRIVATE_KEYWORD) != true &&
           this.modifierList?.hasModifier(KtTokens.PROTECTED_KEYWORD) != true &&
           this.modifierList?.hasModifier(KtTokens.INTERNAL_KEYWORD) != true
}
