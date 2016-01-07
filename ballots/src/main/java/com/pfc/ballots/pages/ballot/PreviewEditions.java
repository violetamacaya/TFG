package com.pfc.ballots.pages.ballot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;

import com.pfc.ballots.dao.EditLogDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.EditLog;
/**
 * 
 * PreviewEdition class is the controller for the page that
 * allow to see changes 
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2015
 */

public class PreviewEditions {
	@SessionState
	private DataSession datasession;

	@SessionAttribute
	private String ballotIdSesion;

	@Persist
	@Property
	private EditLog editLog;
	
	@Persist
	EditLogDao editLogDao;
	
	@Persist
	@Property
	List<EditLog> changes;
	
	@Property
	EditLog tabla;
		
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	
	public void setupRender()
	{
	
		changes = new LinkedList<EditLog>();
		editLogDao=DB4O.getEditLogDao(datasession.getDBName());
		changes=editLogDao.retrieve(ballotIdSesion);
	}
	
	public String getEditDate()
	{
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		System.out.println("Hay "+editLog.getEditDate());
		return format.format(editLog.getEditDate());
	}
	public String getNewData(){
		String data = editLog.getNewData();
		data = data.replaceAll(";", "<br />");	
		data = data.replaceAll("name", "Nombre");
		data = data.replaceAll("description", "Descripción");
		data = data.replaceAll("startDate", "Fecha de inicio");
		data = data.replaceAll("endDate", "Fecha de fin");
		data = data.replaceAll("imagenes", "Imágenes");
		data = data.replaceAll("options", "Opciones");
		data = data.replaceAll("categories", "Categorías");
		data = data.replaceAll("null", "Vacío");
		
		return data;
	}
}
