package com.github.armedis.redis.info;

public final class Persistence extends StatsBaseVo {
	private int loading;
	private long currentCowSize;
	private int currentCowSizeAge;
	private double currentForkPerc;
	private long currentSaveKeysProcessed;
	private long currentSaveKeysTotal;
	private long rdbChangesSinceLastSave;
	private int rdbBgsaveInProgress;
	private long rdbLastSaveTime;
	private String rdbLastBgsaveStatus;
	private long rdbLastBgsaveTimeSec;
	private long rdbCurrentBgsaveTimeSec;
	private long rdbLastCowSize;
	private int aofEnabled;
	private int aofRewriteInProgress;
	private int aofRewriteScheduled;
	private long aofLastRewriteTimeSec;
	private long aofCurrentRewriteTimeSec;
	private String aofLastBgrewriteStatus;
	private String aofLastWriteStatus;
	private long aofLastCowSize;
	private int moduleForkInProgress;
	private long moduleForkLastCowSize;

	/**
	 * @return the loading
	 */
	public int getLoading() {
		return loading;
	}

	/**
	 * @param loading the loading to set
	 */
	public void setLoading(int loading) {
		this.loading = loading;
	}

	/**
	 * @return the currentCowSize
	 */
	public long getCurrentCowSize() {
		return currentCowSize;
	}

	/**
	 * @param currentCowSize the currentCowSize to set
	 */
	public void setCurrentCowSize(long currentCowSize) {
		this.currentCowSize = currentCowSize;
	}

	/**
	 * @return the currentCowSizeAge
	 */
	public int getCurrentCowSizeAge() {
		return currentCowSizeAge;
	}

	/**
	 * @param currentCowSizeAge the currentCowSizeAge to set
	 */
	public void setCurrentCowSizeAge(int currentCowSizeAge) {
		this.currentCowSizeAge = currentCowSizeAge;
	}

	/**
	 * @return the currentForkPerc
	 */
	public double getCurrentForkPerc() {
		return currentForkPerc;
	}

	/**
	 * @param currentForkPerc the currentForkPerc to set
	 */
	public void setCurrentForkPerc(double currentForkPerc) {
		this.currentForkPerc = currentForkPerc;
	}

	/**
	 * @return the currentSaveKeysProcessed
	 */
	public long getCurrentSaveKeysProcessed() {
		return currentSaveKeysProcessed;
	}

	/**
	 * @param currentSaveKeysProcessed the currentSaveKeysProcessed to set
	 */
	public void setCurrentSaveKeysProcessed(long currentSaveKeysProcessed) {
		this.currentSaveKeysProcessed = currentSaveKeysProcessed;
	}

	/**
	 * @return the currentSaveKeysTotal
	 */
	public long getCurrentSaveKeysTotal() {
		return currentSaveKeysTotal;
	}

	/**
	 * @param currentSaveKeysTotal the currentSaveKeysTotal to set
	 */
	public void setCurrentSaveKeysTotal(long currentSaveKeysTotal) {
		this.currentSaveKeysTotal = currentSaveKeysTotal;
	}

	/**
	 * @return the rdbChangesSinceLastSave
	 */
	public long getRdbChangesSinceLastSave() {
		return rdbChangesSinceLastSave;
	}

	/**
	 * @param rdbChangesSinceLastSave the rdbChangesSinceLastSave to set
	 */
	public void setRdbChangesSinceLastSave(long rdbChangesSinceLastSave) {
		this.rdbChangesSinceLastSave = rdbChangesSinceLastSave;
	}

	/**
	 * @return the rdbBgsaveInProgress
	 */
	public int getRdbBgsaveInProgress() {
		return rdbBgsaveInProgress;
	}

	/**
	 * @param rdbBgsaveInProgress the rdbBgsaveInProgress to set
	 */
	public void setRdbBgsaveInProgress(int rdbBgsaveInProgress) {
		this.rdbBgsaveInProgress = rdbBgsaveInProgress;
	}

	/**
	 * @return the rdbLastSaveTime
	 */
	public long getRdbLastSaveTime() {
		return rdbLastSaveTime;
	}

	/**
	 * @param rdbLastSaveTime the rdbLastSaveTime to set
	 */
	public void setRdbLastSaveTime(long rdbLastSaveTime) {
		this.rdbLastSaveTime = rdbLastSaveTime;
	}

	/**
	 * @return the rdbLastBgsaveStatus
	 */
	public String getRdbLastBgsaveStatus() {
		return rdbLastBgsaveStatus;
	}

	/**
	 * @param rdbLastBgsaveStatus the rdbLastBgsaveStatus to set
	 */
	public void setRdbLastBgsaveStatus(String rdbLastBgsaveStatus) {
		this.rdbLastBgsaveStatus = rdbLastBgsaveStatus;
	}

	/**
	 * @return the rdbLastBgsaveTimeSec
	 */
	public long getRdbLastBgsaveTimeSec() {
		return rdbLastBgsaveTimeSec;
	}

