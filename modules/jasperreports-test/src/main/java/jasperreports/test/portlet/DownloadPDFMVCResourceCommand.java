package jasperreports.test.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import jasperreports.test.constants.JasperreportsTestPortletKeys;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;


@Component(immediate = true, property = {
		"javax.portlet.name=" + JasperreportsTestPortletKeys.JasperreportsTest, "mvc.command.name=/download/pdf"
	}, service = MVCResourceCommand.class)
public class DownloadPDFMVCResourceCommand implements MVCResourceCommand {

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		OutputStream outputStream = null;
		
		try {
			outputStream = resourceResponse.getPortletOutputStream();
			
			URL jasperURL = Thread.currentThread().getContextClassLoader().getResource("report1.jasper");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperURL);

			Map<String, String> fields = new HashMap<String, String>();

			JRMapArrayDataSource dataSource = new JRMapArrayDataSource(new Object[] {
				fields
			});
			Map<String, Object> parameters = new HashMap<>();

			resourceResponse.setContentType("application/pdf");
			resourceResponse.setProperty(
				"Content-Disposition", "attachment; filename=test.pdf");
			
			JasperPrint printer = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
			JasperExportManager.exportReportToPdfStream(printer, outputStream);
		} catch (Exception exception) {
			_log.error("Error found writing PDF, ", exception);
			throw new PortletException(exception);
		} finally {
			try{
				if(outputStream != null){
					outputStream.close();
				}
			}catch(IOException exception){
				
			}
		}
		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(DownloadPDFMVCResourceCommand.class);
}
