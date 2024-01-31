package com.github.armedis.redis.info;

import com.google.common.base.CaseFormat;

public final class Memory {
	private long usedMemory;
	private String usedMemoryHuman;
	private long usedMemoryRss;
	private String usedMemoryRssHuman;
	private long usedMemoryPeak;
	private String usedMemoryPeakHuman;
	private String usedMemoryPeakPerc;
	private long usedMemoryOverhead;
	private long usedMemoryStartup;
	private long usedMemoryDataset;
	private String usedMemoryDatasetPerc;
	private long allocatorAllocated;
	private long allocatorActive;
	private long allocatorResident;
	private long totalSystemMemory;
	private String totalSystemMemoryHuman;
	private long usedMemoryLua;
	private String usedMemoryLuaHuman;
	private long usedMemoryScripts;
	private String usedMemoryScriptsHuman;
	private int numberOfCachedScripts;
	private long maxmemory;
	private String maxmemoryHuman;
	private String maxmemoryPolicy;
	private double allocatorFragRatio;
	private long allocatorFragBytes;
	private double allocatorRssRatio;
	private long allocatorRssBytes;
	private double rssOverheadRatio;
	private long rssOverheadBytes;
	private double memFragmentationRatio;
	private long memFragmentationBytes;
	private long memNotCountedForEvict;
	private long memReplicationBacklog;
	private int memClientsSlaves;
	private int memClientsNormal;
	private long memAofBuffer;
	private String memAllocator;
	private int activeDefragRunning;
	private int lazyfreePendingObjects;
	private int lazyfreedObjects;


	/**
	 * @return the usedMemory
	 */
	public long getUsedMemory() {
		return usedMemory;
	}


