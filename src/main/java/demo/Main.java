package demo;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import com.itextpdf.text.pdf.*;

import java.io.*;

/**
 * author : fanzhe
 * email : fansy1990@foxmail.com
 * date : 2019/11/28 AM8:53.
 */
public class Main {
    public static void main(String[] args) throws IOException, DocumentException {
        String filename = "/Users/fanzhe/Downloads/test-1.pdf";

        PdfReader reader = new PdfReader(filename);

        // get number of pages
        int n = reader.getNumberOfPages();
        System.out.println("Number of pages: " + n); //240
        reader.close();

        splitPDF(filename, "./output/test-1(1-120).pdf",1,120);
        splitPDF(filename, "./output/test-1(121-240).pdf",119,240);


    }


    public static void splitPDF(String input,String output, int fromPage, int toPage) throws FileNotFoundException {
        splitPDF(new FileInputStream(input),new FileOutputStream(output),fromPage,toPage);
    }

    public static void splitPDF(InputStream inputStream,
                                OutputStream outputStream, int fromPage, int toPage) {
        Document document = null;
        try {
            PdfReader inputPDF = new PdfReader(inputStream);

            int totalPages = inputPDF.getNumberOfPages();

            //make fromPage equals to toPage if it is greater
            if(fromPage > toPage ) {
                fromPage = toPage;
            }
            if(toPage > totalPages) {
                toPage = totalPages;
            }

            document = new Document(inputPDF.getPageSizeWithRotation(1));
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data
            PdfImportedPage page;

            while(fromPage <= toPage) {
                document.newPage();
                page = writer.getImportedPage(inputPDF, fromPage);
                cb.addTemplate(page, 0, 0);
                fromPage++;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

}
