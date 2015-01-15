package com.pfc.ballots.dao;

/**
 * Extends from FactoryDao and creates ballots for DB4O database
 * 
 * @author Mario Temprnano Martin
 * @version 2.0 FEB-2014
 * @author Violeta Macaya Sánchez
 * @version	3.0 ENE-2015
 *
 */

public class DB4OFactoryDao extends FactoryDao{

	

	@Override
	public UserDao getUsuarioDao() 
	{
		return new UserDaoDB4O(null);
	}
	
	@Override
	public UserDao getUsuarioDao(String DBName)
	{
		return new UserDaoDB4O(DBName);
	}
	
	
	@Override
	public LogDao getLogDao() 
	{
		return new LogDaoDB4O();
	}

	

	@Override
	public CompanyDao getCompanyDao() 
	{
		return new CompanyDaoDB4O();
	}

	@Override
	public UserLogedDao getUserLogedDao() 
	{
		return new UserLogedDaoDB4O(null);
	}

	@Override
	public UserLogedDao getUserLogedDao(String DBName) 
	{
		return new UserLogedDaoDB4O(DBName);
	}

	@Override
	public CensusDao getCensusDao() 
	{
		return new CensusDaoDB4O(null);
	}

	@Override
	public CensusDao getCensusDao(String DBName) 
	{
		return new CensusDaoDB4O(DBName);
	}
	
	@Override 
	public EmailAccountDao getEmailAccountDao()
	{
		return new EmailAccountDaoDB4O(null);
	}
	@Override 
	public EmailAccountDao getEmailAccountDao(String DBName)
	{
		return new EmailAccountDaoDB4O(DBName);
	}

	@Override
	public BallotDao getBallotDao() {
		
		return new BallotDaoDB4O(null);
	}

	@Override
	public BallotDao getBallotDao(String DBName) {
		
		return new BallotDaoDB4O(DBName);
	}

	@Override
	public VoteDao getVoteDao()
	{
		return new VoteDaoDB4O(null);
	}

	@Override
	public VoteDao getVoteDao(String DBName) 
	{
		return new VoteDaoDB4O(DBName);
	}

	@Override
	public RelativeMajorityDao getRelativeMajorityDao() 
	{	
		return new RelativeMajorityDaoDB4O(null);
	}

	@Override
	public RelativeMajorityDao getRelativeMajorityDao(String DBName) 
	{	
		return new RelativeMajorityDaoDB4O(DBName);
	}

	@Override
	public KemenyDao getKemenyDao() {
		
		return new KemenyDaoDB4O(null);
	}

	@Override
	public KemenyDao getKemenyDao(String DBName) {
		
		return new KemenyDaoDB4O(DBName);
	}

	@Override
	public ProfileCensedInDao getProfileCensedInDao() {
		
		return  new ProfileCensedInDaoDB4O(null);
	}

	@Override
	public ProfileCensedInDao getProfileCensedInDao(String DBName) {
		return new ProfileCensedInDaoDB4O(DBName);
	}
	
	@Override
	public BordaDao getBordaDao()
	{
		return new BordaDaoDB4O(null);
	}
	@Override
	public BordaDao getBordaDao(String DBName)
	{
		return new BordaDaoDB4O(DBName);
	}
	
	@Override
	public RangeVotingDao getRangeVotingDao()
	{
		return new RangeVotingDaoDB4O(null);
	}
	@Override
	public RangeVotingDao getRangeVotingDao(String DBName)
	{
		return new RangeVotingDaoDB4O(DBName);
	}

	@Override
	public AboutTextDao getAboutTextDao()
	{
		return new AboutTextDaoDB4O();
	}
	@Override
	public BordaDao getBordaTextDao()
	{
		return new BordaDaoDB4O(null);
	}
	@Override
	public KemenyDao getKemenyTextDao()
	{
		return new KemenyDaoDB4O(null);
	}
	@Override
	public MajoryDao getMajoryTextDao()
	{
		return new MajoryDaoDB4O(null);
	}
	@Override
	public RangeVotingDao getRangeVotingTextDao()
	{
		return new RangeVotingDaoDB4O(null);
	}
	@Override
	public ApprovalVotingDao getApprovalVotingTextDao()
	{
		return new ApprovalVotingDaoDB4O(null);
	}
	@Override
	public BlackDao getBlackTextDao()
	{
		return new BlackDaoDB4O(null);
	} 	 
	@Override
	public BramsDao getBramsTextDao()
	{
		return new BramsDaoDB4O(null);
	} 	 
	@Override
	public BucklinDao getBucklinTextDao()
	{
		return new BucklinDaoDB4O(null);
	} 	
	@Override
	public CondorcetDao getCondorcetTextDao()
	{
		return new CondorcetDaoDB4O(null);
	} 	
	@Override
	public CoombsDao getCoombsTextDao()
	{
		return new CoombsDaoDB4O(null);
	} 	
	@Override
	public CopelandDao getCopelandTextDao()
	{
		return new CopelandDaoDB4O(null);
	} 
	
	@Override
	public SchulzeDao getSchulzeTextDao()
	{
		return new SchulzeDaoDB4O(null);
	} 
	@Override
	public SmallDao getSmallTextDao()
	{
		return new SmallDaoDB4O(null);
	} 
	@Override
	public NansonDao getNansonTextDao()
	{
		return new NansonDaoDB4O(null);
	} 
	@Override
	public MejorPeorDao getMejorPeorTextDao()
	{
		return new MejorPeorDaoDB4O(null);
	} 
	@Override
	public JuicioMayoritarioDao getJuicioMayoritarioTextDao()
	{
		return new JuicioMayoritarioDaoDB4O(null);
	} 
	@Override
	public HareDao getHareTextDao()
	{
		return new HareDaoDB4O(null);
	} 
	@Override
	public VotoAcumulativoDao getVotoAcumulativoTextDao()
	{
		return new VotoAcumulativoDaoDB4O(null);
	} 
	@Override
	public DodgsonDao getDodgsonTextDao()
	{
		return new DodgsonDaoDB4O(null);
	} 

}
