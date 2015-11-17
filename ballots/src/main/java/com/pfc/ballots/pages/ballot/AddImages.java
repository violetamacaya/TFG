package com.pfc.ballots.pages.ballot;

import java.util.ArrayList;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.pfc.ballots.dao.BallotDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.util.ImageHandle;
@Import(
		library = {
				"context:js/jquery-min.js",
				"context:js/jquery.ajax.upload.js",
				"context:js/upload.js"
		})
public class AddImages {

	@SessionState
	private DataSession datasession;

	@SessionAttribute
	private String ballotIdSesion;

	@Persist
	@Property
	private Ballot ballot;
	
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	@Persist
	BallotDao ballotDao;

	@Persist
	@Property
	private UploadedFile file;

	@InjectComponent
	private Zone formZone;

	@InjectComponent
	private Zone imageZone;

	@InjectContainer
	private Form form;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	ImageHandle image;

	@Property
	@Persist
	ArrayList<String> ballotImages;

	@Property
	String imagen;
	
	@Property
	@Persist
	private boolean errorExtension;

    @Inject
    private ComponentResources componentResources;

	public void setupRender()
	{
	
		ballotDao=DB4O.getBallotDao(datasession.getDBName());
		ballot=ballotDao.getById(ballotIdSesion);
		errorExtension = false;
		ajaxResponseRenderer.addRender("formZone", formZone);
	}

	public Object onSuccessFromForm(){
		if (file.getFileName().toLowerCase().endsWith(".jpg") || file.getFileName().toLowerCase().endsWith(".png") || file.getFileName().toLowerCase().endsWith(".gif")) {
			errorExtension = false;

			String pathname;
			image = new ImageHandle();
			pathname = image.saveImage(file);
			System.out.println("Función de sucess " + pathname);
			ballot.addImagen(pathname);
			ballotImages= ballot.getImagenes();
			ajaxResponseRenderer.addRender("formZone", formZone).addRender("imageZone", imageZone);
		}
		else {
			System.out.println("Error en la extension");
			errorExtension = true;
			ajaxResponseRenderer.addRender("formZone", formZone);
		}
		return true;
	}

	public Object onActionFromFinish()
	{
		System.out.println("Ballot images en addImages" + ballotImages);

		ballot.setImagenes(ballotImages);		
		ballotDao.updateBallot(ballot);
		componentResources.discardPersistentFieldChanges();
		return BallotWasCreated.class;
	}

	public Object onActionFromDelete(String imagen)
	{
		ballot.removeImagen(imagen);
		ajaxResponseRenderer.addRender("formZone", formZone).addRender("imageZone", imageZone);
		return true;
	}
}