	/**
	 * @param usedMemory the usedMemory to set
	 */
	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}


	/**
	 * @return the usedMemoryHuman
	 */
	public String getUsedMemoryHuman() {
		return usedMemoryHuman;
	}


	/**
	 * @param usedMemoryHuman the usedMemoryHuman to set
	 */
	public void setUsedMemoryHuman(String usedMemoryHuman) {
		this.usedMemoryHuman = usedMemoryHuman;
	}


	/**
	 * @return the usedMemoryRss
	 */
	public long getUsedMemoryRss() {
		return usedMemoryRss;
	}


	/**
	 * @param usedMemoryRss the usedMemoryRss to set
	 */
	public void setUsedMemoryRss(long usedMemoryRss) {
		this.usedMemoryRss = usedMemoryRss;
	}


	/**
	 * @return the usedMemoryRssHuman
	 */
	public String getUsedMemoryRssHuman() {
		return usedMemoryRssHuman;
	}


	/**
	 * @param usedMemoryRssHuman the usedMemoryRssHuman to set
	 */
	public void setUsedMemoryRssHuman(String usedMemoryRssHuman) {
		this.usedMemoryRssHuman = usedMemoryRssHuman;
	}


	/**
	 * @return the usedMemoryPeak
	 */
	public long getUsedMemoryPeak() {
		return usedMemoryPeak;
	}


	/**
	 * @param usedMemoryPeak the usedMemoryPeak to set
	 */
	public void setUsedMemoryPeak(long usedMemoryPeak) {
		this.usedMemoryPeak = usedMemoryPeak;
	}


	/**
	 * @return the usedMemoryPeakHuman
	 */
	public String getUsedMemoryPeakHuman() {
		return usedMemoryPeakHuman;
	}


	/**
	 * @param usedMemoryPeakHuman the usedMemoryPeakHuman to set
	 */
	public void setUsedMemoryPeakHuman(String usedMemoryPeakHuman) {
		this.usedMemoryPeakHuman = usedMemoryPeakHuman;
	}


	/**
	 * @return the usedMemoryPeakPerc
	 */
	public String getUsedMemoryPeakPerc() {
		return usedMemoryPeakPerc;
	}


	/**
	 * @param usedMemoryPeakPerc the usedMemoryPeakPerc to set
	 */
	public void setUsedMemoryPeakPerc(String usedMemoryPeakPerc) {
		this.usedMemoryPeakPerc = usedMemoryPeakPerc;
	}


	/**
	 * @return the usedMemoryOverhead
	 */
	public long getUsedMemoryOverhead() {
		return usedMemoryOverhead;
	}


	/**
	 * @param usedMemoryOverhead the usedMemoryOverhead to set
	 */
	public void setUsedMemoryOverhead(long usedMemoryOverhead) {
		this.usedMemoryOverhead = usedMemoryOverhead;
	}


	/**
	 * @return the usedMemoryStartup
	 */
	public long getUsedMemoryStartup() {
		return usedMemoryStartup;
	}


	/**
	 * @param usedMemoryStartup the usedMemoryStartup to set
	 */
	public void setUsedMemoryStartup(long usedMemoryStartup) {
		this.usedMemoryStartup = usedMemoryStartup;
	}


	/**
	 * @return the usedMemoryDataset
	 */
	public long getUsedMemoryDataset() {
		return usedMemoryDataset;
	}


	/**
	 * @param usedMemoryDataset the usedMemoryDataset to set
	 */
	public void setUsedMemoryDataset(long usedMemoryDataset) {
		this.usedMemoryDataset = usedMemoryDataset;
	}


	/**
	 * @return the usedMemoryDatasetPerc
	 */
	public String getUsedMemoryDatasetPerc() {
		return usedMemoryDatasetPerc;
	}


	/**
	 * @param usedMemoryDatasetPerc the usedMemoryDatasetPerc to set
	 */
	public void setUsedMemoryDatasetPerc(String usedMemoryDatasetPerc) {
		this.usedMemoryDatasetPerc = usedMemoryDatasetPerc;
	}


	/**
	 * @return the allocatorAllocated
	 */
	public long getAllocatorAllocated() {
		return allocatorAllocated;
	}


	/**
	 * @param allocatorAllocated the allocatorAllocated to set
	 */
	public void setAllocatorAllocated(long allocatorAllocated) {
		this.allocatorAllocated = allocatorAllocated;
	}


	/**
	 * @return the allocatorActive
	 */
	public long getAllocatorActive() {
		return allocatorActive;
	}


	/**
	 * @param allocatorActive the allocatorActive to set
	 */
	public void setAllocatorActive(long allocatorActive) {
		this.allocatorActive = allocatorActive;
	}


	/**
	 * @return the allocatorResident
	 */
	public long getAllocatorResident() {
		return allocatorResident;
	}


	/**
	 * @param allocatorResident the allocatorResident to set
	 */
	public void setAllocatorResident(long allocatorResident) {
		this.allocatorResident = allocatorResident;
	}


	/**
	 * @return the totalSystemMemory
	 */
	public long getTotalSystemMemory() {
		return totalSystemMemory;
	}


	/**
	 * @param totalSystemMemory the totalSystemMemory to set
	 */
	public void setTotalSystemMemory(long totalSystemMemory) {
		this.totalSystemMemory = totalSystemMemory;
	}


	/**
	 * @return the totalSystemMemoryHuman
	 */
	public String getTotalSystemMemoryHuman() {
		return totalSystemMemoryHuman;
	}


	/**
	 * @param totalSystemMemoryHuman the totalSystemMemoryHuman to set
	 */
	public void setTotalSystemMemoryHuman(String totalSystemMemoryHuman) {
		this.totalSystemMemoryHuman = totalSystemMemoryHuman;
	}


	/**
	 * @return the usedMemoryLua
	 */
	public long getUsedMemoryLua() {
		return usedMemoryLua;
	}


	/**
	 * @param usedMemoryLua the usedMemoryLua to set
	 */
	public void setUsedMemoryLua(long usedMemoryLua) {
		this.usedMemoryLua = usedMemoryLua;
	}


	/**
	 * @return the usedMemoryLuaHuman
	 */
	public String getUsedMemoryLuaHuman() {
		return usedMemoryLuaHuman;
	}


	/**
	 * @param usedMemoryLuaHuman the usedMemoryLuaHuman to set
	 */
	public void setUsedMemoryLuaHuman(String usedMemoryLuaHuman) {
		this.usedMemoryLuaHuman = usedMemoryLuaHuman;
	}


	/**
	 * @return the usedMemoryScripts
	 */
	public long getUsedMemoryScripts() {
		return usedMemoryScripts;
	}


	/**
	 * @param usedMemoryScripts the usedMemoryScripts to set
	 */
	public void setUsedMemoryScripts(long usedMemoryScripts) {
		this.usedMemoryScripts = usedMemoryScripts;
	}


	/**
	 * @return the usedMemoryScriptsHuman
	 */
	public String getUsedMemoryScriptsHuman() {
		return usedMemoryScriptsHuman;
	}


	/**
	 * @param usedMemoryScriptsHuman the usedMemoryScriptsHuman to set
	 */
	public void setUsedMemoryScriptsHuman(String usedMemoryScriptsHuman) {
		this.usedMemoryScriptsHuman = usedMemoryScriptsHuman;
	}


	/**
	 * @return the numberOfCachedScripts
	 */
	public int getNumberOfCachedScripts() {
		return numberOfCachedScripts;
	}


	/**
	 * @param numberOfCachedScripts the numberOfCachedScripts to set
	 */
	public void setNumberOfCachedScripts(int numberOfCachedScripts) {
		this.numberOfCachedScripts = numberOfCachedScripts;
	}


	/**
	 * @return the maxmemory
	 */
	public long getMaxmemory() {
		return maxmemory;
	}


	/**
	 * @param maxmemory the maxmemory to set
	 */
	public void setMaxmemory(long maxmemory) {
		this.maxmemory = maxmemory;
	}


	/**
	 * @return the maxmemoryHuman
	 */
	public String getMaxmemoryHuman() {
		return maxmemoryHuman;
	}


	/**
	 * @param maxmemoryHuman the maxmemoryHuman to set
	 */
	public void setMaxmemoryHuman(String maxmemoryHuman) {
		this.maxmemoryHuman = maxmemoryHuman;
	}


	/**
	 * @return the maxmemoryPolicy
	 */
	public String getMaxmemoryPolicy() {
		return maxmemoryPolicy;
	}


	/**
	 * @param maxmemoryPolicy the maxmemoryPolicy to set
	 */
	public void setMaxmemoryPolicy(String maxmemoryPolicy) {
		this.maxmemoryPolicy = maxmemoryPolicy;
	}


	/**
	 * @return the allocatorFragRatio
	 */
	public double getAllocatorFragRatio() {
		return allocatorFragRatio;
	}


	/**
	 * @param allocatorFragRatio the allocatorFragRatio to set
	 */
	public void setAllocatorFragRatio(double allocatorFragRatio) {
		this.allocatorFragRatio = allocatorFragRatio;
	}


	/**
	 * @return the allocatorFragBytes
	 */
	public long getAllocatorFragBytes() {
		return allocatorFragBytes;
	}


	/**
	 * @param allocatorFragBytes the allocatorFragBytes to set
	 */
	public void setAllocatorFragBytes(long allocatorFragBytes) {
		this.allocatorFragBytes = allocatorFragBytes;
	}


	/**
	 * @return the allocatorRssRatio
	 */
	public double getAllocatorRssRatio() {
		return allocatorRssRatio;
	}


	/**
	 * @param allocatorRssRatio the allocatorRssRatio to set
	 */
	public void setAllocatorRssRatio(double allocatorRssRatio) {
		this.allocatorRssRatio = allocatorRssRatio;
	}


	/**
	 * @return the allocatorRssBytes
	 */
	public long getAllocatorRssBytes() {
		return allocatorRssBytes;
	}


	/**
	 * @param allocatorRssBytes the allocatorRssBytes to set
	 */
	public void setAllocatorRssBytes(long allocatorRssBytes) {
		this.allocatorRssBytes = allocatorRssBytes;
	}


	/**
	 * @return the rssOverheadRatio
	 */
	public double getRssOverheadRatio() {
		return rssOverheadRatio;
	}


	/**
	 * @param rssOverheadRatio the rssOverheadRatio to set
	 */
	public void setRssOverheadRatio(double rssOverheadRatio) {
		this.rssOverheadRatio = rssOverheadRatio;
	}


	/**
	 * @return the rssOverheadBytes
	 */
	public long getRssOverheadBytes() {
		return rssOverheadBytes;
	}


	/**
	 * @param rssOverheadBytes the rssOverheadBytes to set
	 */
	public void setRssOverheadBytes(long rssOverheadBytes) {
		this.rssOverheadBytes = rssOverheadBytes;
	}


	/**
	 * @return the memFragmentationRatio
	 */
	public double getMemFragmentationRatio() {
		return memFragmentationRatio;
	}


	/**
	 * @param memFragmentationRatio the memFragmentationRatio to set
	 */
	public void setMemFragmentationRatio(double memFragmentationRatio) {
		this.memFragmentationRatio = memFragmentationRatio;
	}


	/**
	 * @return the memFragmentationBytes
	 */
	public long getMemFragmentationBytes() {
		return memFragmentationBytes;
	}


	/**
	 * @param memFragmentationBytes the memFragmentationBytes to set
	 */
	public void setMemFragmentationBytes(long memFragmentationBytes) {
		this.memFragmentationBytes = memFragmentationBytes;
	}


	/**
	 * @return the memNotCountedForEvict
	 */
	public long getMemNotCountedForEvict() {
		return memNotCountedForEvict;
	}


	/**
	 * @param memNotCountedForEvict the memNotCountedForEvict to set
	 */
	public void setMemNotCountedForEvict(long memNotCountedForEvict) {
		this.memNotCountedForEvict = memNotCountedForEvict;
	}


	/**
	 * @return the memReplicationBacklog
	 */
	public long getMemReplicationBacklog() {
		return memReplicationBacklog;
	}


	/**
	 * @param memReplicationBacklog the memReplicationBacklog to set
	 */
	public void setMemReplicationBacklog(long memReplicationBacklog) {
		this.memReplicationBacklog = memReplicationBacklog;
	}


	/**
	 * @return the memClientsSlaves
	 */
	public int getMemClientsSlaves() {
		return memClientsSlaves;
	}


	/**
	 * @param memClientsSlaves the memClientsSlaves to set
	 */
	public void setMemClientsSlaves(int memClientsSlaves) {
		this.memClientsSlaves = memClientsSlaves;
	}


	/**
	 * @return the memClientsNormal
	 */
	public int getMemClientsNormal() {
		return memClientsNormal;
	}


	/**
	 * @param memClientsNormal the memClientsNormal to set
	 */
	public void setMemClientsNormal(int memClientsNormal) {
		this.memClientsNormal = memClientsNormal;
	}


	/**
	 * @return the memAofBuffer
	 */
	public long getMemAofBuffer() {
		return memAofBuffer;
	}


	/**
	 * @param memAofBuffer the memAofBuffer to set
	 */
	public void setMemAofBuffer(long memAofBuffer) {
		this.memAofBuffer = memAofBuffer;
	}


	/**
	 * @return the memAllocator
	 */
	public String getMemAllocator() {
		return memAllocator;
	}


	/**
	 * @param memAllocator the memAllocator to set
	 */
	public void setMemAllocator(String memAllocator) {
		this.memAllocator = memAllocator;
	}


	/**
	 * @return the activeDefragRunning
	 */
	public int getActiveDefragRunning() {
		return activeDefragRunning;
	}


	/**
	 * @param activeDefragRunning the activeDefragRunning to set
	 */
	public void setActiveDefragRunning(int activeDefragRunning) {
		this.activeDefragRunning = activeDefragRunning;
	}


	/**
	 * @return the lazyfreePendingObjects
	 */
	public int getLazyfreePendingObjects() {
		return lazyfreePendingObjects;
	}


	/**
	 * @param lazyfreePendingObjects the lazyfreePendingObjects to set
	 */
	public void setLazyfreePendingObjects(int lazyfreePendingObjects) {
		this.lazyfreePendingObjects = lazyfreePendingObjects;
	}


	/**
	 * @return the lazyfreedObjects
	 */
	public int getLazyfreedObjects() {
		return lazyfreedObjects;
	}


	/**
	 * @param lazyfreedObjects the lazyfreedObjects to set
	 */
	public void setLazyfreedObjects(int lazyfreedObjects) {
		this.lazyfreedObjects = lazyfreedObjects;
	}


	/**
	 * TODO from string에서 getter/Setter 찾는 로직 넣기.
	 * 
	 * @param content
	 * @return
	 */
	public static Memory convert(String content) {
		Memory memory = new Memory();
		String[] lines = content.split("\r\n");

		for (String line : lines) {
			String[] parts = line.split(":");
			if (parts.length == 2) {
				String key = parts[0].trim();
				String value = parts[1].trim();

				key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);

				RedisInfoConverter.setField(memory, key, value);
			}
		}

		return memory;
	}
}