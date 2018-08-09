package com.cursomc.relatorios;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.ValueColumnBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.datatype.DateType;
import net.sf.dynamicreports.report.builder.style.BorderBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.builder.style.PenBuilder;
import net.sf.dynamicreports.report.builder.style.SimpleStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class DynamicReportsUtil {

    private static final String TITULO_DOCUMENTO = "POC de estudo geração de relatório com dynamic reports";

    private DynamicReportsUtil() {
    }

    public static final Integer MAXIMO_REGISTROS_CONSULTA = 2500;

    private static final int LARGURA_IMG_CABECALHO = 113;

    private static final int ALTURA_IMG_CABECALHO = 37;

    private static final float LINE_WIDTH = 0.5f;

    private static final int COLOR_R = 100;

    private static final int COLOR_G = 100;

    private static final int COLOR_B = 100;

    private static final int PADDING_TOP = 5;

    private static final int PADDING_RIGHT = 3;

    private static final int PADDING_BOTTOM = 5;

    private static final int PADDING_LEFT = 3;

    private static final int CINZA_DUZENTOS = 200;

    private static final int CINZA_DUZENTOS_VINTE = 220;

    private static final int FONT_SIZE_DEFAULT = 8;

    public static JasperReportBuilder getExportacao(String titulo, List<ColunasPropriedadeRelatorio> propriedades,
            CabecalhoRodapeRelatorio cabecalhoRodapeRelatorio, String tipo) {
        JasperReportBuilder jrb = getBuilderBase(propriedades);
        setarCabecalho(titulo, cabecalhoRodapeRelatorio, jrb, tipo);
        return jrb;
    }

    @SuppressWarnings({"rawtypes"})
    private static JasperReportBuilder getBuilderBase(List<ColunasPropriedadeRelatorio> propriedades) {
        JasperReportBuilder report = DynamicReports.report();

        PenBuilder penBuilder = stl.pen(LINE_WIDTH, LineStyle.SOLID).setLineColor(new Color(COLOR_R, COLOR_G, COLOR_B));
        BorderBuilder defaultBorder = stl.border(penBuilder);

        StyleBuilder alignLeft = stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setBorder(defaultBorder);
        setStylePadding(alignLeft, PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM, PADDING_LEFT);

        StyleBuilder alignRight = stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setBorder(defaultBorder);
        setStylePadding(alignLeft, PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM, PADDING_LEFT);

        HashMap<String, StyleBuilder> stylesMap = new HashMap<>();
        stylesMap.put(RelatorioConstantes.ALINHAR_ESQUERDA, alignLeft);
        stylesMap.put(RelatorioConstantes.ALINHAR_DIREITA, alignRight);

        StyleBuilder styleHeader = getStyleTitle();
        styleHeader.setBackgroundColor(new Color(CINZA_DUZENTOS, CINZA_DUZENTOS, CINZA_DUZENTOS));
        styleHeader.setBorder(defaultBorder);
        FontBuilder fontBuilder = stl.font().bold();
        styleHeader.setFont(fontBuilder);
        report.setColumnTitleStyle(styleHeader);

        for (int i = 0; i < propriedades.size(); i++) {
            ColunasPropriedadeRelatorio colunasPropriedadeRelatorio = propriedades.get(i);
            ValueColumnBuilder column = col.column(colunasPropriedadeRelatorio.getDescricaoColuna(), colunasPropriedadeRelatorio.getNomePropriedade(), colunasPropriedadeRelatorio.getTipo())
                    .setWidth(colunasPropriedadeRelatorio.getLargura());

            if (colunasPropriedadeRelatorio.getEstilo() != null) {
                column.setStyle(stylesMap.get(colunasPropriedadeRelatorio.getEstilo()));
            }
            if (colunasPropriedadeRelatorio.getTipo() == LocalDate.class) {
                column.setDataType(new DateType());
                LocalDateValueFormatter formatter = new DynamicReportsUtil.LocalDateValueFormatter();
                column.setValueFormatter(formatter);
            }
            report.addColumn(column);
        }

        SimpleStyleBuilder backgroundOddRowsStyle = stl.simpleStyle();
        backgroundOddRowsStyle.setBackgroundColor(new Color(CINZA_DUZENTOS_VINTE, CINZA_DUZENTOS_VINTE, CINZA_DUZENTOS_VINTE));

        report.setLocale(new Locale("pt", "BR"));
        report.setDetailSplitType(SplitType.PREVENT);
        report.setDetailOddRowStyle(backgroundOddRowsStyle);
        report.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE);
        report.setDefaultFont(stl.font().setFontSize(FONT_SIZE_DEFAULT));

        return report;
    }

    @SuppressWarnings("rawtypes")
    public static ByteArrayOutputStream getReportStream(JasperReportBuilder jrb, List list, String tipo) throws
            ExportacaoException {
        try {
            JRDataSource ds = new JRBeanCollectionDataSource(list);
            jrb.setDataSource(ds);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (tipo.equals(RelatorioConstantes.PDF)) {
                jrb.toPdf(os);
            } else {
                jrb.ignorePagination();
                jrb.toCsv(os);
            }
            return os;
        } catch (DRException e) {
            throw new ExportacaoException(e);
        }
    }

    private static void setStylePadding(StyleBuilder style, Integer paddingTop, Integer paddingRight,
            Integer paddingBottom, Integer paddingLeft) {
        style.setTopPadding(paddingTop);
        style.setRightPadding(paddingRight);
        style.setBottomPadding(paddingBottom);
        style.setLeftPadding(paddingLeft);
    }

    private static StyleBuilder getStyleTitle() {
        return stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).bold();
    }

    private static void setarCabecalho(String titulo, CabecalhoRodapeRelatorio cabecalhoRodapeRelatorio,
            JasperReportBuilder jrb, String tipo) {
        InputStream logo = DynamicReportsUtil.class.getResourceAsStream("/imagens/logo.png");

        HorizontalListBuilder dadosCabecalho = cmp.horizontalList();

        dadosCabecalho.add(cmp.text(TITULO_DOCUMENTO).setStyle(getStyleTitle()))
                .newRow();
        if (tipo.equals(RelatorioConstantes.PDF)) {
            dadosCabecalho
                    .add(cmp.image(logo).setFixedDimension(LARGURA_IMG_CABECALHO, ALTURA_IMG_CABECALHO))
                    .newRow();
        }
        dadosCabecalho.add(cmp.text(titulo).setStyle(getStyleTitle()))
                .newRow()
                .newRow();

        jrb.title(dadosCabecalho);

    }

    /**
     * LocalDate formatter para exibir datas no padrão brasileiro
     */
    static class LocalDateValueFormatter extends AbstractValueFormatter<String, LocalDate> {

        public LocalDateValueFormatter() {
        }

        public String format(LocalDate o, ReportParameters reportParameters) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return o.format(formatter);
        }

        public Class getValueClass() {
            return null;
        }
    }
}