	/**
	 * @param rdbLastBgsaveTimeSec the rdbLastBgsaveTimeSec to set
	 */
	public void setRdbLastBgsaveTimeSec(long rdbLastBgsaveTimeSec) {
		this.rdbLastBgsaveTimeSec = rdbLastBgsaveTimeSec;
	}

	/**
	 * @return the rdbCurrentBgsaveTimeSec
	 */
	public long getRdbCurrentBgsaveTimeSec() {
		return rdbCurrentBgsaveTimeSec;
	}

	/**
	 * @param rdbCurrentBgsaveTimeSec the rdbCurrentBgsaveTimeSec to set
	 */
	public void setRdbCurrentBgsaveTimeSec(long rdbCurrentBgsaveTimeSec) {
		this.rdbCurrentBgsaveTimeSec = rdbCurrentBgsaveTimeSec;
	}

	/**
	 * @return the rdbLastCowSize
	 */
	public long getRdbLastCowSize() {
		return rdbLastCowSize;
	}

	/**
	 * @param rdbLastCowSize the rdbLastCowSize to set
	 */
	public void setRdbLastCowSize(long rdbLastCowSize) {
		this.rdbLastCowSize = rdbLastCowSize;
	}

	/**
	 * @return the aofEnabled
	 */
	public int getAofEnabled() {
		return aofEnabled;
	}

	/**
	 * @param aofEnabled the aofEnabled to set
	 */
	public void setAofEnabled(int aofEnabled) {
		this.aofEnabled = aofEnabled;
	}

	/**
	 * @return the aofRewriteInProgress
	 */
	public int getAofRewriteInProgress() {
		return aofRewriteInProgress;
	}

	/**
	 * @param aofRewriteInProgress the aofRewriteInProgress to set
	 */
	public void setAofRewriteInProgress(int aofRewriteInProgress) {
		this.aofRewriteInProgress = aofRewriteInProgress;
	}

	/**
	 * @return the aofRewriteScheduled
	 */
	public int getAofRewriteScheduled() {
		return aofRewriteScheduled;
	}

	/**
	 * @param aofRewriteScheduled the aofRewriteScheduled to set
	 */
	public void setAofRewriteScheduled(int aofRewriteScheduled) {
		this.aofRewriteScheduled = aofRewriteScheduled;
	}

	/**
	 * @return the aofLastRewriteTimeSec
	 */
	public long getAofLastRewriteTimeSec() {
		return aofLastRewriteTimeSec;
	}

	/**
	 * @param aofLastRewriteTimeSec the aofLastRewriteTimeSec to set
	 */
	public void setAofLastRewriteTimeSec(long aofLastRewriteTimeSec) {
		this.aofLastRewriteTimeSec = aofLastRewriteTimeSec;
	}

	/**
	 * @return the aofCurrentRewriteTimeSec
	 */
	public long getAofCurrentRewriteTimeSec() {
		return aofCurrentRewriteTimeSec;
	}

	/**
	 * @param aofCurrentRewriteTimeSec the aofCurrentRewriteTimeSec to set
	 */
	public void setAofCurrentRewriteTimeSec(long aofCurrentRewriteTimeSec) {
		this.aofCurrentRewriteTimeSec = aofCurrentRewriteTimeSec;
	}

	/**
	 * @return the aofLastBgrewriteStatus
	 */
	public String getAofLastBgrewriteStatus() {
		return aofLastBgrewriteStatus;
	}

	/**
	 * @param aofLastBgrewriteStatus the aofLastBgrewriteStatus to set
	 */
	public void setAofLastBgrewriteStatus(String aofLastBgrewriteStatus) {
		this.aofLastBgrewriteStatus = aofLastBgrewriteStatus;
	}

	/**
	 * @return the aofLastWriteStatus
	 */
	public String getAofLastWriteStatus() {
		return aofLastWriteStatus;
	}

	/**
	 * @param aofLastWriteStatus the aofLastWriteStatus to set
	 */
	public void setAofLastWriteStatus(String aofLastWriteStatus) {
		this.aofLastWriteStatus = aofLastWriteStatus;
	}

	/**
	 * @return the aofLastCowSize
	 */
	public long getAofLastCowSize() {
		return aofLastCowSize;
	}

	/**
	 * @param aofLastCowSize the aofLastCowSize to set
	 */
	public void setAofLastCowSize(long aofLastCowSize) {
		this.aofLastCowSize = aofLastCowSize;
	}

	/**
	 * @return the moduleForkInProgress
	 */
	public int getModuleForkInProgress() {
		return moduleForkInProgress;
	}

	/**
	 * @param moduleForkInProgress the moduleForkInProgress to set
	 */
	public void setModuleForkInProgress(int moduleForkInProgress) {
		this.moduleForkInProgress = moduleForkInProgress;
	}

	/**
	 * @return the moduleForkLastCowSize
	 */
	public long getModuleForkLastCowSize() {
		return moduleForkLastCowSize;
	}

	/**
	 * @param moduleForkLastCowSize the moduleForkLastCowSize to set
	 */
	public void setModuleForkLastCowSize(long moduleForkLastCowSize) {
		this.moduleForkLastCowSize = moduleForkLastCowSize;
	}
}