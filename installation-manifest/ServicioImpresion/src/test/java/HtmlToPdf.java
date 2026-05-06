import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.io.FileNotFoundException;
import java.io.IOException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gio_j
 */
public class HtmlToPdf {
	public static final StringBuilder HTML = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n")
.append("<html>\n")
.append("<head>\n")
.append("  <title></title>\n")
.append("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n")
.append("  <style type=\"text/css\">\n")
.append("    a {text-decoration: none}\n")
.append("  </style>\n")
.append("</head>\n")
.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\">\n")
.append("HOLA mundo")
.append("</body>\n")
.append("</html>");
	
	
    public static void main( String[] args ) throws FileNotFoundException, IOException{
//        try{
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//
//            //step1: render html to memory         
//            ITextRenderer renderer = new ITextRenderer();
//            renderer.setDocumentFromString(HTML.toString());
//            renderer.layout();
//            renderer.createPDF(os);
//
//            //step2: conver to byte array stream         
//            System.out.println(Base64.getEncoder().encodeToString(os.toByteArray()));
//            os.close();
//        }catch(DocumentException ex){
//            
//        }
            
         
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out));
        pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
        ConverterProperties properties = new ConverterProperties();
        MediaDeviceDescription med = new MediaDeviceDescription(MediaType.ALL);
        properties.setMediaDeviceDescription(med); 
        HtmlConverter.convertToPdf(HTML.toString(), pdfDocument, properties);
        System.out.println(Base64.getEncoder().encodeToString(out.toByteArray()));
    }
}
