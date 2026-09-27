package com.ashraful.learningquest.data

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

object BengaliTranslation {

    fun translate(
        text: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (text.isBlank()) {
            onError("Nothing to translate")
            return
        }

        if (text.any { it in '\u0980'..'\u09FF' }) {
            onSuccess(text)
            return
        }

        val languageIdentifier = LanguageIdentification.getClient()

        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageTag ->
                if (languageTag == "und") {
                    languageIdentifier.close()
                    onError("Language could not be detected")
                    return@addOnSuccessListener
                }

                val sourceLanguage = TranslateLanguage.fromLanguageTag(languageTag)
                if (sourceLanguage == null || sourceLanguage == TranslateLanguage.BENGALI) {
                    languageIdentifier.close()
                    onSuccess(text)
                    return@addOnSuccessListener
                }

                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(sourceLanguage)
                    .setTargetLanguage(TranslateLanguage.BENGALI)
                    .build()

                val translator = Translation.getClient(options)

                translator.downloadModelIfNeeded(
                    DownloadConditions.Builder().build()
                ).addOnSuccessListener {
                    translator.translate(text)
                        .addOnSuccessListener { translated ->
                            translator.close()
                            languageIdentifier.close()
                            onSuccess(translated)
                        }
                        .addOnFailureListener { error ->
                            translator.close()
                            languageIdentifier.close()
                            onError(error.localizedMessage ?: "Translation failed")
                        }
                }.addOnFailureListener { error ->
                    translator.close()
                    languageIdentifier.close()
                    onError(error.localizedMessage ?: "Translation model could not be downloaded")
                }
            }
            .addOnFailureListener { error ->
                languageIdentifier.close()
                onError(error.localizedMessage ?: "Language detection failed")
            }
    }
}
