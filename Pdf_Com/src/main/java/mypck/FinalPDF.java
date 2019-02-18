package mypck;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

public class FinalPDF {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// same files
		System.out.println("Hi how are you");
		list_pdf_file(args[0],args[1],args[2]);
		

	}

	public static ResultDetails comparePDF(String expectedPDF, String actualPDF, boolean comparePdfAsImage,
			boolean convertPDFToXml) throws IOException {

		ResultDetails rs1 = new ResultDetails();
		try {
		ResultDetails rs = new ResultDetails();
		String actPdfData = "";
		String expPdfData = "";
		boolean comparePdfAsImages = false;
		boolean convertToXml = false;
		
		if (!comparePdfAsImages) {
			comparePdfAsImages = comparePdfAsImage;
		}
		if (!convertToXml) {
			convertToXml = convertPDFToXml;
		}
		List<String> expListData = new ArrayList<String>();
		List<String> actListData = new ArrayList<String>();

		java.lang.System.setErr(new PrintStream(new NullOutputStream()));

		if ((expectedPDF == null || actualPDF == null)) {
			System.out.println("Please provide the file names");
			return rs;
		}

		// Creating the object for the files
		PDDocument expPdfDocument = PDDocument.load(new File(expectedPDF));
		PDDocument actPdfDocument = PDDocument.load(new File(actualPDF));

		if (!(expPdfDocument.getNumberOfPages() == actPdfDocument.getNumberOfPages())) {
			rs.setR1_file_name(expectedPDF);
			rs.setR2_file_name(actualPDF);
			rs.setR1_Diff_details("Both the files pages are not equal, first file page count is " + expPdfDocument.getNumberOfPages() + " and second file is " +actPdfDocument.getNumberOfPages());
			return rs;
		}

		// Gets the PDF document data and stores them into strings
		if (!expPdfDocument.isEncrypted() && !actPdfDocument.isEncrypted()) {
			PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);
			PDFTextStripper Tstripper = new PDFTextStripper();
			expPdfData = Tstripper.getText(expPdfDocument);
			actPdfData = Tstripper.getText(actPdfDocument);
		}

		if (expPdfData.equals(actPdfData)) {
			System.out.println("Both the files are having same data");
		}

		else {
			// System.out.println("Data in the files are not same");
			StringTokenizer stExpPdf1 = new StringTokenizer(expPdfData, "\n");
			StringTokenizer stActPdf1 = new StringTokenizer(actPdfData, "\n");
			rs.setR1_file_name(expectedPDF);
			rs.setR2_file_name(actualPDF);
			rs.setR1_Diff_details("Both the files Data  is not equal ");
			while (stExpPdf1.hasMoreTokens()) {
				expListData.add(stExpPdf1.nextToken());
			}

			while (stActPdf1.hasMoreTokens()) {
				actListData.add(stActPdf1.nextToken());
			}
			//String diff_data ="";
			List<String> tempListData = new ArrayList<String>(expListData);
			tempListData.removeAll(actListData);
			
			String diff_data ="";
			System.out.println("------------R1  PDF File --------------" + expectedPDF ); 
			for(int i=0;i<tempListData.size();i++){
				diff_data +=  tempListData.get(i);
				diff_data +=  "\r\n";
              //  System.out.println(tempListData.get(i)); //content from test.txt which is not there in test2.txt
            }
			
			
			rs.setR1_Diff_details(diff_data);
			System.out.println(diff_data);
			
			System.out.println("------------R2 PDF File --------------" + actualPDF ); 
			 
			
			
			diff_data ="";
			tempListData = actListData ;
			tempListData.removeAll(expListData);
					
			for(int i=0;i<tempListData.size();i++){
				diff_data +=  tempListData.get(i);
				diff_data +=  "\r\n";
              //System.out.println(tempListData.get(i)); //content from test.txt which is not there in test2.txt
            }
			rs.setR2_diff_details(diff_data);
			System.out.println(diff_data);
			System.out.println("------------File Comparison Ends --------------"); 
			
			 
		}

	
		return rs;
		}catch(Exception ex)
		{
			System.out.println("comparePDF "+ ex);
		}
		return rs1;
	}

	public static void list_pdf_file(String r1_path,String r2_path,String diff_path) throws IOException {
		
		//File folder = new File("C:\\\\Projects\\\\Optus\\\\B2B\\\\R1");
		File folder = new File(r1_path);
		
		ArrayList<ResultDetails> dtlList = new ArrayList<ResultDetails>(2000);
		File[] listOfFiles = folder.listFiles();
		//String filePathString = "C:\\Projects\\Optus\\B2B\\R2\\";
		String filePathString = r2_path;
		String filename;
		try {
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith("pdf")) {

				File f = new File(filePathString);

				filename = listOfFiles[i].getName().substring(0, 10) + "*pdf";
				FileFilter fileFilter = new WildcardFileFilter(filename);
				File[] files = f.listFiles(fileFilter);

				if (files.length > 0) {
					for (int j = 0; j < files.length; j++) {
						ResultDetails rs = comparePDF(listOfFiles[i].getAbsolutePath(), files[j].getAbsolutePath(),
								false, true);
						dtlList.add(rs);
					}
				} else {
					//System.out.println("File: " + filename + " Doesn't exist in R2: ");
					//System.out.println("Size of al  : " + dtlList.size());
				}

			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}
		}
		catch (IndexOutOfBoundsException ex)
		{
			ex.printStackTrace();
			System.out.println(ex);	
		}
		catch(Exception e)
		{
			System.out.println("Exception is  " + e.getMessage());
		}
		finally
		{
		System.out.println("Program Ends ");
		System.out.println("Size of al  : " + dtlList.size());
		create_xls_file(dtlList,diff_path);
		}
		
	}

	public static void create_xls_file(ArrayList<ResultDetails>  dtlList,String diff_path) {
		try {
			//String filename = "C:\\\\Projects\\\\Optus\\\\B2B\\NewExcelFile.xls";
			//String filename = diff_path + "/NewExcelFile.xls";
			String filename = diff_path + "\\NewExcelFile.xls";
			
			//FileWriter outputfile = new FileWriter(filename);
			
			System.out.println(" create_xls_file file name is " + filename);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("FirstSheet");
			
			HSSFRow rowhead = sheet.createRow((short) 0);
			sheet.setColumnWidth(0, 18000);
			sheet.setColumnWidth(1, 18000);
			sheet.setColumnWidth(2, 18000);
			sheet.setColumnWidth(3, 18000);
			 CellStyle style = workbook.createCellStyle(); //Create new style
             style.setWrapText(true); //Set wordwrap
			rowhead.createCell(0).setCellValue("R1 File Name");
			rowhead.createCell(1).setCellValue("R2 File Name");
			rowhead.createCell(2).setCellStyle(style);
			rowhead.createCell(2).setCellValue("R1 Diff Details");
			rowhead.createCell(3).setCellStyle(style);
			rowhead.createCell(3).setCellValue("R2 Diff Details");
			int counter=0;
			System.out.println(" before For loop \n");;
			for( ResultDetails rs :dtlList )
			{
				counter++;
			HSSFRow row = sheet.createRow((short) counter);
			row.setRowStyle(style);
			row.createCell(0).setCellValue(rs.getR1_file_name());
			row.createCell(1).setCellValue(rs.getR2_file_name());
			row.createCell(2).setCellStyle(style);
			row.createCell(2).setCellValue(rs.getR1_Diff_details());
			row.createCell(3).setCellStyle(style);
			row.createCell(3).setCellValue(rs.getR2_diff_details());
			
			/*	outputfile.write(rs.getR1_file_name() + ",");
				outputfile.write(rs.getR2_file_name() +" ,");
				outputfile.write(rs.getDiff_details() + ",");*/
			}
			System.out.println(" After For loop \n");
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
			//outputfile.close();
			System.out.println("Your excel file has been generated!");

		}catch (IOException  ex) {
			System.out.println(ex);
		}
		catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
