package com.pfc.ballots.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * Generates random votes for the ballots
 * 
 * @author Mario Temprano Martin
 * @version JUL-2014
 * @author Violeta Macaya Sánchez
 * @version OCT-2015
 * 
 */
public class GenerateDocentVotes {

	/**
	 * Generates a list of random votes for a relative majority ballot
	 * 
	 * @param options options of the ballot
	 * @param numVotes number of votes to generate
	 * @return List<String> a list of random votes
	 */
	static public List<String> generateRelativeMajority(List<String> options,int numVotes)
	{
		List<String> votes=new LinkedList<String>();
		for(int i=0;i<numVotes;i++)
		{
			Random random=new Random();
			votes.add(options.get(random.nextInt(options.size())));
		}

		return votes;
	}
	/**
	 * Generates a list of random votes for a kemeny ballot
	 * @param kemenyOptions lists of options of the ballots
	 * @param numVotes number of votes to generate
	 * @return List<List<String>> list of random votes (a vote is a list)
	 */
	static public List<List<String>> generateKemeny(List<String> kemenyOptions,int numVotes)
	{
		Map<String,Integer> votesPerOption=new HashMap<String,Integer>();
		List<List<String>>votes=new LinkedList<List<String>>();

		for(String option:kemenyOptions)
		{
			votesPerOption.put(option, 0);
		}

		for(int i=1; i<=numVotes;i++)
		{
			List<String> vote=new LinkedList<String>();
			for(int x=0;x<kemenyOptions.size();x++)
			{
				boolean notSelected=true;
				while(notSelected)
				{
					Random random=new Random();
					int numOption=random.nextInt(kemenyOptions.size());
					if(votesPerOption.get(kemenyOptions.get(numOption))<i)
					{
						notSelected=false;
						vote.add(kemenyOptions.get(numOption));
						votesPerOption.put(kemenyOptions.get(numOption), i);
					}
				}
			}
			votes.add(vote);
		}

		return votes;

	}
	/**
	 * Generates a list of random votes for a borda ballot
	 * 
	 * @param bordaOptions options of the ballot
	 * @param numVotes number of votes to generate
	 * @return List<String> a list of random votes
	 */

	static public List<List<String>> generateBorda(List<String> bordaOptions,int numVotes)
	{
		Map<String,Integer> votesPerOption=new HashMap<String,Integer>();
		List<List<String>>votes=new LinkedList<List<String>>();

		for(String option:bordaOptions)
		{
			votesPerOption.put(option, 0);
		}

		for(int i=1; i<=numVotes;i++)
		{
			List<String> vote=new LinkedList<String>();
			for(int x=0;x<bordaOptions.size();x++)
			{
				boolean notSelected=true;
				while(notSelected)
				{
					Random random=new Random();
					int numOption=random.nextInt(bordaOptions.size());
					if(votesPerOption.get(bordaOptions.get(numOption))<i)
					{
						notSelected=false;
						vote.add(bordaOptions.get(numOption));
						votesPerOption.put(bordaOptions.get(numOption), i);
					}
				}
			}
			votes.add(vote);
		}

		return votes;
	}



	/**
	 * Generates a list of random votes for a RangeVoting ballot
	 * 
	 * @param options options of the ballot
	 * @param numVotes number of votes to generate
	 * @param min minimum value
	 * @param max maximum value
	 * @return List<String> a list of random votes
	 */
	static public List<List<String>> generateRangeVoting(List<String>options, int numVotes,int min,int max)
	{

		List<List<String>>votes=new LinkedList<List<String>>();


		for(int x=0;x<numVotes;x++)
		{
			List<String>vote=new LinkedList<String>();
			for(@SuppressWarnings("unused") String option:options)
			{
				Random random=new Random();
				int valor=random.nextInt(max-min+1)+min;
				vote.add(String.valueOf(valor));				
			}
			votes.add(vote);
		}
		return votes;

	}

