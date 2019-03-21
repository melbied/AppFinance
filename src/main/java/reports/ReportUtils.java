/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reports;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import model.Dossierhsupp;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

/**
 *
 * @author Mounir
 */
public class ReportUtils implements Serializable {
    
    
    public static void exportarPDF(String fileName, Map<String,Object> param, List<Object> liste) throws JRException, IOException{
        

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/"+fileName+".jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),param, new JRBeanCollectionDataSource(liste));
        System.out.println("reports.ReportUtils.exportarPDF()");
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        //response.addHeader("Content-disposition","attachment; filename="+fileName+".pdf");
        response.setHeader("Cache-Control", "max-age=0");
        response.setContentType("application/pdf");
        ServletOutputStream stream = response.getOutputStream();
        System.out.println("reports.ReportUtils.exportarPDF()");

        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
        stream.flush();
        stream.close();
        
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    	public static void exportarDOC(String fileName, Map<String,Object> param, List<Object> liste) throws JRException, IOException{
		
		
                File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/"+fileName+".jasper"));
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),param, new JRBeanCollectionDataSource(liste));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename="+fileName+".docx");
		ServletOutputStream stream = response.getOutputStream();
		
                JRDocxExporter exporter = new JRDocxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
		exporter.exportReport();
		
		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

        public static double calculerMontant(Dossierhsupp ds){
            double taux = ds.getNbrHeures() * ds.getIdGrade().getTaux();
            
            double b = Math.abs((ds.getAllocationFamiliale() + ds.getBrutAdditionner() + taux) - ds.getSalaireAnnuelleBrut());
            
            double frais = 0.0;
            if(b * 0.2 > 30000){
                frais = 30000;
            }else{
                frais = b * 0.2;
            }
            
            double totalDeduire = frais + ds.getAmo() + ds.getRetenuCmr() + ds.getMutuelleCaisse() + ds.getMutuelleMutialiste() + ds.getRachatCmr();
            double c = Math.abs(b - totalDeduire);
            double d = 0.3 * c;
            double charge = ds.getNbrEnfant() * ds.getChargeFamiliale();
            double e = Math.abs(d - ds.getSommeDeduire() - charge);
            double f = Math.abs(e - ds.getIrSource() - ds.getIrComplement());
            
            double montant = Math.abs(taux - Math.floor(f)); 
            
            
            return montant;
            
        }
        
            public static void exportarExcel(String fileName, Map<String,Object> param, List<Object> liste) throws JRException, IOException{
		
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/"+fileName+".jasper"));
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(),param, new JRBeanCollectionDataSource(liste));
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition","attachment; filename="+fileName+".xls");
		ServletOutputStream stream = response.getOutputStream();
		
                JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
		exporter.exportReport();
		
		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}
        
}
