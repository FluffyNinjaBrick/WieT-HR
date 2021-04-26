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
import java.time.Period;


public class GeneratePDF {

    public static ResponseEntity<byte[]> fromDaysOffRequest(DaysOffRequest request) throws IOException, DocumentException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();
        addDaysOffRequestMetadata(document);
        addContent(document, request);
        document.close();

        byte[] content = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("request.pdf", "request.pdf");
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }


    private static void addDaysOffRequestMetadata(Document document) {
        document.addTitle("Wniosek urlopowy");
        document.addSubject(LocalDate.now().toString());
        document.addCreator("WIeT-HR App");
    }

    private static void addContent(Document document, DaysOffRequest request) throws DocumentException {

        Font regular = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font title = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font subscript = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);

        // name and date
        Paragraph nameAndDate = new Paragraph(
                request.getNameAtSigning() + "                                 " + request.getDateIssued(),
                regular
        );
        nameAndDate.setAlignment(Element.ALIGN_CENTER);
        Paragraph nameAndDateSubscripts = new Paragraph(
                "Imie i Nazwisko" + "                                   " + "data wystawienia",
                subscript
        );
        nameAndDateSubscripts.setAlignment(Element.ALIGN_CENTER);
        document.add(nameAndDate);
        document.add(nameAndDateSubscripts);

        // title
        Paragraph titleText = new Paragraph("WNIOSEK URLOPOWY", title);
        titleText.setAlignment(Element.ALIGN_CENTER);
        document.add(Chunk.NEWLINE);
        document.add(titleText);
        document.add(Chunk.NEWLINE);

        // request text
        document.add(createDaysOffRequestText(regular, request));
        document.add(Chunk.NEWLINE);

        // acceptance
        Paragraph acceptance;
        if (request.isSigned()) acceptance = new Paragraph(
                "Wyrazam zgode na urlop we wskazanym terminie, "
                + request.getSignedBy().getFirstName() + " " + request.getSignedBy().getLastName(),
                regular
        );
        else acceptance = new Paragraph(
                "Wyrazam zgode na urlop we wskazanym terminie, ......................................",
                regular
        );
        Paragraph signatureCaption = new Paragraph(
                "podpis osoby upowaznionej                                              ",
                subscript
        );
        signatureCaption.setAlignment(Element.ALIGN_RIGHT);
        document.add(acceptance);
        document.add(signatureCaption);

    }


    private static Paragraph createDaysOffRequestText(Font regular, DaysOffRequest request) {

        String leaveType;
        switch (request.getLeaveType()) {
            case SICK:
                leaveType = " chorobowego ";
                break;
            case MATERNITY:
                leaveType = " macierzynskiego ";
                break;
            case RECREATIONAL:
                leaveType = " wypoczynkowego ";
                break;
            default:
                leaveType = "";
        }

        String leaveTime, leaveSpan;
        if (request.getDateTo() != null) {
            leaveTime = " w liczbie " + Period.between(request.getDateFrom(), request.getDateTo()).getDays() + " dni";
            leaveSpan = " od dnia " + request.getDateFrom() + " do dnia " + request.getDateTo() + ".";
        }
        else {
            leaveTime = " na czas nieokreslony";
            leaveSpan = " od dnia " + request.getDateFrom() + ".";
        }

        String leaveTimespan;
        Paragraph requestText = new Paragraph(
                "Prosze o udzielenie urlopu" + leaveType + "za rok " + request.getDateFrom().getYear() + ","
                        + leaveTime + leaveSpan,
                regular
        );
        return requestText;
    }

}
