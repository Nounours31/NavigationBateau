package sfa.nav.pdf;

import java.nio.charset.Charset;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.ListNumberingType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

public class PDF4DroiteDeHauteur {
	private Document document;
	private PdfDocument pdf;
	private PdfFont font;
	static final private float fontSize = 10.0f;
	static final private String root = "E:\\WorkSpaces\\WS\\GitHub\\Navigation\\NavigationBateau\\ProjetJava\\doc\\";
	static final float TextOffset = 15.0f;
	
	public PDF4DroiteDeHauteur(String id) {
		try {
			pdf = new PdfDocument(new PdfWriter(root + "DroiteDeHauteur_" + id + ".pdf"));
			pdf.addNewPage();
			document = new Document(pdf);
			font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			document.setFont(font).setFontSize(fontSize);
			document.setBorder(new DashedBorder(ColorConstants.GREEN, 1));
			document.setLeftMargin(5.0f);
			document.setRightMargin(5.0f);
			document.setTopMargin(5.0f);
			document.setBottomMargin(5.0f);
		}
		catch (Exception e) {
			pdf = null;
			document = null;
			font = null;
		}
	}
	
	public void addTitle (String title) {
		Div d = new Div();
        d.setPaddingLeft(1);
        d.setPaddingRight(1);
        d.setPaddingBottom(5);
        
        Table t = new Table(1);
        t.addCell(title);
        t.setBackgroundColor(ColorConstants.GREEN);
        t.setFontColor(ColorConstants.BLACK);
        t.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
        t.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);

        PageSize ps = pdf.getDefaultPageSize();
        float largeur = ps.getWidth();
        float largeurTxt = 2 * font.getWidth(title, fontSize);
        float HauteurTxt = 50;
        t.setWidth(largeurTxt);
        d.add(t);
        document.add(d);
	}
	
	public void addText (String titre, String text) {
		Div d = new Div();
        d.setPaddingLeft(1);
        d.setPaddingRight(1);
        d.setPaddingBottom(5);
        
        Table t = new Table(1);
        t.setBorder(Border.NO_BORDER);
        t.addCell(titre);
        t.setBackgroundColor(ColorConstants.WHITE);
        t.setFontColor(ColorConstants.BLACK);
        t.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.LEFT);
        t.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
        d.add(t);
        
        t = new Table(2);
        t.setBorder(Border.NO_BORDER);
        t.addCell("");
        t.addCell(text);
        t.getCell(0, 0).setMinWidth(TextOffset);
        
        PageSize ps = pdf.getDefaultPageSize();
        float largeur = ps.getWidth() - 50.0f;
        t.getCell(0, 1).setMinWidth(largeur - TextOffset);
        t.setBackgroundColor(ColorConstants.WHITE);
        t.setFontColor(ColorConstants.BLUE);
        t.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.LEFT);
        t.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);

        d.add(t);
        document.add(d);
	}

	public void addTextSansTitre (int nbOffset, String sText) {
		Div d = new Div();
        d.setPaddingLeft(1);
        d.setPaddingRight(1);
        d.setPaddingBottom(5);
        
        String text = formatMyText(sText);
        
        Table t = new Table(1 + nbOffset);
        t.setBorder(Border.NO_BORDER);
        for (int i = 1; i < nbOffset; i++) {
        	Cell c = new Cell();
        	c.setMinWidth(TextOffset).setBorder(Border.NO_BORDER);
        	t.addCell(c);
        }
        
        PageSize ps = pdf.getDefaultPageSize();
        float largeur = ps.getWidth() - 50.0f;
    	Cell c = new Cell();
    	c.add(new Paragraph(text)).setMinWidth(largeur - nbOffset * TextOffset).setBorder(Border.NO_BORDER);
        t.addCell(c);
        
        t.setBackgroundColor(ColorConstants.WHITE);
        t.setFontColor(ColorConstants.BLACK);
        t.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.LEFT);
        t.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);

        d.add(t);
        d.setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1));
        document.add(d);
	}

	
	private String formatMyText(String sText) {
		byte[] bs = sText.getBytes(Charset.forName("UTF8"));
		byte[] bs2 = new byte[bs.length * 2];
		
		int i = 0;
		for (byte b : bs) {
			if (b == '\n') {
				bs2[i++] = '\n';
				for (byte c : "\u00A0".getBytes(Charset.forName("UTF8"))) {
					bs2[i++] = c;
				}
			}
			else 
				bs2[i++] = b;
		}
		return new String(bs2, Charset.forName("UTF8"));
	}

	public void generePDF () {
		document.close();
	}

	public PdfDocument pdfDocument() {
		return pdf;
	}

	public Document document() {
		return document;
	}
}
