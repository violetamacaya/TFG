package com.pfc.ballots.dao;

/**
 * Extends from FactoryDao and creates ballots for DB4O database
 * 
 * @author Mario Temprnano Martin
 * @version 2.0 FEB-2014
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
		// TODO Auto-generated method stub
		return new ProfileCensedInDaoDB4O(DBName);
	}
	
	


	 
	
	 

}
