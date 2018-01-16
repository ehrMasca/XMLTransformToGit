package servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;

import gitcontrol.GitControl;

/**
 * Servlet implementation class ArisToJbpmServlet
 */
@WebServlet("/ArisToJbpmServlet")
public class ArisToJbpmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String UPLOAD_DIRECTORY = "tmp";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ArisToJbpmServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
		
		if (isMultipart) {

			List<FileItem> multiparts = null;

			try {
				multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			} catch (FileUploadException e1) {
				e1.printStackTrace();
			}

			String inputName = null;
			String projectName = null;
			String gitRepository = null;
			String gitRepositoryAux = null;
			String projectPackage = null;
			File resultFile = null;
			
			for (FileItem item : multiparts) {
				if (item.isFormField()) { // Check form data
					inputName = (String) item.getFieldName();
					if (inputName.equalsIgnoreCase("projectName")) {
						projectName = (String) item.getString();
					} else if (inputName.equalsIgnoreCase("gitRepository")) {
						gitRepository = (String) item.getString();
					} else if (inputName.equalsIgnoreCase("projectPackage")) {
						projectPackage = (String) item.getString();
					}
				} else if (!item.isFormField()) { // Check field

					File fileAux = new File("src/input.bpmn2");
					FileUtils.copyInputStreamToFile(item.getInputStream(), fileAux);//take the input file and transform it to a readable file
					
					File xslAux = FileUtils.getFile("/opt/jboss/standalone/deployments/TransformationJBPM.war/WEB-INF/classes/aris-to-jbpm.xsl");//take the transformation sheet
					
					//FileUtils.getFile("/opt/jboss/standalone/deployments/TransformationJBPM.war/WEB-INF/classes/aris-to-jbpm.xsl");
					
					if(!gitRepository.substring(gitRepository.length()-4).equalsIgnoreCase(".git")){
						gitRepositoryAux = gitRepository+".git";
					}else {
						gitRepositoryAux = gitRepository;
						gitRepository = gitRepository.substring(0, gitRepository.length()-4);
					}
					
					//convert and push to the repository
					try {
						FileUtils.deleteDirectory(new File("src/repository"));//delete the repository directory
						//GitControl gc = new GitControl("src/repository", "https://github.com/ehrMasca/"+gitRepositoryAux);
						GitControl gc = new GitControl("src/repository", "/opt/jboss/reposbpm/git/.niogit/"+gitRepositoryAux+"/");
						gc.cloneRepo();

						resultFile = new File("src/repository/"+projectName+"/src/main/resources/"+projectPackage+"/"+item.getName());
						
						simpleTransform(fileAux, xslAux, resultFile);

						gc.addToRepo();
						gc.commitToRepo("add "+item.getName()+" transformed");
						gc.pushToRepo();
						FileUtils.deleteDirectory(new File("src/repository"));//delete the repository directory
					} catch (GitAPIException e) {
						e.printStackTrace();
					}

				}

			}
		}

	}

	//Function to transform the input bpmn2 file
	private void simpleTransform(File source, File xslt, File resultDir) {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = tFactory.newTransformer(new StreamSource(xslt));

			transformer.transform(new StreamSource(source), new StreamResult(resultDir));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
