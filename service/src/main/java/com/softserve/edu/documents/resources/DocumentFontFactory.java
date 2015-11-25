package com.softserve.edu.documents.resources;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.softserve.edu.documents.parameter.FileSystem;
import com.softserve.edu.documents.utils.FileUtils;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.EnumMap;

/**
 * Singleton.
 * Font factory.
 * Contains static methods for building fonts.
 */
public enum DocumentFontFactory {
    INSTANCE;

    static Logger logger = Logger.getLogger(DocumentFontFactory.class);

    /**
     * Map of font objects for reuse.
     */
    private EnumMap<DocumentFont, BaseFont> baseFontMap =
            new EnumMap<>(DocumentFont.class);

    /**
     * Builds font with the specified parameters.
     *
     * @param documentFont one of standard fonts
     * @return a font
     * @throws IOException if the requested font can't be found.
     */
    public Font buildFont(DocumentFont documentFont) throws IOException {
        Assert.notNull(documentFont, "the parameter can't be null");

        return createFont(documentFont);
    }

    /**
     * Builds font with the specified parameters.
     *
     * @param documentFont one of standard fonts
     * @param size         of font
     * @return a font
     * @throws IOException if the requested font can't be found.
     */
    public Font buildFont(DocumentFont documentFont, int size)
            throws IOException {
        Assert.notNull(documentFont, "documentFont can't be null");
        Assert.isTrue(size > 0, "size must be more then 0, actually is:" + size);

        return createFont(documentFont, size);
    }

    /**
     * Builds font with the specified parameters.
     *
     * @param documentFont one of standard fonts
     * @param size         of font
     * @param style        style of font
     * @return a font
     * @throws IOException if the requested font can't be found.
     */
    public Font buildFont(DocumentFont documentFont, int size, int style)
            throws IOException {
        Assert.notNull(documentFont, "documentFont can't be null");
        Assert.isTrue(size > 0, "size must be more then 0, actually is:" + size);
        Assert.isTrue(style > -1, "style must be more then -1, actually is:" + style);

        return createFont(documentFont, size, style);
    }

    /**
     * Creates a base font that can be used for creation of a font.
     * If the font has been previously build this function doesn't create
     * a new font, but returns the existing font.
     *
     * @param font to create
     * @return created font
     * @throws IOException if font is invalid or the font's file couldn't be found
     */
    private BaseFont getBaseFont(DocumentFont font) throws IOException {
        BaseFont baseFontObject = baseFontMap.get(font);

        if (baseFontObject == null) {
            FileObject file = FileUtils.findFile(FileSystem.RES,
                    ResourcesFolder.FONTS + FileName.SEPARATOR + font.toString());
            String path = file.getURL().getPath();

            try {
                baseFontObject = BaseFont.createFont(path,
                        BaseFont.IDENTITY_H, false);
            } catch (DocumentException exception) {
                logger.info(exception);
            }

            baseFontMap.put(font, baseFontObject);
        }

        return baseFontObject;
    }

    /**
     * Creates a font
     *
     * @param font to create
     * @return created font
     * @throws IOException if font is invalid or the font's file couldn't be found
     */
    private Font createFont(DocumentFont font) throws IOException {
        BaseFont bf = getBaseFont(font);
        return new Font(bf);
    }

    /**
     * Creates a font
     *
     * @param font to create
     * @param size needed font size
     * @return created font
     * @throws IOException if font is invalid or the font's file couldn't be found
     */
    private Font createFont(DocumentFont font, int size) throws IOException {
        BaseFont bf = getBaseFont(font);
        return new Font(bf, size);
    }

    /**
     * Creates a font
     *
     * @param font  to create
     * @param size  needed font size
     * @param style needed font style
     * @return created font
     * @throws IOException if font is invalid or the font's file couldn't be found
     */
    private Font createFont(DocumentFont font, int size, int style)
            throws IOException {
        BaseFont bf = getBaseFont(font);
        return new Font(bf, size, style);
    }
}
