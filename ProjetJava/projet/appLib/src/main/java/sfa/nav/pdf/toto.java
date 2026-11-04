package sfa.nav.pdf;

import java.io.IOException;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.ListNumberingType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.kernel.colors.ColorConstants;

// https://api.itextpdf.com/iText/java/8.0.1/com/itextpdf/layout/element/package-summary.html


public class toto {
	public static void main(String[] args) throws IOException { 
		PDF4DroiteDeHauteur dh = new PDF4DroiteDeHauteur("cc");
		
		PdfDocument pdf = dh.pdfDocument();
		Document document = dh.document();
/*
		dh.addTitle("Droite de hauteur de lune");
		dh.setItem("1. GHA");
		dh.setText("\t\tContenu\tff");
		dh.setText("Contenu\tff");
		dh.setText("Contenu\tff");
		dh.setText("Contenu\tff");
		dh.setText("Contenu\tff");
		
	*/	
		
		
		
		String line = "Hello! Welcome to iTextPdf\ntoto";
	    document.add(new Paragraph(line));
	    
	    List l = new List(ListNumberingType.GREEK_LOWER);
	    l.add("toto");
	    l.add("titi");
	    document.add(l);
	    
	    // itext 7 text color
	    PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
        Text title = new Text("The Strange Case of Dr. Jekyll and Mr. Hyde").setFont(font);
        title.setUnderline();
        title.setBold();
        title.setBackgroundColor(ColorConstants.GREEN);
        title.setFontColor(ColorConstants.RED);
        
        Text author = new Text("Robert Louis Stevenson").setFont(font);
        Paragraph p = new Paragraph().add(title).add(" by ").add(author);
        document.add(p);
        
        p = new Paragraph();
        p.add("The beginning of the line ");
        p.add(new Text("          (fill in your name)          ").setTextRise(-10).setUnderline().setFontSize(8));
        p.add(" end of the line");
        document.add(p);
        
        PageSize ps = pdf.getDefaultPageSize();
        
        p = new Paragraph("The quick brown fox");
        // PdfCanvas canvas = new PdfCanvas(pdf.addNewPage());
        PdfCanvas canvas = new PdfCanvas(pdf.getFirstPage());
        Rectangle rect = new Rectangle(ps.getWidth() - 90, ps.getHeight() - 100, 50, 50);
        
        canvas.rectangle(rect)
        .setFillColor(ColorConstants.LIGHT_GRAY)
        .fillStroke();

        new Canvas(canvas, rect)
             .setFontColor(ColorConstants.WHITE)
             .setFontSize(12)
             .add(p);
        
        Div div2 = new Div();
        div2.setPaddingLeft(1);
        div2.setPaddingRight(1);
        div2.setPaddingBottom(5);
        
        div2.add(new Paragraph().add(new Paragraph("Hola Mundo")
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                )
                .setPaddingBottom(5));
        Border border = new SolidBorder(ColorConstants.GREEN, 3);
        div2.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
        div2.setBorder(border);
        
        document.add(div2);

        div2 = new Div();
        div2.setPaddingLeft(1);
        div2.setPaddingRight(1);
        div2.setPaddingBottom(5);
		
        Paragraph pp  = new Paragraph();
        pp.add("Contenu\tff\n");
        Paragraph ppp  = new Paragraph();
        ppp.add("xxContenu\tff\n");
        ppp.setPaddingLeft(20.0f);
        pp.add(ppp);
        pp.add("      2Contenu\tff\n");
        pp.add("Contenu\tff\n");
		
        div2.add(pp.setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                );
        border = new SolidBorder(ColorConstants.BLUE, 1);
        div2.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE);
        div2.setBorder(border);

        document.add(div2);
        
	    document.close();
	}
}
