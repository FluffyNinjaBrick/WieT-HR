package com.wiethr.app.repository;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.wiethr.app.model.DaysOffRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;


public class GeneratePDF {

    public static ResponseEntity<byte[]> fromDaysOffRequest(DaysOffRequest request) throws IOException, DocumentException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();
        addMetaData(document);
        addContent(document);
        document.close();

        byte[] content = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("request.pdf", "request.pdf");
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }


    private static void addMetaData(Document document) {
        document.addTitle("Wniosek urlopowy");
        document.addSubject(LocalDate.now().toString());
        document.addCreator("WIeT-HR App");
    }

    private static void addContent(Document document) throws DocumentException {

        // shamelessly stolen from vogella.com, serves as mock content for now
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);

        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // now add all this to the document
        document.add(catPart);

    }

}
