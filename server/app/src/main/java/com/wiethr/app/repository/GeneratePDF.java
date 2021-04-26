package com.wiethr.app.repository;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.wiethr.app.model.DaysOffRequest;
import com.wiethr.app.model.DelegationRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Period;

import static com.wiethr.app.model.enums.LeaveType.*;   // this import is actually necessary, don't remove it


public class GeneratePDF {

    // =========== CALLED METHODS ===========

    public static ResponseEntity<byte[]> fromDelegationRequest(DelegationRequest request) throws DocumentException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();
        addMetadata(document, "Wniosek o delegacje");
        addDelegationRequestContent(document, request);
        document.close();

        byte[] content = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("request.pdf", "request.pdf");
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    public static ResponseEntity<byte[]> fromDaysOffRequest(DaysOffRequest request) throws DocumentException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();
        addMetadata(document, "Wniosek urlopowy");
        addDaysOffRequestContent(document, request);
        document.close();

        byte[] content = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("request.pdf", "request.pdf");
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }


    // =========== METADATA ===========

    private static void addMetadata(Document document, String title) {
        document.addTitle(title);
        document.addSubject(LocalDate.now().toString());
        document.addCreator("WIeT-HR App");
    }


    // =========== DAYS OFF REQUEST CONTENT ===========

    private static void addDaysOffRequestContent(Document document, DaysOffRequest request) throws DocumentException {

        Font regular = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font title = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font subscript = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);

        // name and date
        document.add(createNameAndDate(regular, request));
        document.add(createNameAndDateSubscripts(subscript));

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

        return new Paragraph(
                "Prosze o udzielenie urlopu" + leaveType + "za rok " + request.getDateFrom().getYear() + ","
                        + leaveTime + leaveSpan,
                regular
        );
    }


    // =========== DELEGATION REQUEST CONTENT ===========

    private static void addDelegationRequestContent(Document document, DelegationRequest request) throws DocumentException {

        Font regular = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font title = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font subscript = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);

        // name and date
        document.add(createNameAndDate(regular, request));
        document.add(createNameAndDateSubscripts(subscript));

        // title
        Paragraph titleText = new Paragraph("WNIOSEK O DELEGACJE", title);
        titleText.setAlignment(Element.ALIGN_CENTER);
        document.add(Chunk.NEWLINE);
        document.add(titleText);
        document.add(Chunk.NEWLINE);

        // request text
        document.add(createDelegationRequestText(regular, request));
        Paragraph destination = new Paragraph(
                "Cel podróży: " + request.getDestination(),
                regular
        );
        document.add(destination);
        document.add(Chunk.NEWLINE);

        // acceptance
        Paragraph acceptance;
        if (request.isSigned()) acceptance = new Paragraph(
                "Wyrazam zgode na wyjazd na delegacje we wskazanym terminie, "
                        + request.getSignedBy().getFirstName() + " " + request.getSignedBy().getLastName(),
                regular
        );
        else acceptance = new Paragraph(
                "Wyrazam zgode na wyjazd we wskazanym terminie, ......................................",
                regular
        );
        Paragraph signatureCaption = new Paragraph(
                "podpis osoby upowaznionej                                            ",
                subscript
        );
        signatureCaption.setAlignment(Element.ALIGN_RIGHT);
        document.add(acceptance);
        document.add(signatureCaption);

    }

    private static Paragraph createDelegationRequestText(Font regular, DelegationRequest request) {

        // delegations always have a "to" date, so we can ignore the warning
        String leaveTime = " na okres " + Period.between(request.getDateFrom(), request.getDateTo()).getDays() + " dni";
        String leaveSpan = " od dnia " + request.getDateFrom() + " do dnia " + request.getDateTo() + ".";

        return new Paragraph(
                "Prosze o udzielenie zezwolenia na wyjazd delegacyjny" + leaveTime + leaveSpan,
                regular
        );
    }


    // =========== COMMON CONTENT ===========

    private static Paragraph createNameAndDate(Font regular, com.wiethr.app.model.Document request) {
        Paragraph nameAndDate = new Paragraph(
                request.getNameAtSigning() + "                                 " + request.getDateIssued(),
                regular
        );
        nameAndDate.setAlignment(Element.ALIGN_CENTER);
        return nameAndDate;
    }

    private static Paragraph createNameAndDateSubscripts(Font subscript) {
        Paragraph nameAndDateSubscripts = new Paragraph(
                "Imie i Nazwisko" + "                                   " + "data wystawienia",
                subscript
        );
        nameAndDateSubscripts.setAlignment(Element.ALIGN_CENTER);
        return nameAndDateSubscripts;
    }

}
