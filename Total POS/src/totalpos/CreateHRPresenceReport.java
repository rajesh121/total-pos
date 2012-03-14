
package totalpos;

import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author shidalgo
 */
public class CreateHRPresenceReport {
    private String fromDate;
    private String untilDate;
    private String storeName;
    JasperReportBuilder jrb;
    String[] columns;

    public CreateHRPresenceReport(String fromDate, String untilDate, String storeName, String[] columnsA) {
        this.fromDate = fromDate;
        this.untilDate = untilDate;
        this.storeName = storeName;
        this.columns = columnsA;
        build();
    }

    private void build(){
        jrb = report();

        jrb = jrb.setPageFormat(PageType.LETTER, PageOrientation.LANDSCAPE);
        jrb = jrb.setColumnTitleStyle(Constants.columnTitleStyle);

        jrb = jrb.title(cmp.text("Control de Asistencia y Horas extras"));

        

    }

}
