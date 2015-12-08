package com.pfc.ballots.dao;


import com.pfc.ballots.dao.DB4OFactoryDao;
import com.pfc.ballots.dao.UserDao;

/**
 * FactoryDao Creates an abstract factory for databases
 * 
 * @version 2.0, FEB-2014
 * @author Mario Temprano Martin
 * @version 3.0 OCT-2015
 * @author Violeta Macaya Sánchez
 */
public abstract class FactoryDao {

	
	public static final int DB4O_FACTORY = 1;
		
	public abstract UserDao getUsuarioDao();
	public abstract UserDao getUsuarioDao(String DBName);
	
	public abstract UserLogedDao getUserLogedDao();
	public abstract UserLogedDao getUserLogedDao(String DBName);
	
	public abstract LogDao getLogDao();//in DB4O has its own DB
	
	public abstract ProfileCensedInDao getProfileCensedInDao();
	public abstract ProfileCensedInDao getProfileCensedInDao(String DBName);
	
	public abstract CensusDao getCensusDao();
	public abstract CensusDao getCensusDao(String DBName);
	
	public abstract CompanyDao getCompanyDao();//only can be in main DB in DB4O
	
	public abstract EmailAccountDao getEmailAccountDao();
	public abstract EmailAccountDao getEmailAccountDao(String DBName);
	
	public abstract BallotDao getBallotDao();
	public abstract BallotDao getBallotDao(String DBName);
	
	public abstract VoteDao getVoteDao();
	public abstract VoteDao getVoteDao(String DBName);
	
	////BALLOT DATA
	public abstract RelativeMajorityDao getRelativeMajorityDao();
	public abstract RelativeMajorityDao getRelativeMajorityDao(String DBName);
	
	public abstract KemenyDao getKemenyDao();
	public abstract KemenyDao getKemenyDao(String DBName);
	
	public abstract BordaDao getBordaDao();
	public abstract BordaDao getBordaDao(String DBName);
	
	public abstract RangeVotingDao getRangeVotingDao();
	public abstract RangeVotingDao getRangeVotingDao(String DBName);
	
	public abstract ApprovalVotingDao getApprovalVotingDao();
	public abstract ApprovalVotingDao getApprovalVotingDao(String DBName);
	
	public abstract BlackDao getBlackDao();
	public abstract BlackDao getBlackDao(String DBName);
	
	public abstract BramsDao getBramsDao();
	public abstract BramsDao getBramsDao(String DBName);
	
	public abstract BucklinDao getBucklinDao();
	public abstract BucklinDao getBucklinDao(String DBName);	
	
	public abstract CondorcetDao getCondorcetDao();
	public abstract CondorcetDao getCondorcetDao(String DBName);
	
	public abstract CoombsDao getCoombsDao();
	public abstract CoombsDao getCoombsDao(String DBName);
	
	public abstract CopelandDao getCopelandDao();
	public abstract CopelandDao getCopelandDao(String DBName);
	
	public abstract DodgsonDao getDodgsonDao();
	public abstract DodgsonDao getDodgsonDao(String DBName);
	
	public abstract HareDao getHareDao();
	public abstract HareDao getHareDao(String DBName);
	
	public abstract JuicioMayoritarioDao getJuicioMayoritarioDao();
	public abstract JuicioMayoritarioDao getJuicioMayoritarioDao(String DBName);
	
	public abstract MejorPeorDao getMejorPeorDao();
	public abstract MejorPeorDao getMejorPeorDao(String DBName);
	
	public abstract NansonDao getNansonDao();
	public abstract NansonDao getNansonDao(String DBName);	
	
	public abstract SchulzeDao getSchulzeDao();
	public abstract SchulzeDao getSchulzeDao(String DBName);
	
	public abstract SmallDao getSmallDao();
	public abstract SmallDao getSmallDao(String DBName);
	
	public abstract VotoAcumulativoDao getVotoAcumulativoDao();
	public abstract VotoAcumulativoDao getVotoAcumulativoDao(String DBName);

	
	
	
	
	public abstract AboutTextDao getAboutTextDao();
	
	
	public static FactoryDao getFactory(int claveFactory) {
		
		switch (claveFactory) {
	
			case DB4O_FACTORY:
			
				return new DB4OFactoryDao();
				
			//any new kind of db would go here, for example
			//case SQL_FACTORY (would be number 2 up there)

			default:
			
				throw new IllegalArgumentException();
		}
	}
	public abstract BordaDao getBordaTextDao();
	public abstract KemenyDao getKemenyTextDao();
	public abstract RelativeMajorityDao getRelativeMajorityTextDao();
	public abstract ApprovalVotingDao getApprovalVotingTextDao();
	public abstract RangeVotingDao getRangeVotingTextDao();
	public abstract BlackDao getBlackTextDao();
	public abstract BramsDao getBramsTextDao();
	public abstract BucklinDao getBucklinTextDao();
	public abstract CondorcetDao getCondorcetTextDao();
	public abstract CoombsDao getCoombsTextDao();
	public abstract CopelandDao getCopelandTextDao();
	public abstract DodgsonDao getDodgsonTextDao();
	public abstract HareDao getHareTextDao();
	public abstract JuicioMayoritarioDao getJuicioMayoritarioTextDao();
	public abstract MejorPeorDao getMejorPeorTextDao();
	public abstract NansonDao getNansonTextDao();
	public abstract SchulzeDao getSchulzeTextDao();
	public abstract SmallDao getSmallTextDao();
	public abstract VotoAcumulativoDao getVotoAcumulativoTextDao();
	
	
}