	/**
	 * Generates a list of random votes for a Approval voting ballot
	 * 
	 * @param options options of the ballot
	 * @param numVotes number of votes to generate
	 * @return List<String> a list of random votes
	 */
	static public List<String> generateApprovalVoting(List<String> options,int numVotes)
	{
		List<String> votes=new LinkedList<String>();

		//Cada uno de los usuarios puede votar el numero de opciones que quiera, entre una y todas. 
		for(int i=0; i<numVotes; i++){
			int[] votospersona = new int[options.size()];
			int numuservotes = 1 + (int)(Math.random() * ((options.size() - 1) + 1));
			for (int j = 0; j<numuservotes; j++){

				int posvoto = 0 + (int)(Math.random() * ((votospersona.length - 1) + 1));
				if(votospersona[posvoto] == 0){
					votospersona[posvoto] = 1;
					votes.add(options.get(posvoto));
				}
				else {
					j--;
				}
			}	
		}

		return votes;
	}
	/**
	 * Generates a list of random votes for a Brams ballot
	 * 
	 * @param options options of the ballot
	 * @param numVotes number of votes to generate
	 * @return List<String> a list of random votes
	 */
	static public List<List<String>> generateBrams(List<String> options, int numVotes) {
		List<List<String>> votes=new LinkedList<List<String>>();

		//Cada votante podrá dejar de votar 3 opciones (o más si quiere). En las que vote les asignará una puntuación entre 1 y 10, las que no, tendrán 0. 
		for(int i=0; i<numVotes; i++){
			List<String> votospersona = new LinkedList<String>(); //Array que almacena los votos de una persona.
			int numuservotes = 1 + (int)(Math.random() * ((options.size() - 4) + 1));

			for (int k = 0; k<options.size(); k++){
				votospersona.add("0"); //Inicialmente, todo el array a 0, para solo cambiar en los que haya votado la persona.
			}
			for (int j = 0; j<numuservotes; j++){

				int posvoto = 0 + (int)(Math.random() * ((votospersona.size() - 1) + 1));
				if(Integer.parseInt(votospersona.get(posvoto)) == 0){
					int valorvoto= (int)Math.random()*(10-1)+1;
					System.out.println("El valor aleatorio generado es: "+valorvoto);
					votospersona.add(posvoto, String.valueOf(valorvoto));
				}
				else {
					j--;
				}
			}	
			votes.add(votospersona);
		}

		return votes;
	}
	static public List<String> generateVotoAcumulativo(List<String> options, int numVotes) {
		List<String> votes=new LinkedList<String>();

		//Cada votante tiene 5 votos que podrá repartir como quiera. 
		for(int i=0; i<numVotes; i++){
			int[] votospersona = new int[options.size()]; //Array que almacena los votos de una persona.

			for (int j = 0; j<5; j++){
				int posvoto = 0 + (int)(Math.random() * ((votospersona.length - 1) + 1));
				votes.add(options.get(posvoto));
			}	
		}

		return votes;
	}
	static public List<List<String>> generateJuicioMayoritario(List<String> options, int numVotes) {
		List<List<String>> votes=new LinkedList<List<String>>();

		//Cada votante tiene que votar todas las opciones entre 1-5 
		for(int i=0; i<numVotes; i++){
			List<String> voto = new LinkedList<String>();
			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				Integer numVotos = 1 + (int)(Math.random() * 5);
					voto.add(numVotos.toString());
			}	
			votes.add(voto);
		}
		System.out.println("los votos generados son: "+votes);
		return votes;
	}

	public static List<List<String>> generateMejorPeor(List<String> options, int numVotes) {
		List<List<String>> votes=new LinkedList<List<String>>();
		List<String> votosPos = new LinkedList<String>();
		List<String> votosNeg = new LinkedList<String>();

		//Cada votante tiene 2 votos (mejor-peor)
		for(int i=0; i<numVotes; i++){
			int posvoto = 0 + (int)(Math.random() * (options.size()-1));
			int negvoto = posvoto;
			while (negvoto == posvoto){
				negvoto = 0 + (int)(Math.random() * (options.size()-1));
			}			
			votosPos.add(options.get(posvoto));
			votosNeg.add(options.get(negvoto));
		}
		votes.add(votosPos);
		votes.add(votosNeg);
		return votes;
	}


	static public List<List<String>> generateCondorcet(List<String> options,int numVotes)
	{ 
		//Cada votante le asigna una preferencia entre 0 y numOpciones (pudiendo repetirse) a cada opción.
		List<List<String>>votes=new LinkedList<List<String>>();

		for(int i=0; i<numVotes;i++)
		{
			List<String> vote = new LinkedList<String>();
			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				Integer prioridad = (int)(Math.random() * options.size());	
				vote.add(prioridad.toString());
			}
			votes.add(vote);

		}

		return votes;
	}
	static public List<List<String>> generateBlack(List<String> options,int numVotes)
	{ 
		//Cada votante le asigna una preferencia entre 0 y numOpciones (pudiendo repetirse) a cada opción.
		List<List<String>>votes=new LinkedList<List<String>>();

		for(int i=0; i<numVotes;i++)
		{
			List<String> vote = new LinkedList<String>();
			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				Integer prioridad = (int)(Math.random() * options.size());	
				vote.add(prioridad.toString());
			}
			votes.add(vote);

		}

		return votes;
	}
	static public List<List<String>> generateDodgson(List<String> options,int numVotes)
	{ 
		//Cada votante le asigna una preferencia entre 0 y numOpciones (pudiendo repetirse) a cada opción.
		List<List<String>>votes=new LinkedList<List<String>>();

		for(int i=0; i<numVotes;i++)
		{
			List<String> vote = new LinkedList<String>();
			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				Integer prioridad = (int)(Math.random() * options.size());	
				vote.add(prioridad.toString());
			}
			votes.add(vote);

		}

		return votes;
	}
	static public List<List<String>> generateCopeland(List<String> options,int numVotes)
	{ 
		//Cada votante le asigna una preferencia entre 0 y numOpciones (pudiendo repetirse) a cada opción.
		List<List<String>>votes=new LinkedList<List<String>>();

		for(int i=0; i<numVotes;i++)
		{
			List<String> vote = new LinkedList<String>();
			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				Integer prioridad = (int)(Math.random() * options.size());	
				vote.add(prioridad.toString());
			}
			votes.add(vote);

		}

		return votes;
	}
	static public List<List<String>> generateSchulze(List<String> options,int numVotes)
	{ 
		//Cada votante le asigna una preferencia entre 0 y numOpciones (pudiendo repetirse) a cada opción.
		List<List<String>>votes=new LinkedList<List<String>>();

		for(int i=0; i<numVotes;i++)
		{
			List<String> vote = new LinkedList<String>();
			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				Integer prioridad = (int)(Math.random() * options.size());	
				vote.add(prioridad.toString());
			}
			votes.add(vote);

		}

		return votes;
	}
	static public List<List<String>> generateSmall(List<String> options,int numVotes)
	{ 
		//Cada votante le asigna una preferencia entre 0 y numOpciones (pudiendo repetirse) a cada opción.
		List<List<String>>votes=new LinkedList<List<String>>();

		for(int i=0; i<numVotes;i++)
		{
			List<String> vote = new LinkedList<String>();
			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				Integer prioridad = (int)(Math.random() * options.size());	
				vote.add(prioridad.toString());
			}
			votes.add(vote);

		}

		return votes;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public List<List<String>> generateBucklin(List<String> options,int numVotes)
	{ 
		//Cada votante le asigna una preferencia entre 0 y numOpciones a cada opción.
		List<List<String>>votes=new LinkedList<List<String>>();
		boolean opcionAsignada = false;
		for(int i=0; i<numVotes;i++)
		{
			List<String> vote = new LinkedList<String>();
			ArrayList numerosAsignados = new ArrayList();

			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				opcionAsignada=false;
				while(!opcionAsignada){
					Integer prioridad = (int)(Math.random() * (options.size())+1);

					if (numerosAsignados.isEmpty()){
						numerosAsignados.add(prioridad);
						vote.add(options.get(prioridad-1));
						opcionAsignada = true;
						//Añadimos el número aleatorio a una lista para poder comprobar cuáles han salido ya
					}
					else{
						if (!numerosAsignados.contains(prioridad)){
							numerosAsignados.add(prioridad);
							vote.add(options.get(prioridad -1));
							opcionAsignada = true;
						}
					}
				}
			}
			votes.add(vote);
		}
		return votes;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public List<List<String>> generateCoombs(List<String> options,int numVotes)
	{ 
		//Cada votante le asigna una preferencia entre 0 y numOpciones a cada opción.
		List<List<String>>votes=new LinkedList<List<String>>();
		boolean opcionAsignada = false;
		for(int i=0; i<numVotes;i++)
		{
			List<String> vote = new LinkedList<String>();
			ArrayList numerosAsignados = new ArrayList();

			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				opcionAsignada=false;
				while(!opcionAsignada){
					Integer prioridad = (int)(Math.random() * (options.size())+1);

					if (numerosAsignados.isEmpty()){
						numerosAsignados.add(prioridad);
						vote.add(options.get(prioridad-1));
						opcionAsignada = true;
						//Añadimos el número aleatorio a una lista para poder comprobar cuáles han salido ya
					}
					else{
						if (!numerosAsignados.contains(prioridad)){
							numerosAsignados.add(prioridad);
							vote.add(options.get(prioridad -1));
							opcionAsignada = true;
						}
					}
				}
			}
			votes.add(vote);
		}
		return votes;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public List<List<String>> generateHare(List<String> options,int numVotes)
	{ 
		//Cada votante le asigna una preferencia entre 0 y numOpciones a cada opción.
		List<List<String>>votes=new LinkedList<List<String>>();
		boolean opcionAsignada = false;
		for(int i=0; i<numVotes;i++)
		{
			List<String> vote = new LinkedList<String>();
			ArrayList numerosAsignados = new ArrayList();

			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				opcionAsignada=false;
				while(!opcionAsignada){
					Integer prioridad = (int)(Math.random() * (options.size())+1);

					if (numerosAsignados.isEmpty()){
						numerosAsignados.add(prioridad);
						vote.add(options.get(prioridad-1));
						opcionAsignada = true;
						//Añadimos el número aleatorio a una lista para poder comprobar cuáles han salido ya
					}
					else{
						if (!numerosAsignados.contains(prioridad)){
							numerosAsignados.add(prioridad);
							vote.add(options.get(prioridad -1));
							opcionAsignada = true;
						}
					}
				}
			}
			votes.add(vote);
		}
		return votes;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public List<List<String>> generateNanson(List<String> options,int numVotes)
	{ 
		//Cada votante le asigna una preferencia entre 0 y numOpciones a cada opción.
		List<List<String>>votes=new LinkedList<List<String>>();
		boolean opcionAsignada = false;
		for(int i=0; i<numVotes;i++)
		{
			List<String> vote = new LinkedList<String>();
			ArrayList numerosAsignados = new ArrayList();

			for (int j = 0; j<options.size(); j++){ //Para cada una de las opciones, hara el random
				opcionAsignada=false;
				while(!opcionAsignada){
					Integer prioridad = (int)(Math.random() * (options.size())+1);

					if (numerosAsignados.isEmpty()){
						numerosAsignados.add(prioridad);
						vote.add(options.get(prioridad-1));
						opcionAsignada = true;
						//Añadimos el número aleatorio a una lista para poder comprobar cuáles han salido ya
					}
					else{
						if (!numerosAsignados.contains(prioridad)){
							numerosAsignados.add(prioridad);
							vote.add(options.get(prioridad -1));
							opcionAsignada = true;
						}
					}
				}
			}
			votes.add(vote);
		}
		return votes;
	}
}
