package dbUtils;

public interface CommonDAO<T, U> {
		
	public int addRecord(T addedObject);
	
	public int updateRecord(T modifiedObject);
	
	public int deleteRecord(T deletedObject);
	
	public U getIDSetByDB(T checkedObject);

}
