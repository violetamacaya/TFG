/**
 * 
 */
package com.pfc.ballots.pages.Methods;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.AboutText;
import com.pfc.ballots.entities.ballotdata.Borda;
import com.pfc.ballots.dao.*;

/**
 * @author violeta
 *
 */
public class BordaMethod {


	@SessionState
	private DataSession datasession;


	@InjectComponent
	private Zone editZone;
	@InjectComponent
	private Zone methodZone;
	
	@Persist
	@Property
	private boolean showText;
	
	@Persist 
	BordaDao BordaDao;
	
	@Persist
	@Property
	private String BordaText;
	@Persist
	@Property
	private String BordaText2;
	
	
	
	@Persist
	@Property
	private String head;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	public void setupRender()
	{
		BordaText= "A cada votante se le pide que exprese un orden completo de preferencias,jerarquizando la alternativa más preferida (que se sitúa en primer lugar), a la menos preferida,(que se colca en último lugar)."+
	"Dentro de cada orden individual de preferencias, se adjudica un puntaje a las alternativas situadas en cada nivel dentro del orden. "+
				"La alternativa situada en el nivel más alto, recibe el puntaje N-1 (es decir, el número de alternativas jerarquizadas, menos uno). "+

					"Si existen cuatro alternativas, la alternativa de la primera preferencia recibirá un puntaje de 3. La alternativa situada en segundo nivel recibirá un puntaje equivalente a N-2, y así hasta la última alternativa, que recibirá el puntaje N-N, esto es, un puntaje de 0."+ 
					"Se pasan a sumar los puntajes que reciben las alternativas en cada orden individual. "+
					"La opción ganadora es aquella que obtiene el mayor puntaje, que es la ganadora Borda. "+
					"En caso de empate entre dos alternativas ganadoras, se escoge una de las dos alternativas al azar.";
		BordaText2="Asdasd";
	}
	
	public boolean isTeacher()
	{
		return datasession.isTeacher();
	}
	public boolean isAdmin()
	{
		return datasession.isAdmin();
	}
	
	public Object onSuccessFromEditForm()
	{
		if(request.isXHR())
		{	
			Borda temp=BordaDao.getBordaText();
			if(temp!=null)
			{
				BordaText=temp.getBordaText();
				head=temp.getBordaHead();
			}
			
			temp.setBordaText(BordaText);
			temp.setBordaHead(head);

			return BordaMethod.class;
		}
		return null;
	}

	
}