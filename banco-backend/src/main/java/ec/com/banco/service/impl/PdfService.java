package ec.com.banco.service.impl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import ec.com.banco.dto.response.ReporteCuentaDTO;
import ec.com.banco.dto.response.ReporteMovimientoDTO;
import ec.com.banco.entity.Cliente;

@Service
public class PdfService {

    public String generarPdfEstadoCuenta(
            Cliente cliente,
            List<ReporteCuentaDTO> cuentas,
            LocalDate desde,
            LocalDate hasta
    ) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("ESTADO DE CUENTA"));
            document.add(new Paragraph("Cliente: " + cliente.getNombre()));
            document.add(new Paragraph("Identificación: " + cliente.getIdentificacion()));
            document.add(new Paragraph("Rango: " + desde + " - " + hasta));
            document.add(new Paragraph(" "));

            for (ReporteCuentaDTO c : cuentas) {
                document.add(new Paragraph("Cuenta: " + c.numeroCuenta()));
                document.add(new Paragraph("Tipo: " + c.tipoCuenta()));
                document.add(new Paragraph("Saldo Inicial: " + c.saldoInicial()));
                document.add(new Paragraph("Saldo Disponible: " + c.saldoDisponible()));
//                document.add(new Paragraph("Total Débitos: " + c.totalDebitos()));
//                document.add(new Paragraph("Total Créditos: " + c.totalCreditos()));
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Movimientos: "));
                for (ReporteMovimientoDTO m : c.movimientos()) {
                    document.add(new Paragraph(
                            " - " + m.fecha() + " | " + m.tipo() + " | $" + m.valor() + " | Saldo " + m.saldo()
                    ));
                }
                document.add(new Paragraph(" "));
            }

            document.close();

        } catch (Exception ex) {
            throw new RuntimeException("Error generando PDF");
        }

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
