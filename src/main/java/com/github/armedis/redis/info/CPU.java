package com.github.armedis.redis.info;

import com.google.common.base.CaseFormat;

final class CPU {
	private double usedCpuSys;
	private double usedCpuUser;
	private double usedCpuSysChildren;
	private double usedCpuUserChildren;
	private double usedCpuSysMainThread;
	private double usedCpuUserMainThread;

	/**
	 * TODO from string에서 getter/Setter 찾는 로직 넣기.
	 * 
	 * @param content
	 * @return
	 */
	public static CPU convert(String content) {
		CPU cpu = new CPU();
		String[] lines = content.split("\r\n");

		for (String line : lines) {
			String[] parts = line.split(":");
			if (parts.length == 2) {
				String key = parts[0].trim();
				String value = parts[1].trim();

				key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);

				RedisInfoConverter.setField(cpu, key, value);
			}
		}

		return cpu;
	}

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