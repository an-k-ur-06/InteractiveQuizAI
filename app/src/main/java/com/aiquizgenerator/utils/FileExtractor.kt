package com.aiquizgenerator.utils
import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.InputStream

object FileExtractor {
    fun extractText(context: Context, uri: Uri, mimeType: String?): Result<String> {
        return try {
            val stream = context.contentResolver.openInputStream(uri)
                ?: return Result.failure(Exception("Cannot open file"))
            val text = when {
                mimeType?.contains("pdf") == true || uri.path?.endsWith(".pdf", true) == true -> extractPdf(stream)
                mimeType?.contains("wordprocessingml") == true || uri.path?.endsWith(".docx", true) == true -> extractDocx(stream)
                else -> stream.bufferedReader().readText()
            }
            if (text.isBlank()) Result.failure(Exception("No text found in file"))
            else Result.success(text)
        } catch (e: Exception) { Result.failure(e) }
    }
    private fun extractPdf(stream: InputStream): String {
        val doc = PDDocument.load(stream); val text = PDFTextStripper().getText(doc); doc.close(); return text
    }
    private fun extractDocx(stream: InputStream): String {
        val doc = XWPFDocument(stream); val sb = StringBuilder()
        doc.paragraphs.forEach { sb.appendLine(it.text) }; doc.close(); return sb.toString()
    }
    fun getFileName(context: Context, uri: Uri): String {
        var name = "document"
        context.contentResolver.query(uri, null, null, null, null)?.use { c ->
            if (c.moveToFirst()) { val i = c.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME); if (i != -1) name = c.getString(i) }
        }
        return name
    }
}
