package com.github.armedis.redis.info;

final class CPU extends StatsBaseVo {
	private double usedCpuSys;
	private double usedCpuUser;
	private double usedCpuSysChildren;
	private double usedCpuUserChildren;
	private double usedCpuSysMainThread;
	private double usedCpuUserMainThread;

	/**
	 * @return the usedCpuSys
	 */
	public double getUsedCpuSys() {
		return usedCpuSys;
	}

	/**
	 * @param usedCpuSys the usedCpuSys to set
	 */
	public void setUsedCpuSys(double usedCpuSys) {
		this.usedCpuSys = usedCpuSys;
	}

	/**
	 * @return the usedCpuUser
	 */
	public double getUsedCpuUser() {
		return usedCpuUser;
	}

	/**
	 * @param usedCpuUser the usedCpuUser to set
	 */
	public void setUsedCpuUser(double usedCpuUser) {
		this.usedCpuUser = usedCpuUser;
	}

	/**
	 * @return the usedCpuSysChildren
	 */
	public double getUsedCpuSysChildren() {
		return usedCpuSysChildren;
	}

	/**
	 * @param usedCpuSysChildren the usedCpuSysChildren to set
	 */
	public void setUsedCpuSysChildren(double usedCpuSysChildren) {
		this.usedCpuSysChildren = usedCpuSysChildren;
	}

	/**
	 * @return the usedCpuUserChildren
	 */
	public double getUsedCpuUserChildren() {
		return usedCpuUserChildren;
	}

	/**
	 * @param usedCpuUserChildren the usedCpuUserChildren to set
	 */
	public void setUsedCpuUserChildren(double usedCpuUserChildren) {
		this.usedCpuUserChildren = usedCpuUserChildren;
	}

	/**
	 * @return the usedCpuSysMainThread
	 */
	public double getUsedCpuSysMainThread() {
		return usedCpuSysMainThread;
	}

	/**
	 * @param usedCpuSysMainThread the usedCpuSysMainThread to set
	 */
	public void setUsedCpuSysMainThread(double usedCpuSysMainThread) {
		this.usedCpuSysMainThread = usedCpuSysMainThread;
	}

	/**
	 * @return the usedCpuUserMainThread
	 */
	public double getUsedCpuUserMainThread() {
		return usedCpuUserMainThread;
	}

	/**
	 * @param usedCpuUserMainThread the usedCpuUserMainThread to set
	 */
	public void setUsedCpuUserMainThread(double usedCpuUserMainThread) {
		this.usedCpuUserMainThread = usedCpuUserMainThread;
	}

}